/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Agents;

import Messages.Message;
import Messages.MessageType;
import Utility.BoundedHashMap;
import java.util.ArrayList;
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
	private HashMap<String,MetaAgent> children = new HashMap<String,MetaAgent>();

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

		children.values().forEach(a -> a.addToQueue(new Message<>(MessageType.UPDATE_ADDRESSES, this.toString(), a.toString(), registeredAddresses)));
	}

	//Updates parent with address book, filters any node addresses whos scope is this node
	private void updateParentWithAddressBook() {
		if(parent == null) return;
		Stream<MetaAgent> filter = registeredAddresses.values().stream().filter(map -> map.getScope() != this);

		Map<String, MetaAgent> toBePassedUp = filter.collect(Collectors.toMap(p -> p.toString(), p -> {
			if(registeredAddresses.containsKey(p)){
				return registeredAddresses.get(p);
			}
			else{
				return p;
			}
			}));

		if(toBePassedUp == null)return;

		parent.addToQueue(new Message<Map<String,MetaAgent>>(MessageType.UPDATE_ADDRESSES, this.toString(), parent.toString(), toBePassedUp));
	}

	//Adds a node  to the portals children and updates the address
	private void addNode(MetaAgent node) {

		children.put(node.toString(), node);
		registeredAddresses.put(node.toString(), this);
		updateChildrenWithAddressBook();
		if (!scopedHere(node.getScope())) {
			updateParentWithAddressBook();
		}
	}

	//True is the scope of the metaAgent is this node
	private boolean scopedHere(MetaAgent scope) {
		return scope == this;
	}

	//Updates the address book by first checking if there are any changes (returns if this is the case)
	//This adds all entries of the passed in address bokk to current address book then updtes both children and parent
	private void updateAddressBook(Message message) {
		HashMap<String, MetaAgent> passedIn = (HashMap< String, MetaAgent>) message.retrieveMessageItem();
		if (passedIn.equals(registeredAddresses)) {
			return;
		}
		registeredAddresses.putAll(passedIn);
		updateChildrenWithAddressBook();
		updateParentWithAddressBook();
		System.out.println("need to add a method that checks if we can allocate any lost mail");

	}

	//Extracts the message details and handles as appropriate
	private void extractMessageDetailsAndHandle(Message message) {

		switch (message.getMessageType()) {

			case PASS_MESSAGE:
				String theMessage = (String) message.retrieveMessageItem();
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

			case FAILED_TO_DELIVER:
				System.out.println("Shouldn't come here, need to do. Maybe create an error message");

		}

	}

	//Looks up the message address and passes on the message if its held in registered addresses
	//otherwise an error is send back and the message added to lost messages
	private void lookUpAndPassOn(Message message) {


		if (registeredAddresses.containsKey(message.getRecipient())) {
			if(children.containsKey(message.getRecipient())){
				children.get(message.getRecipient()).addToQueue(message);
			}
			else{
				registeredAddresses.get(message.getRecipient()).addToQueue(message);
			}
		} else {
			//Need to think about what happens if a message is already in lost messages to an agent, meaning we will have a duplication and overide
			Message removed = lostMessages.putAndRetrieveLostValue(message.getRecipient(), message);
			registeredAddresses.get(message.getSender()).addToQueue(new Message(MessageType.ADDRESS_NOT_FOUND_MOVED_TO_LOST_PROPERTY, this.toString(), message.getSender(), null));
			if(removed!= null){
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