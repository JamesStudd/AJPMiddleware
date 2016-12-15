/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import com.sun.xml.internal.ws.wsdl.writer.document.Message;
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

	
	
	private Set<UserAgent> children = new HashSet<UserAgent>();
	private UserAgent parent = null;
	public  LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();
	private Thread thread = new Thread();
	private Set<NodeMonitor> monitors = new HashSet<NodeMonitor>();


	public Portal() {
		thread.start();
	}

	private void addChild(UserAgent child){
		children.add(child);
	}

	private void removeChild(UserAgent child){
		children.remove(child);
	}

	private void addMonitor(Monitor mon){
		monitors.add(mon);
	}

	private void removeMontior(Monitor mon){
		monitors.remove(mon);
	}
	
	public void addToQueue(Message message){
		queue.add(message);
	}


	private void updateMonitors(Message message){
		Iterator<NodeMonitor> it = monitors.iterator();
		while(it.hasNext()){
			it.next().addToQueue(Message message);
		}
	}

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
