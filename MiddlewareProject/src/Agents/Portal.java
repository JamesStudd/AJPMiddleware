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
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author chris
 */
public class Portal extends MetaAgent {

	//The children of the node
	private Set<MetaAgent> children = new HashSet();

	//The direct parent of the node
	private MetaAgent parent = null;

	//This is the map of all addresses registerd 
	private ConcurrentHashMap<String, MetaAgent> registeredAddresses = new ConcurrentHashMap<String, MetaAgent>();

	//Undelivered messages
	private BoundedHashMap<String, Message> lostMessages = new BoundedHashMap<String, Message>(5);

	//A set of all the active monitors of the portal
	private Set<NodeMonitor> monitors = new HashSet<NodeMonitor>();

	//Constructor starts the thread
	public Portal(String name) {
		super(name);
		setParent(null);
	}

	//Constructor that takes a parent to the portal
	public Portal(String name, MetaAgent parent) {
		super(name);
		setParent(parent);

	}

	//Sets the parent and updates the parent with its address book
	public void setParent(MetaAgent parent) {
		this.parent = parent;
		if (parent != null) {
			registeredAddresses.put(parent.toString(), parent);
			if (children.isEmpty()) {
				parent.addToQueue(new Message(MessageType.ADD_NODE, this.toString(), this.parent.toString(), this));
			} else {
				parent.addToQueue(new Message(MessageType.ADD_NODE, this.toString(), parent.toString(), this));
				parent.addToQueue(new Message(MessageType.UPDATE_ADDRESSES, this.toString(), parent.toString(), prepareAddressBookForTransfer(true)));
			}
		}
	}

	public MetaAgent getParent() {
		return parent;
	}

	//Adds a monitor to report to with messages
	public void addMonitor(NodeMonitor mon) {
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

	//Removes the child from the map passed in
	private Map<String, MetaAgent> removeChild(Map<String, MetaAgent> m, MetaAgent child) {
		m.remove(child.toString());
		return m;
	}

	//Used for testing
	public ConcurrentHashMap<String, MetaAgent> getRegisteredAddresses() {
		return registeredAddresses;
	}

	private Map<String, MetaAgent> prepareAddressBookForTransfer(boolean forParent) {

		Map<String, MetaAgent> addressesToBePassedUp = new HashMap<>();

		Iterator<String> addresses = registeredAddresses.keySet().iterator();
		while (addresses.hasNext()) {
			String next = addresses.next();
			MetaAgent nextAd = registeredAddresses.get(next);
			if (nextAd.getScope(next) == this) {
				continue;
			}
			if (forParent) {
				if (children.contains(nextAd)) {
					addressesToBePassedUp.put(next, this);
				} else {
					addressesToBePassedUp.put(next, nextAd);
				}
			} else {
				addressesToBePassedUp.put(next, this);
			}
		}

		return addressesToBePassedUp;
	}

	//Runs through all the children and updates with current addressbook
	private void updateChildrenWithAddressBook() {
		children.forEach(a -> {
			if (a.getClass() == this.getClass()) {
				a.addToQueue(new Message<>(MessageType.UPDATE_ADDRESSES, this.toString(), a.toString(),
					removeChild(prepareAddressBookForTransfer(false), a)));
			}
		});
	}

	//Updates parent with address book, filters any node addresses whos scope is this node
	private void updateParentWithAddressBook() {
		if (parent == null) {
			return;
		}
		parent.addToQueue(new Message<Map<String, MetaAgent>>(MessageType.UPDATE_ADDRESSES, this.toString(), parent.toString(), prepareAddressBookForTransfer(true)));
	}

	private void passNodeToChildren(String node, MetaAgent address) {
		children.forEach((a) -> {
			if (!a.equals(address) && a.getClass() == this.getClass()) {
				a.addToQueue(new Message(MessageType.ADD_NODE, node, a.toString(), this));
			}
		});
	}

	//Adds a node  to the portals children and updates the address
	private void addNode(String node, MetaAgent address) {

		registeredAddresses.put(node, address);
		if (node == address.toString()) {
			children.add(address);
		}
		passNodeToChildren(node, address);
		if (parent != null && address != (parent) && address.getScope(node) != this) {
			parent.addToQueue(new Message(MessageType.ADD_NODE, node, parent.toString(), this));
		}

		if (lostMessages.contains(node)) {
			registeredAddresses.get(node).addToQueue(lostMessages.get(node));

		}

	}

	//True is the scope of the metaAgent is this node
	private boolean scopedHere(MetaAgent scope) {
		return scope == this;
	}

	//Method used for dubugging
	public void showAddresses() {

		System.out.println("Showing addresses for " + this.toString() + ":");
		Iterator<String> t = registeredAddresses.keySet().iterator();
		while (t.hasNext()) {
			String next = t.next();
			System.out.println(next + " : " + registeredAddresses.get(next));
		}
		System.out.println("Showing Children");
		Iterator<MetaAgent> chil = children.iterator();
		while (chil.hasNext()) {
			System.out.println(chil.next());
		}
	}

	//This method removes all address that are not to be added because  they point to this node or they are children of this node
	private HashMap<String, MetaAgent> getAllAddressesThatNeedUpdating(HashMap<String, MetaAgent> in, Map<String, MetaAgent> current) {
		Iterator<String> comingIn = in.keySet().iterator();
		ArrayList<String> toBeRemoved = new ArrayList();
		MetaAgent inAgent;
		MetaAgent currentAgent;
		boolean somethingIsDueToChange = false;
		while (comingIn.hasNext()) {
			String next = comingIn.next();
			if (current.containsKey(next)) {
				inAgent = in.get(next);
				currentAgent = current.get(next);
				if (next.equals(this.toString())) {
					toBeRemoved.add(next);
				}
				if (inAgent == this || children.contains(registeredAddresses.get(next))) {
					toBeRemoved.add(next);
				} else if (inAgent != currentAgent) {
					somethingIsDueToChange = true;
				}
			} else {
				somethingIsDueToChange = true;
			}
		}
		toBeRemoved.forEach(a -> in.remove(a));
		if (!somethingIsDueToChange) {
			return null;
		}
		return in;
	}

	//Shows all the addresses and values of a passed node - this method is used for debugging 
	private void showAddresses(Map<String, MetaAgent> in) {
		in.keySet().forEach((a) -> System.out.println(a + " : " + in.get(a).toString()));
	}

	/**
	 * This methods checks the passed maps entries to see if we have any
	 * lost mail that needs to be handed over to the new agents
	 *
	 * @param newAddresses
	 */
	private void checkForAnyLostMail(HashMap<String, MetaAgent> newAddresses) {
		Iterator<String> newAddressEntries = newAddresses.keySet().iterator();

		while (newAddressEntries.hasNext()) {
			String next = newAddressEntries.next();
			if (lostMessages.contains(next)) {
				registeredAddresses.get(next).addToQueue(lostMessages.get(next));
			}
		}
	}

	public MetaAgent getScope(String name) {
		if (name.equals(this.toString())) {
			return this.getScope();
		} else {
			return registeredAddresses.get(name).getScope(name);
		}
	}

	//Updates the address book by first checking if there are any changes (returns if this is the case)
	//This adds all entries of the passed in address bokk to current address book then updtes both children and parent
	private void updateAddressBook(Message message) {

		HashMap<String, MetaAgent> newAddressesThatNeedAdding = getAllAddressesThatNeedUpdating((HashMap< String, MetaAgent>) message.retrieveMessageItem(), registeredAddresses);

		if (newAddressesThatNeedAdding == null) {
			return;
		}
		registeredAddresses.putAll(newAddressesThatNeedAdding);
		updateChildrenWithAddressBook();
		updateParentWithAddressBook();
		checkForAnyLostMail(newAddressesThatNeedAdding);

	}

	//Extracts the message details and handles as appropriate
	private void extractMessageDetailsAndHandle(Message message) {

		switch (message.getMessageType()) {

			case PASS_MESSAGE:
				String theMessage = (String) message.retrieveMessageItem();
				System.out.println("I am a message to the portal " + theMessage);
				break;
			case ADD_NODE:
				addNode(message.getSender(), (MetaAgent) message.retrieveMessageItem());
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
			case ADD_NODE_MONITOR:
				addMonitor((NodeMonitor) message.retrieveMessageItem());
				break;

		}

	}

	//Looks up the message address and passes on the message if its held in registered addresses
	//otherwise an error is send back and the message added to lost messages
	private void lookUpAndPassOn(Message message) {

		if (registeredAddresses.containsKey(message.getRecipient())) {
			registeredAddresses.get(message.getRecipient()).addToQueue(message);
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
