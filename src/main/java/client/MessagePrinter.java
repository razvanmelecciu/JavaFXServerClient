package client;

/**
A simple abstract class that flushes the specified string into the outputObject created in flush string
 */
public abstract class MessagePrinter
{

    /**
     * Creates a message printer using the provided string
     * @param msg The string to be flushed.
     */
    protected MessagePrinter(String msg)
    {
        crtString = msg;
    }

    /**
     * Set the string that will further be flushed
     * @param msg the string
     */
    public void setString(String msg)
    {
        crtString = msg;
    }
    
    /**
    Flushes the contained string into an object.
    */
    public abstract void flushString();
    
    protected String crtString;
}
