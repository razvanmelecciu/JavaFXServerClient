package message;

/**
 * A simple factory class which constructs message types
 */
public final class MessageFactory
{
    /**
     * Builds the specified type of message
     * @param messageType the type of the current message
     * @param senderID the sender of the message
     * @param body only considered if the type is MESSAGE
     * @return a valid MsgBase object (can be downcasted or manipulated by this base class)
     */
    public static MsgBase createMessage(MessageType messageType, String senderID, String body)
    {
        MsgBase crtObj = null;

        switch (messageType)
        {
            case CONNECT :
                crtObj = new ConnectMsg();
                break;
            case DISCONNECT :
                crtObj = new DisconnectMsg();
                break;
            case MESSAGE :
                crtObj = new MessageMsg(body);
                break;
            case LIST_ACTIVE_USERS :
                crtObj = new ListUsersMsg();
                break;
        }

        crtObj.setSenderID(senderID);

        return crtObj;
    }
}
