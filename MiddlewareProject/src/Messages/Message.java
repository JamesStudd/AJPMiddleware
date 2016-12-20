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
    private MetaAgent sender;
    /**
     *Stores the previous portal that forwarded the message
     */

    private MetaAgent recipient;
    /**
     *Stores the date the message was sent
     */
    private Calendar date = new GregorianCalendar();

    //The type of message the object is
    MessageType typeOfMessage;

    private MetaAgent messageScope;
    
    public Message(MessageType type, MetaAgent sender, MetaAgent recipient, T messageObject, MetaAgent scope){
        this.sender = sender;
        this.recipient = recipient;
	this.typeOfMessage = type;
	this.messageObject = messageObject;
	this.type = type;
	this.messageScope = scope;
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

    public MetaAgent getScope(){
	    return messageScope;
    }

    public MessageType getMessageType(){
	    return type;
    }

    public MetaAgent getSender() {
        return sender;
    }


    public MetaAgent getRecipient() {
        return recipient;
    }

    public Calendar getDate() {
        return date;
    }  
}