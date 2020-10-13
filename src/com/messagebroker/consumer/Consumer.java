package com.messagebroker.consumer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.messagebroker.IMessageBroker;
import com.messagebroker.message.Message;
import com.messagebroker.queue.IQueue;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Consumer implements IConsumerCallback {

	private String name;
	private Map<String, IQueue> queues;
	private Thread thread;
	private boolean isRunning;
	private List<String> topics;
	private IMessageBroker broker;

	public Consumer(String name, Map<String, IQueue> queues, Thread thread, final boolean isRunning,
			List<String> topics, IMessageBroker broker) {
		this.name = name;

		this.queues = new HashMap<>();

		for (String topic : topics) {
			broker.register(topic, this);
		}
		
		readFromQueue();

		 
	}

	public Consumer(String name, List<String> topics) {
		this.name = name;
		this.topics = topics;
		queues = new HashMap<>();

	}

	 

	private void readFromQueue() {
		Runnable task = () -> {
			// System.out.println("Task is running");
			Collection<IQueue> s = queues.values();
			while (isRunning) {

				for (IQueue queue : s) {
					
					//System.out.println(" reading from Consumer " + this.name + " topic:" + queue.getTopicName());
					Message m = queue.read();
					
					System.out.printf("reading from Consumer: %s ,topis :%s,data :%s \n", this.name,queue.getTopicName(),m.getData());
					 
					processMessage(m);
				}
			}
		};

		thread = new Thread(task);
		thread.start();
		isRunning = true;
	}

	private void processMessage(Message m) {
		try {

			Thread.sleep(10);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void setQueue(IQueue queue) {
		queues.put(queue.getTopicName(), queue);

	}

 

	 

}
