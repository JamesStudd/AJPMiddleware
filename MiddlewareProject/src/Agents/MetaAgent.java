/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Messages.Message;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the abstract building blocks for agents
 */
public abstract class MetaAgent implements Runnable {

	private Thread objectThread;
	private MetaAgent scope;
	private String name;
	private LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

	private Object lock = new Object();

	/**
	 * Constructor starts a new thread per instatiation
	 *
	 * @param name - The name of the object
	 */
	public MetaAgent(String name) {
		this.name = name;
		objectThread = new Thread(this);
		objectThread.start();
	}

	/**
	 * Constructor that also contains a scope
	 *
	 * @param name - The name of the object
	 * @param scope - The scope by which the object is defined
	 */
	public MetaAgent(String name, MetaAgent scope) {
		this.name = name;
		objectThread = new Thread(this);
		objectThread.start();
		this.scope = scope;

	}

	/**
	 * Adds a message to the queue and lets the handle method know we have a new entry
	 *
	 * @param message - The message to be added
	 */
	public void addToQueue(Message message) {
		synchronized (lock) {
			queue.add(message);
			lock.notify();
		}
	}

	/**
	 * An abstract handle method that deals with the passed message object
	 *
	 * @param message
	 */
	abstract void handle(Message message);

	/**
	 * Get the scope by which the object is defined
	 *
	 * @return
	 */
	public MetaAgent getScope() {
		return scope;
	}

	/**
	 * Gets the scope by name
	 *
	 * @param name - Name of the node that the scope is required
	 * @return - The nodes scope
	 */
	public MetaAgent getScope(String name) {
		return scope;
	}

	/**
	 * Gets the name of the object
	 *
	 * @return - the name given to the object
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * The run method continually removes object from the blocking queue as
	 * they come in and handles them
	 */
	@Override
	public void run() {

		synchronized (lock) {
			while (true) {
				try {
					handle(queue.remove());
				} catch (java.util.NoSuchElementException e) {
					try {
						lock.wait();
					} catch (InterruptedException ex) {
						Logger.getLogger(MetaAgent.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}
	}

}
