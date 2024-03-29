package message;

import java.io.Serializable;

/**
 * A base class for a message object used for client-server communication
 */
public abstract class MsgBase implements Serializable
{

    private static final long serialVersionUID = 6722557350437622907L;

    /**
     * Basic default message constructor (needed for deserialization)
     */
    MsgBase()
    {
    }

    /**
     * Get the sender ID for the current message
     *
     * @return the sender ID string
     */
    public String getSenderID()
    {
        return senderID;
    }

    /**
     * Get the current message type
     *
     * @return The message type (connect/message/active users/disconnect)
     */
    public MessageType getMessageType()
    {
        return msgType;
    }

    /**
     * Creates a clone of the current object
     *
     * @return a new object
     */
    public abstract MsgBase cloneCrtObject();

    /**
     * Print the object to std out
     *
     * @return a reference to the current object
     */
    public MsgBase printObjectToStdOut()
    {
        String debugString = DBG_MSG_TYPE + getMessageType() + "\n";
        debugString += SENDER_MSG_TYPE + getSenderID() + "\n";
        System.out.print(debugString);
        System.out.println("\n");
        return this;
    }

    /**
     * Sets the sender ID for the current message
     *
     * @param sender the sender of the message
     */
    public void setSenderID(String sender)
    {
        senderID = sender;
    }

    // - Members


    private String senderID = "";
    MessageType msgType = MessageType.MESSAGE;
    static final String DBG_MSG_TYPE = "<Type>:";
    static final String SENDER_MSG_TYPE = "<Sender>:";
}
