package message;

import java.io.Serializable;

/**
 * A particular class for identifying disconnect requests
 */
public final class DisconnectMsg extends MsgBase implements Serializable
{

    private static final long serialVersionUID = 5135921117247296905L;

    /**
     * Construct a disconnect request object (default ctor needed for deserialization)
     */
    DisconnectMsg()
    {
        msgType = MessageType.DISCONNECT;
        setSenderID("");
    }

    /**
     * Construct from an existing object
     *
     * @param sourceObject the RHS object
     */
    private DisconnectMsg(final DisconnectMsg sourceObject)
    {
        setSenderID(sourceObject.getSenderID());
    }

    /**
     * Creates a copy of the current object
     *
     * @return a new object
     */
    @Override
    public DisconnectMsg cloneCrtObject()
    {
        return new DisconnectMsg(this);
    }

    /**
     * Copy the contents from the specified object
     *
     * @param sourceObject The source object
     * @return A reference to the current object
     */
    public DisconnectMsg copyContents(final DisconnectMsg sourceObject)
    {
        setSenderID(sourceObject.getSenderID());
        return this;
    }
}
