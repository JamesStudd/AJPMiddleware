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
 * This class contains all the functionality of the middleware Portals
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

	/**
	 * Constructor , sets the name and parent
	 *
	 * @param name - The name of the portal
	 */
	public Portal(String name) {
		super(name);
		setParent(null);
	}

	/**
	 * Constructor , sets the name and the parent
	 *
	 * @param name - The name of the portal
	 * @param parent - The name of the parent to this portal
	 */
	public Portal(String name, MetaAgent parent) {
		super(name);
		setParent(parent);

	}

	/**
	 * Sets the parent pointer of this portal. Once added it will add the
	 * parent to the address book and request that the parent add this
	 * portal to its address book. If this portal has children than full
	 * update updates are required so amended addressbook is sent to the
	 * parent, which will come back with their updates and then forwarded to
	 * this portals children
	 *
	 * @param parent - The parent to this portal
	 */
	public void setParent(MetaAgent parent) {
		this.parent = parent;
		if (parent != null) {
			registeredAddresses.put(parent.toString(), parent);
			parent.addToQueue(new Message(MessageType.ADD_NODE, this.toString(), this.parent.toString(), this));
			if (!children.isEmpty()) {
				parent.addToQueue(new Message(
								MessageType.UPDATE_ADDRESSES, 
								this.toString(), 
								parent.toString(), 
								prepareAddressBookForTransfer(true)));
			}
		}
	}

	/**
	 * This adds a monitor to the list of monitors watching this portal
	 *
	 * @param mon
	 */
	private void addMonitor(NodeMonitor mon) {
		monitors.add(mon);
	}

	/**
	 * This removes a monitor from the list of monitors watching the portal
	 *
	 * @param mon
	 */
	private void removeMontior(MetaAgent mon) {
		monitors.remove(mon);
	}
                
        /**
	 * Returns the lost messages, only used for testing.
	 *
	 */
        public BoundedHashMap<String, Message> getLostMessages() {
            return lostMessages;
        }
        
 
	/**
	 * This updates all the registered monitors with the message given to
	 * this portal
	 *
	 * @param message - The message recieved
	 */
	private void updateMonitors(Message message) {
		Iterator<NodeMonitor> it = monitors.iterator();
		while (it.hasNext()) {
			it.next().addToQueue(message);
		};
	}

	/**
	 * Checks if the passed string is the same as this portal
	 *
	 * @param x
	 * @return
	 */
	private boolean isForMe(String x) {
		return x == this.toString();
	}

	/**
	 * Removes an entry from the passed map (this is so that it can be used
	 * in a lambda )
	 *
	 * @param map - The map that the entry needs removing from
	 * @param entry - The entry that needs removing
	 * @return The map with the entry removed
	 */
	private Map<String, MetaAgent> removeItem(Map<String, MetaAgent> map, MetaAgent entry) {
		map.remove(entry.toString());
		return map;
	}

	/**
	 * This method is used for testing - gets the address book of this
	 * portal
	 *
	 * @return - The address book of this portal
	 */
	public ConcurrentHashMap<String, MetaAgent> getRegisteredAddresses() {
		return registeredAddresses;
	}

	/**
	 * When the address book is sent to other nodes in the system it needs
	 * amending such that addresses that should come through this portal
	 * need to be changed to be pointing to this portal rather than them.
	 * Also all nodes that are scoped at this node should not be passed up
	 * the parent. This method captures all of that functionality and
	 * creates a new map with the updated pointers
	 *
	 * @param isForParent - If this preparation is for a parent node
	 * @return A map with the address updated as appropriate from this nodes
	 * registered addresses
	 */
	private Map<String, MetaAgent> prepareAddressBookForTransfer(boolean isForParent) {

		Map<String, MetaAgent> addressBookToBeReturned = new HashMap<>();
		Iterator<String> addresses = registeredAddresses.keySet().iterator();
		while (addresses.hasNext()) {
			String nextAddressKey = addresses.next();
			MetaAgent nextAddressValue = registeredAddresses.get(nextAddressKey);
			if (isForParent && nextAddressValue.getScope(nextAddressKey) == this) {
				continue;
			}
			if (isForParent) {
				if (children.contains(nextAddressValue)) {
					addressBookToBeReturned.put(nextAddressKey, this);
				} else {
					addressBookToBeReturned.put(nextAddressKey, nextAddressValue);
				}
			} else {
				addressBookToBeReturned.put(nextAddressKey, this);
			}
		}

		return addressBookToBeReturned;
	}

	/**
	 * This method runs through all this Portals children and updates them
	 * with a amended addressbook if they are also of type portal (since
	 * user agents don't require address books).It does this by requesting
	 * they update their address book via placing a message on their
	 * blocking queue
	 */
	private void updateChildrenWithAddressBook() {
		children.forEach(a -> {
			if (a.getClass() == this.getClass()) {
				a.addToQueue(new Message<>(MessageType.UPDATE_ADDRESSES, this.toString(), a.toString(),
					removeItem(prepareAddressBookForTransfer(false), a)));
			}
		});
	}

	/**
	 * Updates the parent node with an amended address book with the nodes
	 * that are scoped to this portal are removed and this nodes decendents
	 * addresses to be this portal so that future messages come through
	 * there
	 */
	private void updateParentWithAddressBook() {
		if (parent == null) {
			return;
		}
		parent.addToQueue(new Message<Map<String, MetaAgent>>(MessageType.UPDATE_ADDRESSES, 
								      this.toString(), 
								      parent.toString(),
									prepareAddressBookForTransfer(true)));
	}

	/**
	 * This method updates all this portals children with the new node. It
	 * does this via their blocking queue and by asking that any messages to
	 * that node come through this node.
	 *
	 * @param node - The node that needs to be added
	 * @param address - The address that this portal has for that node
	 */
	private void requestChildrenToAddNode(String node, MetaAgent address) {
		children.forEach((a) -> {
			//Checks that we are not sending the request back to a node that sent it to us
			if (!a.equals(address) && a.getClass() == this.getClass()) {
				a.addToQueue(new Message(MessageType.ADD_NODE, node, a.toString(), this));
			}
		});
	}

	/**
	 * Adds a node to its address book.If the node string passed in is the
	 * same as the name of address passed in, then the node is added to this
	 * portals children This method then passes a request to its children
	 * and parent to add the node depending on scope. Finally, the lost
	 * property is checked to see if the new node has any lost mail.
	 *
	 * @param node - The string name of the node whos address is to be added
	 * @param address - The node pointer which is to be sent any messages to
	 * the new node
	 */
	private void addNode(String node, MetaAgent address) {

		registeredAddresses.put(node, address);
		if (node == address.toString()) {
			children.add(address);
			if(address.getClass() == this.getClass()){
				updateChildrenWithAddressBook();
			}
		}
		requestChildrenToAddNode(node, address);
		if (parent != null && address != (parent) && address.getScope(node) != this) {
			parent.addToQueue(new Message(MessageType.ADD_NODE, node, parent.toString(), this));
		}

		if (lostMessages.contains(node)) {
			registeredAddresses.get(node).addToQueue(lostMessages.get(node));

		}

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

	/**
	 * This method takes in an address book from another agent and runs
	 * through it to see if there are any entries that are either not in
	 * this portals address book or that any entry in this portals address
	 * book needs changing.
	 *
	 * @param addressBookPassedIn - The address book that has been passed in
	 * and needs checking against
	 * @return Either null if no nodes need adding to this portals address
	 * book or a map of all node that do need adding
	 */
	private HashMap<String, MetaAgent> getAllAddressesThatNeedUpdating(HashMap<String, MetaAgent> addressBookPassedIn) {
		Iterator<String> keyIteratorForAddressBookPassedIn = addressBookPassedIn.keySet().iterator();
		ArrayList<String> toBeRemoved = new ArrayList();
		MetaAgent agentOfPassedInAddressBook;
		MetaAgent agentFromThisPortalsAddresses;
		boolean somethingIsDueToChange = false;

		while (keyIteratorForAddressBookPassedIn.hasNext()) {
			String next = keyIteratorForAddressBookPassedIn.next();
			if (registeredAddresses.containsKey(next)) {

				agentOfPassedInAddressBook = addressBookPassedIn.get(next);
				agentFromThisPortalsAddresses = registeredAddresses.get(next);

				if (next.equals(this.toString())) {
					toBeRemoved.add(next);
				}
				//We don't need any entries that relate to this portal or this portals children
				if (agentOfPassedInAddressBook == this || children.contains(registeredAddresses.get(next))) {
					toBeRemoved.add(next);
				} else if (agentOfPassedInAddressBook != agentFromThisPortalsAddresses) {
					somethingIsDueToChange = true;
				}
			} else {
				//This is because the passed in map has an entry that this portals address book doesn't have
				somethingIsDueToChange = true;
			}
		}
		//Remove from the passed address book anything that doesn't need updating in this portal
		toBeRemoved.forEach(a -> addressBookPassedIn.remove(a));
		if (!somethingIsDueToChange) {
			return null;
		}
		return addressBookPassedIn;
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

	/**
	 * This method retrieves the scope from the passed in node name. If the node is 
	 * this node it will return its scope, otherwise it will return the scope of the 
	 * agent held in linked to that node in this portals address book
	 * @param name - The name of the node whos scope we are retrieving
	 * @return  The agents scope
	 */
	@Override
	public MetaAgent getScope(String name) {
		if (name.equals(this.toString())) {
			return this.getScope();
		} else {
			return registeredAddresses.get(name).getScope(name);
		}
	}

	/**
	 * This method updates this portals address book with the given address book
	 * passed in within the message. It checks if there is anything to add and adds it if needs be. 
	 * This method will then update both its children and its parent with the new address book 
	 * then check for any lost mail that may belong to any new additions
	 * @param message  - The message which contains a map as its object
	 */
	private void updateAddressBook(Message message) {

		HashMap<String, MetaAgent> newAddressesThatNeedAdding = getAllAddressesThatNeedUpdating(
													(HashMap< String, MetaAgent>)
													message.retrieveMessageItem());

		if (newAddressesThatNeedAdding == null) {
			return;
		}
		registeredAddresses.putAll(newAddressesThatNeedAdding);
		updateChildrenWithAddressBook();
		updateParentWithAddressBook();
		checkForAnyLostMail(newAddressesThatNeedAdding);

	}

	/**
	 * This method extracts the details from the message and passes it to the 
	 * relevant message type dependant
	 * @param message 
	 */
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
				System.out.println("This should not come through to me as I will not have sent any messages");
				break;
			case ERROR:
				System.out.println(this.toString() + " has recieved an error: ");
				System.out.println((String) message.retrieveMessageItem());
				break;
			case FAILED_TO_DELIVER:
				System.out.println("This should not come through to me as I will not have sent any messages");
				break;
			case ADD_NODE_MONITOR:
				addMonitor((NodeMonitor) message.retrieveMessageItem());
				break;

		}

	}

	/**
	 * This method deals with the scenario when we have recieved a message 
	 * that is not meant for us that needs passing on. It will first try and 
	 * pass the message on to a known entry in its address book. If the entry 
	 * is not found in the address book it will log it in lost property and
	 * send back a message to the original sender that the message has been
	 * moved to lost property awaiting an update of its forwarding address
	 * 
	 * @param message  - The message that is needing to be passed on
	 */
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
			registeredAddresses.get(message.getSender())
					   .addToQueue(new Message
								(MessageType.ADDRESS_NOT_FOUND_MOVED_TO_LOST_PROPERTY, 
								this.toString(), 
								message.getSender(), 
								message));
			//If we are removing from our lost property due to being full we let the original sender know
			if (removed != null) {
				registeredAddresses.get(removed.getSender())
							.addToQueue(new Message(MessageType.FAILED_TO_DELIVER, 
										this.toString(), 
										message.getSender(), 
										removed));
			}
		}

	}


	/**
	 * This method handles a new message being passed by updating the monitors
	 * If the message is for this portal it will get its details extracted and 
	 * deal with it, otherwise it will attempt to pass it on
	 * @param message 
	 */
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
