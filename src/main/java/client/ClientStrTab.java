package client;

/**
The string table used for the client class messages.
@author Razvan
*/
public enum ClientStrTab
{
    /**
     The only compile time instances available.
     *//**
     The only compile time instances available.
     */
    SRV_CONN_SUCCESFULL(0),
    ERR_CONN_SERVER(1),
    STREAM_COMM_ERR(2),
    ERR_CLOSING_RES(3),
    DISCONNECTED(4),
    ERR_RECEIVING_SRV_EV(5),
    IMPROPER_CMD_LINE(6),
    SRV_CONN_CLOSED(7);
    
    private ClientStrTab(int idx)
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
            return "Successfully connected to the server located at address: %s / Port: %d";
        case 1 :
            return "Cannot connect to the requested server";
        case 2 :
            return "Stream communication error.";
        case 3 :
            return "Error closing resources.";
        case 4 :
            return "Disconnected from the server";
        case 5 :
            return "Error receiving server event";
        case 6 :
            return "Improper command line. Usage is Java Client -ServerAddress -portnb -username";
        case 7 :
            return "Connection closed from server.";
        }
        
        return "";
    }
    
    int index;
}