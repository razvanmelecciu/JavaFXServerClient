package message;

import java.io.Serializable;

/**
 * A particular class for identifying connect requests
 */
public final class ConnectMsg extends MsgBase implements Serializable
{

    private static final long serialVersionUID = -6781059741630241006L;

    /**
     * Construct a connect request object (default ctor needed for deserialization)
     */
    ConnectMsg()
    {
        msgType = MessageType.CONNECT;
        setSenderID("");
    }

    /**
     * Construct from an existing object
     *
     * @param sourceObject the RHS object
     */
    private ConnectMsg(final ConnectMsg sourceObject)
    {
        setSenderID(sourceObject.getSenderID());
    }

    /**
     * Creates a copy of the current object
     *
     * @return a new object
     */
    @Override
    public ConnectMsg cloneCrtObject()
    {
        return new ConnectMsg(this);
    }

    /**
     * Copy the contents from the specified object
     *
     * @param sourceObject The source object
     * @return A reference to the current object
     */
    public ConnectMsg copyContents(final ConnectMsg sourceObject)
    {
        setSenderID(sourceObject.getSenderID());
        return this;
    }
}