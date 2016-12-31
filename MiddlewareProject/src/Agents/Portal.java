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
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author chris
 */
public class Portal extends MetaAgent {

	//The children of the node
	private Set<MetaAgent> children = new HashSet();

	//The direct parent of the node
	private MetaAgent parent = null;

	//Should be maybe moved to the meta agent, this of course depending on what we do with the nodeMonitor
	private ConcurrentHashMap<String, MetaAgent> registeredAddresses = new ConcurrentHashMap<String, MetaAgent>();

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
		children.forEach(a -> {
				if(a.getClass() == this.getClass())	
				a.addToQueue(new Message<>(MessageType.UPDATE_ADDRESSES, this.toString(), a.toString(), setChildrensAddressToMe(registeredAddresses)));
			});
	}

	//This method sets all agents address that are set to be this nodes children to be set to this node
	//This is for when we pass up the address book a parent so that they come to this node rather than straight to the child
	private Map<String,MetaAgent> setChildrensAddressToMe(Map<String,MetaAgent> in){

		Map<String, MetaAgent> out = new HashMap<>();
		Iterator<String> keys = in.keySet().iterator();

		while(keys.hasNext()){
			String next = keys.next();
			MetaAgent val = in.get(next);
			if(children.contains(val)){
				out.put(next, this);
			}
			else{
				out.put(next, val);
			}
		}

		return out;
	}

	//Updates parent with address book, filters any node addresses whos scope is this node
	private void updateParentWithAddressBook(Map<String,MetaAgent> toBePassedUp) {
		if (parent == null || toBePassedUp == null) {
			return;
		}

		Map<String, MetaAgent> addressBookToBePassed = setChildrensAddressToMe(toBePassedUp);

		parent.addToQueue(new Message<Map<String, MetaAgent>>(MessageType.UPDATE_ADDRESSES, this.toString(), parent.toString(), addressBookToBePassed));
	}

	//Adds a node  to the portals children and updates the address
	private void addNode(MetaAgent node) {

		children.add(node);
		registeredAddresses.put(node.toString(), node);
		updateChildrenWithAddressBook();
		if (!scopedHere(node.getScope())) {
			updateParentWithAddressBook(getAddressesNotScopedHere());
		}
	}

	//True is the scope of the metaAgent is this node
	private boolean scopedHere(MetaAgent scope) {
		return scope == this;
	}

	//Creates an address book where all nodes that are pointing to one of this nodes children now point to this node
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
		
		System.out.println("Showing addresses for " + this.toString());
		Iterator<String> t = registeredAddresses.keySet().iterator();
		while(t.hasNext()){

			String next = t.next();
			System.out.println(next + " : " + registeredAddresses.get(next));

		}

		System.out.println("Showing Children");

		Iterator<MetaAgent> chil = children.iterator();
		while(chil.hasNext()){
			System.out.println(chil.next());
		}
	}

	//This method removes all address that are not to be added because  they point to this node or they are children of this node
	private HashMap<String, MetaAgent> removeAddressesThatPointToMeOrAreMyChildren(HashMap<String, MetaAgent> in , HashMap<String, MetaAgent> current){
		Iterator<String> comingIn = in.keySet().iterator();
		ArrayList<String> toBeRemoved  = new ArrayList();
		MetaAgent inAgent;
		MetaAgent currentAgent;
		while(comingIn.hasNext()){
			String next = comingIn.next();
			if(current.containsKey(next)){
				inAgent = in.get(next);
				currentAgent = current.get(next);

				if(inAgent == this || children.contains(registeredAddresses.get(next))){
					toBeRemoved.add(next);
				}
				else{
					continue;
				}
			}
		}
		toBeRemoved.forEach(a -> in.remove(a));
		return in;
	}

	//Shows all the addresses and values of a passed node
	private void showAddresses(Map<String, MetaAgent> in){
		in.keySet().forEach((a) -> System.out.println(a + " : " + in.get(a).toString()));
	}

	//This method checks to see if anything is to be added to the registered addresses 
	private boolean nothingDueToChange(Map<String, MetaAgent> in){
		Iterator<String> i = in.keySet().iterator();
		boolean change; 
		while(i.hasNext()){
			String next = i.next();
			if(registeredAddresses.containsKey(next)){
				if(!(registeredAddresses.get(next) == in.get(next))){
					return false;
				}
			}
			else return false;
		}
		return true;
	}
	
	//Updates the address book by first checking if there are any changes (returns if this is the case)
	//This adds all entries of the passed in address bokk to current address book then updtes both children and parent
	private void updateAddressBook(Message message) {

		HashMap<String, MetaAgent> passedIn = (HashMap< String, MetaAgent>) message.retrieveMessageItem();
		HashMap<String, MetaAgent> notScoped = (HashMap) getAddressesNotScopedHere();

		HashMap<String, MetaAgent> newAddressesThatNeedAdding = removeAddressesThatPointToMeOrAreMyChildren(passedIn, notScoped);
		
		
		if (nothingDueToChange(newAddressesThatNeedAdding)) {
			return;
		}
		registeredAddresses.putAll(newAddressesThatNeedAdding);
		updateChildrenWithAddressBook();
		updateParentWithAddressBook(getAddressesNotScopedHere());
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
