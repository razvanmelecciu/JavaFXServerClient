package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import message.MessageFactory;
import message.MessageMsg;
import message.MessageType;
import message.MsgBase;

/**
 * A client application used for connecting to a chat server and talking to other users.
 */
public class Client
{

    /**
     * Construct a client that communicates with the specified server on the provided port.
     *
     * @param servAddress The server's ip address.
     * @param serverPort  The server port.
     * @param usrName     The user name used for identification purposes on the server.
     */
    public Client(String servAddress, int serverPort, String usrName)
    {
        serverAddress = servAddress;
        nbPort = serverPort;
        userName = usrName;
        serverListen = true;
        crtMessagePrinter = new MessagePrinterConsole("");
        crtListener = null;
    }

    /**
     * Construct a client that communicates with the specified server on the provided port.
     *
     * @param servAddress The server's ip address.
     * @param serverPort  The server port.
     * @param usrName     The user name used for identification purposes on the server.
     * @param msgPrinter  The message printer where the output will be printed in. (only for a non console application)
     */
    public Client(String servAddress, int serverPort, String usrName, MessagePrinter msgPrinter)
    {
        serverAddress = servAddress;
        nbPort = serverPort;
        userName = usrName;
        serverListen = true;
        crtMessagePrinter = msgPrinter;
        crtListener = null;
    }

    synchronized boolean listenerStopped()
    {
        return !serverListen;
    }

    /**
     * Send a message to the chat server.
     *
     * @param crtMessage The message to be sent.
     */
    public void SendMessage(MsgBase crtMessage)
    {
        try
        {
            outputStream.writeObject(crtMessage);
        }
        catch (Exception e)
        {
            crtMessagePrinter.setString(ClientStrTab.STREAM_COMM_ERR.toString());
            crtMessagePrinter.flushString();
        }
    }

    /**
     * Start the current client app and connect to the specified server on the specified port.
     *
     * @return Indicates if the communication has been established
     */
    public boolean start()
    {
        try
        {
            crtSocket = new Socket(serverAddress, nbPort);
        }
        catch (Exception e)
        {
            crtMessagePrinter.setString(ClientStrTab.ERR_CONN_SERVER.toString());
            crtMessagePrinter.flushString();
            return false;
        }

        crtMessagePrinter.setString(String.format(ClientStrTab.SRV_CONN_SUCCESFULL.toString(), crtSocket.getInetAddress().toString(), nbPort));
        crtMessagePrinter.flushString();

        try
        {
            outputStream = new ObjectOutputStream(crtSocket.getOutputStream());
            inputStream = new ObjectInputStream(crtSocket.getInputStream());
        }
        catch (Exception e)
        {
            crtMessagePrinter.setString(ClientStrTab.STREAM_COMM_ERR.toString());
            crtMessagePrinter.flushString();
            return false;
        }

        crtListener = new Thread(new SrvEvListener());
        crtListener.start();

        try
        {
            MsgBase connectMsg = MessageFactory.createMessage(MessageType.CONNECT, userName, "");
            outputStream.writeObject(connectMsg);
        }
        catch (Exception e)
        {
            crtMessagePrinter.setString(ClientStrTab.STREAM_COMM_ERR.toString());
            crtMessagePrinter.flushString();
            return false;
        }

        return true;
    }

    /**
     * Close the currently input and output streams.
     */
    void closeStreams()
    {
        try
        {
            inputStream.close();
            outputStream.close();
        }
        catch (IOException e)
        {
            crtMessagePrinter.setString(ClientStrTab.ERR_CLOSING_RES.toString());
            crtMessagePrinter.flushString();
        }
    }

    /**
     * Close the current socket used to communicate with the server.
     */
    void closeSocket()
    {
        try
        {
            crtSocket.close();
        }
        catch (IOException ex)
        {
            crtMessagePrinter.setString(ClientStrTab.ERR_CLOSING_RES.toString());
            crtMessagePrinter.flushString();
        }
    }

    /**
     * Disconnect the current client from the server.
     */
    public void disconnectFromServer()
    {
        serverListen = false;

        try
        {
            crtListener.join();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        closeStreams();
        closeSocket();
    }

    public static void main(String[] args)
    {
        int nbArgs = args.length;

        if (nbArgs != 3)
        {
            System.out.println(ClientStrTab.IMPROPER_CMD_LINE.toString());
        }
        else
        {
            String srvString = args[0];
            String portString = args[1];
            String userString = args[2];

            if (!portString.matches("-\\d+"))
            {
                System.out.println(ClientStrTab.IMPROPER_CMD_LINE.toString());
            }
            else
            {
                int portNb = 60010;
                String srvParse = srvString.replaceFirst("-", "");
                String portNbParse = portString.replaceFirst("-", "");
                String usrParse = userString.replaceFirst("-", "");
                portNb = Integer.parseInt(portNbParse);

                Client clInstance = new Client(srvParse, portNb, usrParse);
                clInstance.start();

                Scanner commandScanner = new Scanner(System.in);

                MsgBase disconnectMsg = MessageFactory.createMessage(MessageType.DISCONNECT, usrParse, "");
                MsgBase listUsrMsg    = MessageFactory.createMessage(MessageType.LIST_ACTIVE_USERS, usrParse, "");
                MsgBase messageMsg    = MessageFactory.createMessage(MessageType.MESSAGE, usrParse, "");

                while (true)
                {
                    System.out.print(">");
                    String command = commandScanner.nextLine();

                    if (command.equalsIgnoreCase(MessageType.DISCONNECT.toString()))
                    {
                        clInstance.SendMessage(disconnectMsg);
                        clInstance.disconnectFromServer();

                        clInstance.crtMessagePrinter.setString(ClientStrTab.DISCONNECTED.toString());
                        clInstance.crtMessagePrinter.flushString();
                    }
                    else if (command.equalsIgnoreCase(MessageType.LIST_ACTIVE_USERS.toString()))
                    {
                        clInstance.SendMessage(listUsrMsg);
                    }
                    else
                    {
                        clInstance.SendMessage(messageMsg);
                    }
                }
            }
        }
    }

    // - Members
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    Socket crtSocket;

    private final String serverAddress, userName;
    private final int nbPort;
    volatile boolean serverListen;
    MessagePrinter crtMessagePrinter;
    Thread crtListener;

    // - Inner classes

    /**
     * A threaded class that acts as a listener for server events.
     */
    public class SrvEvListener implements Runnable
    {

        @Override
        public void run()
        {
            while (true)
            {
                try
                {
                    Object messageReceived = inputStream.readObject();
                    MsgBase baseMsg = (MsgBase) messageReceived;
                    MessageType msgType = baseMsg.getMessageType();
                    String messageStringOutput = "";

                    if (msgType == MessageType.MESSAGE)
                    {
                        MessageMsg msgObject = (MessageMsg) baseMsg;
                        messageStringOutput = msgObject.getSenderID() + " : " + msgObject.getMessageBody() + "\n";
                    }
                    else
                    {
                        messageStringOutput = baseMsg.getSenderID() + " : " + baseMsg.getMessageType().toString() + "\n";
                    }

                    if (listenerStopped())
                    {
                        return;
                    }

                    crtMessagePrinter.setString(messageStringOutput);
                    crtMessagePrinter.flushString();
                }
                catch (IOException | ClassNotFoundException e)
                {
                    // - if I reach this line this means that the server already closed the streams and sockets for the crt user
                    crtMessagePrinter.setString(ClientStrTab.SRV_CONN_CLOSED.toString());
                    crtMessagePrinter.flushString();
                    break;
                }
            }
        }
    }

    class MessagePrinterConsole extends MessagePrinter
    {

        public MessagePrinterConsole(String msg)
        {
            super(msg);
        }

        @Override
        public void flushString()
        {
            System.out.println(crtString);
        }

    }
}
