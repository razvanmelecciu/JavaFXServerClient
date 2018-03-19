package message;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 A message object that can be used to communicate with sockets
 @author Razvan
 */
public final class MessageObject extends MessageBase implements Serializable
{
     /**
     Construct a message object (default ctor neede for deserialization)
     */
    public MessageObject()
    {
        super();
        this.crtMsg = "";
    }
    
    /**
     Construct a message object of the specified type
     @param msgType The type of message
     */
    public MessageObject(MessageType msgType)
    {
        super(msgType);
        this.crtMsg = "";
    }
    
    /**
     Construct a message object of the specified type and optionally add a message string
     @param msgType   The type of the message
     @param crtString The message string (only set if the type set is a message)
     */
    public MessageObject(MessageType msgType, String crtString)
    {
        this(msgType);
        this.crtMsg = "";
        if (crtMsgType == MessageType.CHAT_MSG_EVENT)
            crtMsg = crtString;
    }
  
    
    // - Mutators

    /**
     Copy the contents from the specified object
     @param Source The source object
     @return A reference to the current object
     */
    public MessageObject copyContents(final MessageObject Source)
    {
        crtMsg = Source.crtMsg;
        crtMsgType = Source.crtMsgType;
        return this;
    }
    
    /**
     Set the message string associated with the current object (will be set only if the type is correct)
     @param msg The message string
     @return A reference to the currrent object
     */
    public MessageObject setMessageString(String msg)
    {
        if (crtMsgType == MessageType.CHAT_MSG_EVENT)
            crtMsg = msg;
        return this;
    }
    
    /**
     Deserializes data from a previously serialized JSON string
     @param sourceJSON
     @return A reference to the currrent object
     */
    public MessageObject deserializeJSON(JSONObject<MessageObject> sourceJSON)
    {
        MessageObject deserializedObject = sourceJSON.recreateObject();
        copyContents(deserializedObject);
        return this;
    }
   
    // - Accessors
    
    /**
     Returns the message stored inside the current object
     @return The message stored
     */
    public String getMessage()
    {
        return crtMsg;
    }
    
    /**
     Creates a copy of the current object
     @return A copy of the current object
     */
    @Override
    public MessageObject cloneCrtObject()
    {
        return new MessageObject(crtMsgType, crtMsg);
    }

    /**
     Prints the details of the current object to std out
     @return A reference to the current object
     */
    @Override
    public MessageObject printObjectToStdOut()
    {
        String debugString = DBG_MSG_TYPE + crtMsgType + "\n";
        debugString += debugMsgString + crtMsg;
        System.out.print(debugString);
        System.out.println("\n");
        return this;
    }
    
    /**
     Serialize the current object into a JSON message
     @return a JSON representation of the current message
     */
    public JSONObject<MessageObject> serializeJSON()
    {
        return new JSONObject<>(this);
    }  
    
    // - Generated UID for class identification & versioning
    
    private static final long serialVersionUID = 8178899935112092903L;

    private String crtMsg;   
    private static final String debugMsgString = "<Message>:";
    
    /**
     * A simple main method for debugging purposes
     * @param args No params need to be specified
     */
    public static void main(String[] args)
    {
        MessageObject crtMes = new MessageObject(MessageType.CHAT_MSG_EVENT, "This a test message from the original");
        crtMes.printObjectToStdOut();
        MessageObject crtMesClone = crtMes.cloneCrtObject();
        crtMesClone.setMessageString("This is a new modified test message");
        crtMesClone.printObjectToStdOut();
        
        JSONObject<MessageObject> messageJSON = crtMesClone.serializeJSON();
        messageJSON.printToStdOut();
        
        try
        {
            messageJSON.writeToFile("testmsg");
        }
        catch (IOException ex)
        {
            System.out.println("");
            System.out.println("Exception encountered, file invalid");
        }
        
        try
        {
            messageJSON.readFromFile("testmsg");
        }
        catch (IOException e)
        {
            System.out.println("");
            System.out.println("Exception encountered, file invalid");
        }
        messageJSON.printToStdOut();
        
        crtMes.deserializeJSON(messageJSON);
        crtMes.printObjectToStdOut();
        
            
        try
        {
            crtMes.setMessageType(MessageType.LEAVE_CHAT);
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("sdasdas"));
            outputStream.writeObject(crtMes);
            
            //MessageObject copy2 = new MessageObject(MessageType.CHAT_MSG_EVENT);
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("sdasdas"));
            
            MessageObject copy2 = (MessageObject) inputStream.readObject();
        }
        catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(MessageObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
