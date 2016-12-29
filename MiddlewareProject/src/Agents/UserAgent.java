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
	
        
        //public void sendMessage (Message message)();
        //parent.addToQueue?

	//Handles a message pull
	protected void handle(Message message){

		switch(message.getMessageType()){

			case PASS_MESSAGE:
				System.out.println(message.retrieveMessageItem());
		}
	}
        
        //A set of all the active monitors of the portal
	private Set<NodeMonitor> monitors = new HashSet<NodeMonitor>();


}

    

    