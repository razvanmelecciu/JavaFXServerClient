package message;

import java.io.Serializable;

/**
 * A base class for a message object used for client-server communication
 *
 * @author Razvan
 */
public abstract class MessageBase implements Serializable
{

    /**
     * Basic default message constructor (needed for deserialization)
     */
    protected MessageBase()
    {
        crtMsgType = MessageType.CHAT_MSG_EVENT;
    }

    /**
     * Basic message constructor
     *
     * @param msgType The type of the message (message/active users/leave chat)
     */
    protected MessageBase(MessageType msgType)
    {
        crtMsgType = msgType;
    }

    // - Mutators
    /**
     * Sets the current message type to the specified category
     *
     * @param typeEvent Sets the type of the message (message/active users/leave
     *                  chat)
     */
    public void setMessageType(MessageType typeEvent)
    {
        crtMsgType = typeEvent;
    }

    // - Accessors
    /**
     * Get the current message type
     *
     * @return The message type (message/active users/leave chat)
     */
    public MessageType getMessageType()
    {
        return crtMsgType;
    }

    public abstract MessageBase cloneCrtObject();

    public abstract MessageBase printObjectToStdOut();

    // - Members
    private static final long serialVersionUID = 6508817850638279462L;
    protected MessageType crtMsgType = MessageType.CHAT_MSG_EVENT;
    protected static final String DBG_MSG_TYPE = "<Type>:";
}
