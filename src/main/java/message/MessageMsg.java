package message;

import java.io.Serializable;

/**
 * A particular message type used for the actual conversations
 */
public final class MessageMsg extends MsgBase implements Serializable
{

    private static final long serialVersionUID = 2912898942548948694L;

    /**
     * Construct a message object (default ctor needed for deserialization)
     */
    protected MessageMsg()
    {
        msgType = MessageType.MESSAGE;
        setSenderID("");
        setBody("");
    }

    /**
     * Construct a message object using the specified body
     */
    MessageMsg(String body)
    {
        msgType = MessageType.MESSAGE;
        setSenderID("");
        setBody(body);
    }

    /**
     * Construct from an existing object
     *
     * @param sourceObject the RHS object
     */
    private MessageMsg(final MessageMsg sourceObject)
    {
        setSenderID(sourceObject.getSenderID());
        setBody(sourceObject.getMessageBody());
    }

    /**
     * Get the current message body
     *
     * @return the message body
     */
    public String getMessageBody()
    {
        return messageBody;
    }

    /**
     * Creates a copy of the current object
     *
     * @return a new object
     */
    @Override
    public MessageMsg cloneCrtObject()
    {
        return new MessageMsg(this);
    }

    /**
     * Prints the details of the current object to std out
     *
     * @return A reference to the current object
     */
    @Override
    public MsgBase printObjectToStdOut()
    {
        String debugString = DBG_MSG_TYPE + getMessageType() + "\n";
        debugString += SENDER_MSG_TYPE + getSenderID() + "\n";
        debugString += debugMsgString + getMessageBody();
        System.out.print(debugString);
        System.out.println("\n");
        return this;
    }

    /**
     * Copy the contents from the specified object
     *
     * @param sourceObject The source object
     * @return A reference to the current object
     */
    public MessageMsg copyContents(final MessageMsg sourceObject)
    {
        setSenderID(sourceObject.getSenderID());
        setBody(sourceObject.getMessageBody());
        return this;
    }

    /**
     * Sets the message body
     *
     * @param bodyValue the contents of the message
     */
    private void setBody(String bodyValue)
    {
        messageBody = bodyValue;
    }

    private String messageBody = "";
    private static final String debugMsgString = "<Message>:";
}
