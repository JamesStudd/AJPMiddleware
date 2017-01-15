/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Messages.Message;
import Messages.MessageType;
import java.util.ArrayList;

/**
 *
 * This class contains all the functionality of a user agent whom sends and
 * recieves messages
 */
public class UserAgent extends MetaAgent {



 	//An array of all the monitors watching this node
	ArrayList<NodeMonitor> monitors = new ArrayList();


	//The parent node of this agent
	private MetaAgent parent;


	/**
	 * This is the constructor of the UserAgent which the scope is set 
	 * and this nodes parent is made away of its creation
	 * @param name - The name of this node
	 * @param parent - The parent agent to this node
	 * @param scope  - The scope at which this node is visable
	 */
	public UserAgent(String name, MetaAgent parent, MetaAgent scope) {
		super(name, scope);
		this.parent = parent;
		updateParentWithDetails();
	}

	/**
	 * Adds a message to this nodes parent about its presence so that its parents
	 * address book can be updated
	 */
	private void updateParentWithDetails() {
		if (parent == null) {
			return;
		}
		parent.addToQueue(new Message(MessageType.ADD_NODE, this.toString(), parent.toString(), this));
	}


	/**
	 * Adds a node monitor to the monitors that are viewing it
	 * @param monitor  - The monitor to be added 
	 */
	private void addNodeMonitor(NodeMonitor monitor){
		monitors.add(monitor);
	}

	/**
	 * Passes over a message to a node by adding the message details to its parent
	 * @param to - The recipient of the message
	 * @param message  - The message that is to be passed over
	 */
	public void passOverAMessage(String to, String message) {
		parent.addToQueue(new Message(MessageType.PASS_MESSAGE, this.toString(), to, message));
	}

	/**
	 * Updates all the node monitors with the message that has be recieved
	 * @param message  - The message that has been recieved
	 */
	private void updateNodeMonitors(Message message){
		monitors.forEach(a ->  a.addToQueue(message));
	}

	/**
	 * This method handles messages being passed into this user agent by extracting the 
	 * message details , letting the nodeMonitors know and will deal with it 
	 * depending on the type of message it is. 
	 * @param message  - The message passed in that needs handling
	 */
	protected void handle(Message message) {

		updateNodeMonitors(message);

		switch (message.getMessageType()) {

			case PASS_MESSAGE:
				String theMessage = (String) message.retrieveMessageItem(); //Is this when the user has recieved a message
				System.out.println(this.toString() + " Recieved message: " + theMessage);
				break;
			case ADD_NODE: 
				System.out.println("Error, User Agents cannot have nodes created on them!");
				parent.addToQueue(new Message<String>(MessageType.ERROR,
									this.toString(),
									message.getSender(),
									"Error, User Agents cannot have nodes created on them!"));
				break;
			case UPDATE_ADDRESSES:
				break;
			case ADDRESS_NOT_FOUND_MOVED_TO_LOST_PROPERTY:
				System.out.println("Address not found, holding message in lost property.");
				System.out.println("Message was to " +((Message) message.retrieveMessageItem()).getRecipient());
				System.out.println("The message was sent on " + ((Message) message.retrieveMessageItem()).getTheDateOfCreation());
 ;
				break;
			case ERROR:
				System.out.println("Error " + (String) message.retrieveMessageItem());
				break;
			case ADD_NODE_MONITOR:
				addNodeMonitor((NodeMonitor) message.retrieveMessageItem());
				break;
				
			case FAILED_TO_DELIVER:
				System.out.println("A message has been unable to be delivered, here is the message contents");
				System.out.println("Message was to " +((Message) message.retrieveMessageItem()).getRecipient());
				System.out.println("The message was sent on " + ((Message) message.retrieveMessageItem()).getTheDateOfCreation());
				break;
		}
	}

}
