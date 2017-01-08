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
     
	 private Thread objectThread; 
        //constructor for metaagents only takes in name atm, needs to have other things overloaded onto it from subclasses like scope, parent
        public MetaAgent(String name) {   
            this.name = name;
	    objectThread = new Thread(this);
	    objectThread.start();

        }

	 //constructor for metaagents only takes in name atm, needs to have other things overloaded onto it from subclasses like scope, parent
        public MetaAgent(String name, MetaAgent scope) {   
            this.name = name;
	    objectThread = new Thread(this);
	    objectThread.start();
	    this.scope = scope;

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
	abstract void handle(Message message);


	public MetaAgent getScope(){
		return scope;
	}

	public MetaAgent getScope(String name){
		return scope;
	}



    public void setName(String name) {
        this.name = name;
    }

	@Override
	public String toString() {
		return name;
	}


	@Override
	public void run() {

		while(true){
			if(!queue.isEmpty())
			handle(queue.remove());
			
		}
	}


}

    