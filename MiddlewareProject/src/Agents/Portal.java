/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Messages.Message;
import Messages.MessageType;
import Utility.BoundedHashMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author chris
 */
public class Portal extends MetaAgent {

	//The children of the node
	private HashMap<String, MetaAgent> children = new HashMap<String, MetaAgent>();

	//The direct parent of the node
	private MetaAgent parent = null;

	//Should be maybe moved to the meta agent, this of course depending on what we do with the nodeMonitor
	private HashMap<String, MetaAgent> registeredAddresses = new HashMap<String, MetaAgent>();

	//Undelivered messages
	private BoundedHashMap<String, Message> lostMessages = new BoundedHashMap<String, Message>(5);

	//This should be in the meta object
	private LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

	//A set of all the active monitors of the portal
	private Set<NodeMonitor> monitors = new HashSet<NodeMonitor>();

	//Constructor starts the thread
	public Portal(String name) {
		super(name);
	}

	public Portal(String name, MetaAgent parent){
		super(name);
		this.parent = parent;
		
		parent.addToQueue(new Message(MessageType.ADD_NODE, this.toString(), this.parent.toString(), this));
	}


	//Adds a monitor to report to with messages
	private void addMonitor(NodeMonitor mon) {
		monitors.add(mon);
	}

	//Removes a monitor 
	private void removeMontior(MetaAgent mon) {
		monitors.remove(mon);
	}

	//Updates all current monitors with information about messages coming through
	private void updateMonitors(Message message) {
		Iterator<NodeMonitor> it = monitors.iterator();
		while (it.hasNext()) {
			it.next().addToQueue(message);
		};
	}

	//If the passes meta agent is a reference to this object
	private boolean isForMe(String x) {
		return x == this.toString();
	}

	//Runs through all the children and updates with current addressbook
	private void updateChildrenWithAddressBook() {
		children.values().forEach(a -> {
				if(a.getClass() == this.getClass())	
				a.addToQueue(new Message<>(MessageType.UPDATE_ADDRESSES, this.toString(), a.toString(), registeredAddresses));
			});
	}

	//Updates parent with address book, filters any node addresses whos scope is this node
	private void updateParentWithAddressBook(Map<String,MetaAgent> toBePassedUp) {
		if (parent == null || toBePassedUp == null) {
			return;
		}

		parent.addToQueue(new Message<Map<String, MetaAgent>>(MessageType.UPDATE_ADDRESSES, this.toString(), parent.toString(), toBePassedUp));
	}

	//Adds a node  to the portals children and updates the address
	private void addNode(MetaAgent node) {

		children.put(node.toString(), node);
		registeredAddresses.put(node.toString(), node);
		updateChildrenWithAddressBook();
		if (!scopedHere(node.getScope())) {
			updateParentWithAddressBook(getAddressesNotScopedHere());
		}
		else{

		}
	}

	//True is the scope of the metaAgent is this node
	private boolean scopedHere(MetaAgent scope) {
		return scope == this;
	}

	private Map<String, MetaAgent> getAddressesNotScopedHere(){

		Map<String, MetaAgent> toBePassedUp = new HashMap<>();

		
		Iterator<String> allFromMap = registeredAddresses.keySet().iterator();
		while(allFromMap.hasNext()){
			String next = allFromMap.next();
			if(registeredAddresses.get(next).getScope() != this){
				toBePassedUp.put(next, registeredAddresses.get(next));
			}
		}

		return  toBePassedUp;

	}

	//Method used for dubugging
	public void showAddresses(){
		
		System.out.println("Showing addresses");
		Iterator<String> t = registeredAddresses.keySet().iterator();
		while(t.hasNext()){

			System.out.println(t.next());
		}
	}
	//Updates the address book by first checking if there are any changes (returns if this is the case)
	//This adds all entries of the passed in address bokk to current address book then updtes both children and parent
	private void updateAddressBook(Message message) {

		HashMap<String, MetaAgent> passedIn = (HashMap< String, MetaAgent>) message.retrieveMessageItem();
		HashMap<String, MetaAgent> notScoped = (HashMap) getAddressesNotScopedHere();
		
		if (passedIn.equals(notScoped)) {
			return;
		}
		registeredAddresses.putAll(passedIn);
		updateChildrenWithAddressBook();
		updateParentWithAddressBook(notScoped);
		System.out.println("need to add a method that checks if we can allocate any lost mail");

	}

	//Extracts the message details and handles as appropriate
	private void extractMessageDetailsAndHandle(Message message) {

		switch (message.getMessageType()) {

			case PASS_MESSAGE:
				String theMessage = (String) message.retrieveMessageItem();
				System.out.println("I am a message to the portal " + theMessage) ;
				break;
			case ADD_NODE:
				addNode((MetaAgent) message.retrieveMessageItem());
				break;
			case UPDATE_ADDRESSES:
				updateAddressBook(message);
				break;
			case ADDRESS_NOT_FOUND_MOVED_TO_LOST_PROPERTY:
				System.out.println("To do, should be an error");
				break;
			case ERROR:
				System.out.println("To do, need to think about what errors we will find");
				break;

			case FAILED_TO_DELIVER:
				System.out.println("This should not make it here");
				break;

		}

	}

	//Looks up the message address and passes on the message if its held in registered addresses
	//otherwise an error is send back and the message added to lost messages
	private void lookUpAndPassOn(Message message) {

		if (registeredAddresses.containsKey(message.getRecipient())) {
			if (children.containsKey(message.getRecipient())) {
				children.get(message.getRecipient()).addToQueue(message);
			} else {
				registeredAddresses.get(message.getRecipient()).addToQueue(message);
			}
		} else {

			Message removed;
			//Makes sure we are not losing a message when a node already has mail in lost property. Sends an error if this is the case 
			//due to the message being overriden in lost property
			if (lostMessages.contains(message.getRecipient())) {
				removed = lostMessages.get(message.getRecipient());

			} else {

				removed = lostMessages.putAndRetrieveLostValue(message.getRecipient(), message);

			}
			registeredAddresses.get(message.getSender()).addToQueue(new Message(MessageType.ADDRESS_NOT_FOUND_MOVED_TO_LOST_PROPERTY, this.toString(), message.getSender(), message));
			if (removed != null) {
				registeredAddresses.get(removed.getSender()).addToQueue(new Message(MessageType.FAILED_TO_DELIVER, this.toString(), message.getSender(), removed));
			}
		}

	}

	//Handles a message pull
	@Override
	protected void handle(Message message) {
		updateMonitors(message);
		if (isForMe(message.getRecipient())) {
			extractMessageDetailsAndHandle(message);
		} else {
			lookUpAndPassOn(message);
		}
	}

}
