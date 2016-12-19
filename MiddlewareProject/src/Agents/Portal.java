/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import com.sun.xml.internal.ws.wsdl.writer.document.Message;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import javax.management.monitor.Monitor;

/**
 *
 * @author chris
 */
public class Portal implements Runnable{

	
	
	//The children of the node
	private Set<UserAgent> children = new HashSet<UserAgent>();

	//The direct parent of the node
	private UserAgent parent = null;

	//Should be maybe moved to the meta agent, this of course depending on what we do with the nodeMonitor
	private HashMap<UserAgent, UserAgent> registeredAddresses = new HashMap<UserAgent, UserAgent>();

	//This should be in the meta object
	private LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

	//Should be in the meta
	private Thread thread = new Thread();

	//A set of all the active monitors of the portal
	private Set<NodeMonitor> monitors = new HashSet<NodeMonitor>();


	//Constructor starts the thread
	public Portal() {
		thread.start();
	}

	//Adds a node to its children
	private void addChild(UserAgent child){
		children.add(child);
	}

	//Adds a monitor to report to with messages
	private void addMonitor(Monitor mon){
		monitors.add(mon);
	}

	//Removes a monitor 
	private void removeMontior(Monitor mon){
		monitors.remove(mon);
	}
	
	//Public enabling others to add messages to its queue
	public void addToQueue(Message message){
		queue.add(message);
	}

	//Forwards a message on to the recepients 
	private void sendMessage(Message message){
		UserAgent recepient = message.getRecepient();
		if(registeredAddresses.containsKey(recepient)){
			registeredAddresses.get(recepient).addToQueue(message);
		}
		else{
			
		}
	}


	//Updates all current monitors with information about messages coming through
	private void updateMonitors(Message message){
		Iterator<NodeMonitor> it = monitors.iterator();
		while(it.hasNext()){
			it.next().addToQueue(Message message);
		}
	}

	//Handles a message pull
	private void handle(Message message){
		updateMonitors(message);
		

	}

	@Override
	public void run() {

		while(true){
			handle(queue.remove());
			
		}
	}


}
