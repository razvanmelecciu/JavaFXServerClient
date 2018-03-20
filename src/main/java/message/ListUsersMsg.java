package message;

import java.io.Serializable;

/**
 * A particular class for identifying a list active users request
 */
public final class ListUsersMsg extends MsgBase implements Serializable
{

    private static final long serialVersionUID = -3511092311585919267L;

    /**
     * Construct a list active users request object (default ctor needed for deserialization)
     */
    protected ListUsersMsg()
    {
        msgType = MessageType.LIST_ACTIVE_USERS;
        setSenderID("");
    }

    /**
     * Construct from an existing object
     *
     * @param sourceObject
     */
    protected ListUsersMsg(final ListUsersMsg sourceObject)
    {
        setSenderID(sourceObject.getSenderID());
    }

    /**
     * Creates a copy of the current object
     *
     * @return a new object
     */
    @Override
    public ListUsersMsg cloneCrtObject()
    {
        return new ListUsersMsg(this);
    }

    /**
     * Copy the contents from the specified object
     *
     * @param Source The source object
     * @return A reference to the current object
     */
    public ListUsersMsg copyContents(final ListUsersMsg sourceObject)
    {
        setSenderID(sourceObject.getSenderID());
        return this;
    }
}