package Messages;
/**
 *
 * @author Matthew
 */
import Agents.MetaAgent;
import Messages.MessageType;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class Message <T>{


    //The type of object that is being passed out
    private T messageObject;

    private MessageType type = MessageType.ERROR;

    /**
     *Stores the messages ID number
     */
    private int id;
    /**
     *Stores the name of the sender
     */
    private String sender;
    /**
     *Stores the previous portal that forwarded the message
     */

    private String recipient;
    /**
     *Stores the date the message was sent
     */
    private Calendar date = new GregorianCalendar();

    //The type of message the object is
    MessageType typeOfMessage;

    
    public Message(MessageType type, String sender, String recipient, T messageObject){
        this.sender = sender;
        this.recipient = recipient;
	this.typeOfMessage = type;
	this.messageObject = messageObject;
	this.type = type;
        date = Calendar.getInstance();
        
        /**
        *Temp generation for the ID
        */
        Random random = new Random();
        id = random.nextInt(100000);
    }
    
    /**
    *Returns the date as a string
    * @return 
    */
    public String dateString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        return dateFormat.format(date.getTime());
    }


    //Gets the message item
    public T retrieveMessageItem(){
	    return messageObject;
    }

    public MessageType getMessageType(){
	    return type;
    }

    public String getSender() {
        return sender;
    }


    public String getRecipient() {
        return recipient;
    }

    public Calendar getDate() {
        return date;
    }  
}