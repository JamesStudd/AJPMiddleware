package Messages;

/**
 *
 * This class builds a generic message which takes a type 'T' as its object type
 */
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Message<T> {

	//The type of object that is being passed out
	private T messageObject;

	private MessageType type = MessageType.ERROR;
	//Name of the sender
	private String sender;
	//Name of the recipient
	private String recipient;
	//Date of message creation
	private Calendar date = new GregorianCalendar();
	//The type of message the object is
	MessageType typeOfMessage;

	/**
	 * Constructor for the message First we check that the object passed in
	 * is of the correct type, ie that it matches the type that is
	 * stipulated according to its messag type. If its not we send a message
	 * back to the sender letting them know of the error. Otherwise we
	 * create a message with the given parameters
	 *
	 * @param type - The type of message
	 * @param sender - The person who has sent the message
	 * @param recipient - The person who the message is for
	 * @param messageObject - The contents of the message
	 */
	public Message(MessageType type, String sender, String recipient, T messageObject) {

		if (!type.checkType(messageObject)) {
			try {
				throw new WrongObjectTypeForMessageException();
			} catch (WrongObjectTypeForMessageException ex) {
				this.sender = "Message Error";
				this.recipient = sender;
				this.type = MessageType.WRONG_TYPE_OF_OBJECT_WAS_SENT_WITH_THIS_MESSAGE;
			}
		} else {
			this.sender = sender;
			this.recipient = recipient;
			this.typeOfMessage = type;
			this.type = type;
		}
		this.messageObject = messageObject;
		date = Calendar.getInstance();
	}

	/**
	 * Returns the date that the message was created
	 *
	 * @return - The date the message was created
	 */
	public String getTheDateOfCreation() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
		return dateFormat.format(date.getTime());
	}

	/**
	 * Gets the contents of the message
	 * @return  - The contents of the message
	 */
	public T retrieveMessageItem() {
		return messageObject;
	}

	/**
	 * Gets the type of the message
	 * @return - The message type
	 */
	public MessageType getMessageType() {
		return type;
	}

	/**
	 * Gets the sender of the message
	 * @return  - The sender of the message
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Gets the recipient of the message
	 * @return  - The recipient of the message
	 */
	public String getRecipient() {
		return recipient;
	}

}
