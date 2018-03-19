package server;

/**
The string table used for the server class messages.
@author Razvan
*/
enum SrvStrTab
{
    /**
     The only compile time instances available.
     */
    SERVER_STARTED(0),
    HOST_ADDRESS(1),
    WAITING_CLIENTS(2),
    USER_ID_CONNECTED(3),
    ERROR_STOP_SRV(4),
    ERROR_START_SRV(5),
    MAX_USR_REACHED(6),
    LIST_USERS(7),
    ERROR_CLOSE_RESOURCES(8),
    MESSAGE_DELIVERY_ERR(9),
    IMPROPER_CMD_LINE(10),
    USER_ID_DISCONNECTED(11),
    USER_ID_LEFT(12),
    USER_ID_JOINED(13);
    
    private SrvStrTab(int idx)
    {
        index = idx;
    }
    
    /**
    Extract the string for the specified enum value
    @return The associated enum string.
    */
    @Override
    public String toString()
    {
        switch (index)
        {
        case 0 :
            return "Server started on port: ";
        case 1 :
            return "Host address: ";
        case 2 :
            return "Waiting for clients connections...";
        case 3 :
            return "User < %s > ID < %d > has been connected.";
        case 4 :
            return "Error encountered while stopping server.";
        case 5 :
            return "Error encountered while starting server.";
        case 6 :
            return "Maximum number of users reached.";
        case 7 :
            return "List of connected users: ";
        case 8 :
            return "Errors encountered while closing streams/sockets";
        case 9 :
            return "Message could not be sent to the client ";
        case 10 :
            return "Improper command line. Usage is Java Server -portnb -max_connections";
        case 11 :
            return "User < %s > ID < %d > has been disconnected.";
        case 12 :
            return "%s (ID %d) has left the conversation.";
        case 13 :
            return "%s (ID %d) has joined the conversation.";
        }
        
        return "";
    }
    
    int index;
}