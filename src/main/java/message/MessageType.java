package message;

/**
 A simple enum class for identifying the type of a message sent between a client and a server
 @author Razvan
 */
public enum MessageType 
{ 
    
    /**
     The only compile time instances available.
     */
    CHAT_MSG_EVENT(0), 
    LIST_ACTIVE_USERS(1), 
    LEAVE_CHAT(2);
    
    MessageType(int index)
    {
        this.index = index;
    }
    
    /**
     Returns a string which characterizes the current enum value
     @return The string for the current value
     */
    @Override
    public String toString()
    {
        switch (index)
        {
        case 0 :
        default :
            return "Message";
        case 1 :
            return "Active_Users";
        case 2 :
            return "Leave";
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
    
    int index;
};
