package client;

/**
A simple abstract class that flushes the specified string into the outputObject created in flush string
 @author Razvan
 */
public abstract class MessagePrinter
{

    /**
     * Creates a message printer using the provided string
     * @param msg The string to be flushed.
     */
    public MessagePrinter(String msg)
    {
        crtString = msg;
    }
    
    // - Mutators
    
    public void setString(String msg)
    {
        crtString = msg;
    }
    
    
    // - Accessors
    
    /**
    Flushes the contained string into an object.
    */
    public abstract void flushString();
    
    // - Members
    
    protected String crtString;
}
