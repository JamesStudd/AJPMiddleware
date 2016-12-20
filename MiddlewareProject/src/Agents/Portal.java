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

/**
 *
 * @author chris
 */
public class Portal extends MetaAgent {

	//The children of the node
	private Set<MetaAgent> children = new HashSet<MetaAgent>();

	//The direct parent of the node
	private MetaAgent parent = null;

	//Should be maybe moved to the meta agent, this of course depending on what we do with the nodeMonitor
	private HashMap<MetaAgent, MetaAgent> registeredAddresses = new HashMap<MetaAgent, MetaAgent>();

	//Undelivered messages
	private BoundedHashMap<MetaAgent, Message> lostMessages = new BoundedHashMap<MetaAgent, Message>(5);

	//This should be in the meta object
	private LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

	//Should be in the meta
	private Thread thread = new Thread();

	//A set of all the active monitors of the portal
	private Set<NodeMonitor> monitors = new HashSet<NodeMonitor>();

	//Constructor starts the thread
	public Portal(String name) {
		super(name);
		thread.start();
	}

	//Adds a node to its children
	private void addChild(MetaAgent child) {
		children.add(child);
	}

	//Removes a node from its children
	private void removeChild(MetaAgent child) {
		children.remove(child);
	}

	//Adds a monitor to report to with messages
	private void addMonitor(MetaAgent mon) {
		monitors.add(mon);
	}

	//Removes a monitor 
	private void removeMontior(MetaAgent mon) {
		monitors.remove(mon);
	}

	//Public enabling others to add messages to its queue
	public void addToQueue(Message message) {
		queue.add(message);
	}

	//Forwards a message on to the recepients 
	private void sendMessage(Message message) {
		MetaAgent recepient = message.getRecipient();
		if (registeredAddresses.containsKey(recepient)) {
			registeredAddresses.get(recepient).addToQueue(message);
		} else {

		}
	}

	//Updates all current monitors with information about messages coming through
	private void updateMonitors(Message message) {
		Iterator<NodeMonitor> it = monitors.iterator();
		while (it.hasNext()) {
			it.next().addToQueue(Message message
	);
		}

	}

	//If the passes meta agent is a reference to this object
	private boolean isForMe(MetaAgent x) {
		return x == this;
	}

	//Runs through all the children and updates with current addressbook
	private void updateChildrenWithAddressBook() {

		for (MetaAgent x : children) {
			x.addToQueue(new Message(MessageType.UPDATE_ADDRESSES, this, x, registeredAddresses));
		}
	}

	//Updates parent with address book, filters any node addresses whos scope is this node
	private void updateParentWithAddressBook() {
		Map<MetaAgent, MetaAgent> filtered = registeredAddresses.keySet().stream()
			.filter(map -> map.getScope() != this)
			.collect(Collectors.toMap(p -> p, p -> registeredAddresses.get(p)));

		parent.addToQueue(new Message(MessageType.UPDATE_ADDRESSES, this, parent, filtered));
	}

	//Adds a node  to the portals children and updates the address
	private void addNode(MetaAgent node) {

		children.add(node);
		registeredAddresses.put(node, this);
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
		HashMap<MetaAgent, MetaAgent> passedIn = (HashMap< MetaAgent, MetaAgent>) message.retrieveMessageItem();
		if (passedIn.equals(registeredAddresses)) {
			return;
		}
		registeredAddresses.putAll(passedIn);
		updateChildrenWithAddressBook();
		updateParentWithAddressBook();

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

		}

	}

	//Looks up the message address and passes on the message if its held in registered addresses
	//otherwise an error is send back and the message added to lost messages
	private void lookUpAndPassOn(Message message) {

		if (registeredAddresses.containsKey(message.getRecipient())) {
			registeredAddresses.get(message.getRecipient()).addToQueue(message);
		} else {
			//Need to think about what happens if a message is already in lost messages to an agent, meaning we will have a duplication and overide
			Message removed = lostMessages.putAndRetrieveLostValue(message.getRecipient(), message);
			registeredAddresses.get(message.getSender()).addToQueue(new Message(MessageType.ADDRESS_NOT_IN_LOST_PROPERTY, this, message.getSender(), null));
			if(removed!= null){
				registeredAddresses.get(removed.getSender()).addToQueue(new Message(MessageType.FAILED_TO_DELIVER, this, message.getSender(), removed));
			}
		}

	}

	//Handles a message pull
	private void handle(Message message) {
		updateMonitors(message);
		if (isForMe(message.getRecipient())) {
			extractMessageDetailsAndHandle(message);
		} else {
			lookUpAndPassOn(message);
		}
	}

	//Merge 2 portals together
	private void mergePortal(MetaAgent portal) {
		//something about merging portals here
	}

	@Override
	public void run() {

		while (true) {
			handle(queue.remove());

		}
	}

}
