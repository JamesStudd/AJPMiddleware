/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Messages.Message;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Sean
 */public abstract class MetaAgent implements Runnable {
     
        //constructor for metaagents only takes in name atm, needs to have other things overloaded onto it from subclasses like scope, parent
        public MetaAgent(String name) {   
            thread.start();
            this.name = name;
        }
        
        //Name for user ease/debugging
        private String name;

	//This should be in the meta object
	private LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

	private MetaAgent scope;

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


	public MetaAgent getScope(){
		return scope;
	}


	@Override
	public void run() {

		while(true){
			handle(queue.remove());
			
		}
	}


}

    