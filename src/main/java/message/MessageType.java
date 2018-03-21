package message;

/**
 * A simple enum class for identifying the type of an event sent between a client and a server
 */
public enum MessageType
{

    /**
     * The only compile time instances available.
     */
    CONNECT(0),
    MESSAGE(1),
    LIST_ACTIVE_USERS(2),
    DISCONNECT(3);

    /**
     * Build this type from an integer
     * @param index the message index
     */
    MessageType(int index)
    {
        this.index = index;
    }

    /**
     * Returns a string which characterizes the current enum value
     * @return The string for the current value
     */
    @Override
    public String toString()
    {
        switch (index)
        {
            case 0:
                return "Connect";
            case 1:
            default:
                return "Message";
            case 2:
                return "Active_Users";
            case 3:
                return "Disconnect";
        }
    }

    /**
     * Returns the index of the value.
     * @return An index.
     */
    public int getIdx()
    {
        return index;
    }

    // - Members

    final int index;
}
