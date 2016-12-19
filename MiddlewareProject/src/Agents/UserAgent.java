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
 * @author Sean
 */public class UserAgent implements Runnable {

	//The direct parent of the node
	private UserAgent parent = null;

	//Should be maybe moved to the meta agent, this of course depending on what we do with the nodeMonitor
	private HashMap<UserAgent, UserAgent> registeredAddresses = new HashMap<UserAgent, UserAgent>();

	//This should be in the meta object
	private LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

	//Should be in the meta
	private Thread thread = new Thread();
	
	//Public enabling others to add messages to its queue
	public void addToQueue(Message message){
		queue.add(message);
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

    
}
