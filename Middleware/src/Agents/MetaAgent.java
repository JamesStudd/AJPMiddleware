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
 * 
 * 
 */

//Adding a comment to check we are working ok 
public abstract class MetaAgent implements Runnable {
     
        //constructor for metaagents only takes in name atm, needs to have other things overloaded onto it from subclasses like scope, parent
        public MetaAgent(String name) {   
            thread.start();
            this.name = name;
       }
        
	private Portal scope;

        //Name for user ease/debugging
        private String name;

	//This should be in the meta object
	private LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

	//Should be in the meta
	private Thread thread = new Thread();
	
	//Public enabling others to add messages to its queue
	public void addToQueue(Message message){
		queue.add(message);
	}

	public Portal getScope(){
		return scope;
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

    
