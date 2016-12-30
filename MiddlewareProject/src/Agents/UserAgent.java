/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Messages.Message;
import Messages.MessageType;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Sean
 */
public class UserAgent extends MetaAgent {
        
    
        //constructor for UserAgent
        public UserAgent(String name, MetaAgent parent, MetaAgent scope) {
            super(name, scope);
            this.parent = parent;
	    updateParentWithDetails();
        }

	private void updateParentWithDetails(){
		if(parent == null) return;
		System.out.println("\n\n\nam updating  after being created " + this.toString());
		parent.addToQueue(new Message(MessageType.ADD_NODE, this.toString(), parent.toString(), this));
	}
    
        
        private MetaAgent parent;
	
        
        //public void sendMessage (Message message)();
        //parent.addToQueue?

	//Handles a message pull
	protected void handle(Message message){

		switch (message.getMessageType()) {
                    //Cases that cannot happen need to return a message of type error onto the senders queue
                    //Only really handle pass message, failed to deliver and address not found
                    //update addresses should be a return it only sends msgs to its parent

			case PASS_MESSAGE:
				String theMessage = (String) message.retrieveMessageItem(); //Is this when the user has recieved a message
				System.out.println("This is the message " + theMessage);
				break;
			case ADD_NODE: //not used in users
				System.out.println("Error, User Agents cannot have nodes created on them!"); 
                                parent.addToQueue(new Message<String> (MessageType.ERROR, this.toString(),message.getSender(), "Error, User Agents cannot have nodes created on them!"));
				break;
			case UPDATE_ADDRESSES: //not used in users also needs to send an error msg back
				break;
			case ADDRESS_NOT_FOUND_MOVED_TO_LOST_PROPERTY: //Is this going to go through the system and try to find a user or does it know straight away it doesn't exist
				System.out.println("To do, should be an error");
				break;
			case ERROR:
				System.out.println("To do, need to think about what errors we will find");

			case FAILED_TO_DELIVER:
                            //How is this different to address not found or error messages?
                                parent.addToQueue(new Message<String> (MessageType.ERROR, this.toString(),message.getSender(), "Error or something"));
				System.out.println("Shouldn't come here, need to do. Maybe create an error message");
		}
	}
        
        //A set of all the active monitors of the portal
	private Set<NodeMonitor> monitors = new HashSet<NodeMonitor>();


}

    

    