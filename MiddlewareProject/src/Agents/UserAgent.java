/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Messages.Message;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Sean
 */
public class UserAgent extends MetaAgent {
        
    
        //constructor for UserAgent
        public UserAgent(String name, MetaAgent parent, MetaAgent scope) {
            super(name);
            this.scope = scope;
            this.parent = parent;
        }
    
        private MetaAgent scope;
        
        private MetaAgent parent;
	
	//Public enabling others to add messages to its queue
	public void addToQueue(Message message){
		this.addToQueue(message);
	}      
        
        //public void sendMessage (Message message)();
        //parent.addToQueue?

	//Handles a message pull
	private void handle(Message message){
		updateMonitors(message);		
	}
        
        //A set of all the active monitors of the portal
	private Set<NodeMonitor> monitors = new HashSet<NodeMonitor>();

	@Override
	public void run() {

		while(true){
			handle(queue.remove());
			
		}
	}


}

    

    