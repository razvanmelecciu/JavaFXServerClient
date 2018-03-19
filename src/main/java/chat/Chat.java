package chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**

 @author Razvan
 */
public class Chat extends Application
{
    
    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader loaderObject = new FXMLLoader(getClass().getResource("../../resources/FXMLDocument.fxml"));
        Parent root = loaderObject.load();

        Scene scene = new Scene(root, initialWidth, initialHeight);

        stage.setTitle("Small Talk Chat Client");

        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
        stage.setScene(scene);

        FXMLDocumentController controller = loaderObject.getController();

        stage.setOnCloseRequest((WindowEvent event) ->
        {
            controller.HandleCLose();
        });

        stage.show();
    }

    /**
     @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
    final static int initialWidth = 800, initialHeight = 420;
    final static int minWidth = 800, minHeight = 500;
    final static int maxWidth = 900, maxHeight = 450;
}
