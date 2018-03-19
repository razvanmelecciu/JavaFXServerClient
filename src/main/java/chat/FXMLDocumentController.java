package chat;

import client.Client;
import client.MessagePrinter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import message.MessageObject;
import message.MessageType;

/**

 @author Razvan
 */
public class FXMLDocumentController implements Initializable
{    
     
    @FXML
    private void enterMessages(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            if (clInstance == null)
            {
                return;
            }

            String crtMsg = currentMessage.getText();
            clInstance.SendMessage(new MessageObject(MessageType.CHAT_MSG_EVENT, crtMsg));
            currentMessage.clear();
            
            event.consume(); // Consume Event
        }
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        Object crtObject = event.getSource();
        Button crtButton = (Button) crtObject;
        
        String crtString = crtButton.getId();
        
        if (crtString.equalsIgnoreCase("ex"))
        {
            Alert crtMsgBox = new Alert(Alert.AlertType.CONFIRMATION, "");
            crtMsgBox.setTitle("Confirm exit");
            crtMsgBox.setHeaderText("Exiting will disconnect you from the active chat server.\nDo you wish to proceed ?");
            crtMsgBox.showAndWait();
            if (!crtMsgBox.getResult().getButtonData().isCancelButton())
                System.exit(0);
        }
        else if (crtString.equalsIgnoreCase("ab"))
        {
            Alert crtMsgBox = new Alert(Alert.AlertType.INFORMATION, "Developed by: Melecciu Razvan - 2017");
            crtMsgBox.setTitle("About...");
            crtMsgBox.setHeaderText("This tiny chat application has been developed as a project for school.\n However I think"
                                  + " that it can be used on a daily basis for chatting with\n other users that are connected "
                                  + "to the specific server.");
            crtMsgBox.setResizable(false);
            crtMsgBox.showAndWait();
        }
        else if (crtString.equalsIgnoreCase("srvc"))
        {
            if (clInstance != null)
                return;
            
            String serverAddress = serverAddressCtrl.getText();
            String serverPort    = serverPortCtrl.getText();
            String userName      = userNameCtrl.getText();
            int srvPort = Integer.parseInt(serverPort);
            
            // Perform validations and if ok continue
            if (!serverPort.matches("-\\d+"))
            {
                // invalid data msgbox
            }
            
            startClientFunctionality(serverAddress, srvPort, userName);          
        }
        else if (crtString.equalsIgnoreCase("srvd"))
        {
            if (clInstance == null)
                return;
            
            clInstance.SendMessage(new MessageObject(MessageType.LEAVE_CHAT));
            clInstance.disconnectFromServer();
            clInstance = null;
            
            /*crtMsgPrinter.setString(ClientStrTab.DISCONNECTED.toString());
            crtMsgPrinter.flushString();*/
        }
        else if (crtString.equalsIgnoreCase("au"))
        {
            if (clInstance == null)
                return;
            
            clInstance.SendMessage(new MessageObject(MessageType.LIST_ACTIVE_USERS));
        }
        else if (crtString.equalsIgnoreCase("sm"))
        {
            if (clInstance == null)
            {
                return;
            }

            String crtMsg = currentMessage.getText();
            clInstance.SendMessage(new MessageObject(MessageType.CHAT_MSG_EVENT, crtMsg));
            currentMessage.clear();
        }
        else if (crtString.equals("cls"))
        {
            serverMessages.clear();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        crtMsgPrinter = new MessagePrinterTextArea("", serverMessages); 
    }  

    private void startClientFunctionality(String serverAddress, int srvPort, String userName)
    {            
        clInstance = new Client(serverAddress, srvPort, userName, crtMsgPrinter);
        serverConnectedOk = clInstance.start();
    }
    
    public void HandleCLose()
    {
        // - When closing the window disconnect from the chat server
        if (clInstance == null)
        {
            return;
        }

        if (serverConnectedOk)
        {
            clInstance.SendMessage(new MessageObject(MessageType.LEAVE_CHAT));
            clInstance.disconnectFromServer();
            clInstance = null;
        }
    }
    
    // - Members
    
    @FXML
    private TextField serverAddressCtrl;
    @FXML
    private TextField serverPortCtrl;
    @FXML
    private TextField userNameCtrl;
    @FXML
    private TextArea  serverMessages;
    @FXML
    private TextArea  currentMessage; 
    
    private Client clInstance = null;   
    MessagePrinter crtMsgPrinter;
    boolean serverConnectedOk = true;
    
    // - Inner classes
    
    class MessagePrinterTextArea extends MessagePrinter
    {

        public MessagePrinterTextArea(String msg, TextArea crtTextArea)
        {
            super(msg);
        }

        @Override
        synchronized public void flushString()
        {
            Platform.runLater(()->
            {
                serverMessages.appendText(crtString + "\n");
            });
        }
    }
}
