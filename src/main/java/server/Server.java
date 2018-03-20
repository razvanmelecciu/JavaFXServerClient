package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

import static java.net.InetAddress.getLocalHost;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import message.MessageFactory;
import message.MessageType;
import message.MsgBase;

/**
 * A server class that uses sockets to connect with multiple clients.
 *
 */
public class Server
{

    /**
     * Construct a server on the specified port with the maximum nb of connections specified.
     *
     * @param portNumber     The port number
     * @param maxConnections The maximum number of connections
     */
    public Server(int portNumber, int maxConnections)
    {
        this.portNumber = portNumber;
        continueRunning = false;
        maxNbConnections = maxConnections;
        listClConnections = new ArrayList();
        socketSrv = null;
    }

    /**
     * Start listening for incoming connections on the initially specified port.
     */
    public void start()
    {
        continueRunning = true;

        MsgBase messageMsgDisconn = MessageFactory.createMessage(MessageType.MESSAGE, "Server", SrvStrTab.MAX_USR_REACHED.toString());

        try
        {
            socketSrv = new ServerSocket(portNumber, 0, InetAddress.getByName(null));
            printMessageStdOut(SrvStrTab.SERVER_STARTED.toString() + portNumber + " " + SrvStrTab.HOST_ADDRESS.toString() + getLocalHost());
            printMessageStdOut(SrvStrTab.WAITING_CLIENTS.toString());

            while (continueRunning)
            {
                Socket socketCl = socketSrv.accept();

                if (listClConnections.size() == 0)
                    printMessageStdOut("--------------------------------");

                ClientConnection crtConnection = new ClientConnection(socketCl, this);

                listClConnections.add(crtConnection);

                if (getNbConnectedUsers() > maxNbConnections)                // no more slots available, inform the user and then close the connection
                {
                    printMessageStdOut(SrvStrTab.MAX_USR_REACHED.toString());
                    crtConnection.writeMsg(messageMsgDisconn);
                    removeClient(crtConnection.clientID);
                }
                else
                    crtConnection.start();
            }
        }
        catch (Exception e)
        {
            printMessageStdOut(SrvStrTab.ERROR_START_SRV.toString());
        }
    }

    /**
     * Stop the server from receiving any incoming connections and close all the existing connections and streams.
     */
    public void stop()
    {
        continueRunning = false;

        try
        {
            socketSrv.close();
            Iterator<ClientConnection> iteratorCol = listClConnections.iterator();

            while (iteratorCol.hasNext())
            {
                ClientConnection crtConn = iteratorCol.next();

                crtConn.stopRun();
            }
        }
        catch (IOException e)
        {
            printMessageStdOut(SrvStrTab.ERROR_STOP_SRV.toString());
        }
    }

    /**
     * Remove the client with the specified unique ID
     *
     * @param clientID The client ID.
     */
    synchronized void removeClient(int clientID)
    {
        ListIterator<ClientConnection> itCrt = listClConnections.listIterator();

        while (itCrt.hasNext())
        {
            ClientConnection crtConn = itCrt.next();
            if (crtConn.clientID == clientID)
            {
                crtConn.stopRun();
                itCrt.remove();
            }
        }
    }


    /**
     * Broadcast the specific message to all the clients.
     *
     * @param wireMessage The message sent to all the clients connected.
     */
    synchronized void broadcastClients(MsgBase wireMessage, int clientIDExcluded)
    {
        ListIterator<ClientConnection> itCrt = listClConnections.listIterator();

        while (itCrt.hasNext())
        {
            ClientConnection crtConn = itCrt.next();
            if (crtConn.clientID != clientIDExcluded || !consoleMode)
                crtConn.writeMsg(wireMessage);
        }
    }

    /**
     * Get the number of connected users (active threads).
     *
     * @return
     */
    int getNbConnectedUsers()
    {
        return listClConnections.size();
    }

    /**
     * Print the specified message to the standard output.
     *
     * @param eventString The string to be printed out.
     */
    static void printMessageStdOut(String eventString)
    {
        System.out.println(eventString);
    }

    public static void main(String[] args)
    {
        int portNb = 3000;
        int maxConnections = 10;
        int nbArgs = args.length;

        if (nbArgs != 2)
        {
            System.out.println(SrvStrTab.IMPROPER_CMD_LINE.toString());
        }
        else
        {
            String portString = args[0];
            String maxUsrString = args[1];

            if (!portString.matches("-\\d+") || !maxUsrString.matches("-\\d+"))
            {
                System.out.println(SrvStrTab.IMPROPER_CMD_LINE.toString());
            }
            else
            {
                String portNbParse = portString.replaceFirst("-", "");
                String maxConParse = maxUsrString.replaceFirst("-", "");
                portNb = Integer.parseInt(portNbParse);
                maxConnections = Integer.parseInt(maxConParse);
            }

            Server srvInstance = new Server(portNb, maxConnections);
            srvInstance.start();
        }


    }

    private ServerSocket socketSrv;
    List<ClientConnection> listClConnections;
    private boolean continueRunning;
    private final int portNumber;
    private final int maxNbConnections;
    static boolean consoleMode = false;

}

class ClientConnection extends Thread
{
    /*
    Variable used when sending messages to the clients (to avoid flooding messages)
    */
    private static final long sleepMsgOutputTime = 150;

    // - Ctors

    /**
     * Create a connection to the client streams
     *
     * @param clientSocket The socket used to connect the server and the client
     * @param crtInstance  The server class instance
     */
    ClientConnection(Socket clientSocket, Server crtInstance)
    {
        serverInstance = crtInstance;
        this.crtSocket = clientSocket;
        clientID = ++nextAvailableUserID;
        continueLoop = false;

        try
        {
            outputStream = new ObjectOutputStream(crtSocket.getOutputStream());
            inputStream = new ObjectInputStream(crtSocket.getInputStream());

            Object clConn = inputStream.readObject();
            MsgBase baseMsg = (MsgBase) clConn;
            String clientUserName = baseMsg.getSenderID();

            String msgJoinedBody = String.format(SrvStrTab.USER_ID_JOINED.toString(), clientUserName, clientID);
            MsgBase messageMsgJoined = MessageFactory.createMessage(MessageType.MESSAGE, "Server", msgJoinedBody);

            serverInstance.broadcastClients(messageMsgJoined, clientID);
            //Server.printMessageStdOut(String.format(SrvStrTab.USER_ID_CONNECTED.toString(), clientUserName, clientID));
        }
        catch (IOException | ClassNotFoundException ex)
        {
            System.out.println("The user name can't be read properly from the connection.");
        }
    }

    // - Mutators

    /**
     * Run the specified class (called by a thread start()).
     */
    @Override
    public void run()
    {
        continueLoop = true;
        MessageType crtEventType = MessageType.MESSAGE;
        MsgBase baseMsg = null;

        while (continueLoop)
        {
            try
            {
                Object clConn = inputStream.readObject();
                baseMsg = (MsgBase) clConn;
                crtEventType = baseMsg.getMessageType();
                clientUserName = baseMsg.getSenderID();
            }
            catch (IOException | ClassNotFoundException ex)
            {
                System.out.println("The message cannot be read properly from the stream.");
                continueLoop = false;
                serverInstance.removeClient(clientID);
            }

            crtEventType = baseMsg.getMessageType();

            switch (crtEventType)
            {
                case MESSAGE :
                {
                    message.MessageMsg msgObj = (message.MessageMsg) baseMsg;
                    String msgJoinedBody = msgObj.getMessageBody();

                    MsgBase messageMsgJoined = MessageFactory.createMessage(MessageType.MESSAGE, clientUserName, msgJoinedBody);


                    serverInstance.broadcastClients(messageMsgJoined, clientID);
                }
                break;
                case DISCONNECT :
                {
                    continueLoop = false;

                    String msgBody = String.format(SrvStrTab.USER_ID_LEFT.toString(), clientUserName, clientID);
                    MsgBase messageMsgDisconnected = MessageFactory.createMessage(MessageType.MESSAGE, clientUserName, msgBody);

                    serverInstance.broadcastClients(messageMsgDisconnected, clientID);
                    //Server.printMessageStdOut(String.format(SrvStrTab.USER_ID_DISCONNECTED.toString(), clientUserName, clientID));
                }
                break;
                case LIST_ACTIVE_USERS :
                {
                    String msgBody = SrvStrTab.LIST_USERS.toString() + "\n";
                    msgBody += "--------------------\n";

                    ListIterator<ClientConnection> itCrt = serverInstance.listClConnections.listIterator();
                    int i = 0;
                    while (itCrt.hasNext())
                    {
                        ClientConnection crtConnUser = itCrt.next();
                        msgBody += i + "] " + crtConnUser.clientUserName + "\n";
                        ++i;
                    }
                    msgBody += "--------------------\n";

                    MsgBase messageMsgList = MessageFactory.createMessage(MessageType.MESSAGE, "Server", msgBody);
                    writeMsg(messageMsgList);
                }
                break;
            }
        }

        closeStreams();
        closeSocket();
        serverInstance.removeClient(clientID);
    }

    /**
     * Stops the current connection from running.
     */
    void stopRun()
    {
        closeStreams();
        closeSocket();
        continueLoop = false;
    }

    /**
     * Close the input and output streams.
     */
    void closeStreams()
    {
        try
        {
            inputStream.close();
            outputStream.close();
        }
        catch (IOException ex)
        {
            Server.printMessageStdOut(SrvStrTab.ERROR_CLOSE_RESOURCES.toString());
        }

    }

    /**
     * Close the current socket between the server and the client streams.
     */
    void closeSocket()
    {
        try
        {
            crtSocket.close();
        }
        catch (IOException ex)
        {
            Server.printMessageStdOut(SrvStrTab.ERROR_CLOSE_RESOURCES.toString());
        }
    }

    /**
     * Write a message to the associated output stream.
     *
     * @param wireMsg The message to send.
     * @return True or false depending if the message was sent successfully.
     */
    boolean writeMsg(MsgBase wireMsg)
    {
        try                                    // sleep required to let the client print out the messages
        {
            sleep(sleepMsgOutputTime);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (crtSocket.isClosed())
        {
            Server.printMessageStdOut(SrvStrTab.MESSAGE_DELIVERY_ERR.toString() + clientUserName + " [" + clientID + "]");
            return false;
        }

        try
        {
            outputStream.writeObject(wireMsg);
        }
        catch (IOException ex)
        {
            Server.printMessageStdOut(SrvStrTab.MESSAGE_DELIVERY_ERR.toString() + clientUserName + " [" + clientID + "]");
        }

        return true;
    }

    // - Members

    Server serverInstance;
    Socket crtSocket;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    volatile boolean continueLoop;

    int clientID;
    String clientUserName;
    MsgBase crtMessage;

    private static int nextAvailableUserID = 0;

}