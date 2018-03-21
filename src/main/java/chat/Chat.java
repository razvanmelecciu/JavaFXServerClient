package chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 The main JavaFX chat application
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
                controller.HandleCLose());

        stage.show();
    }

    /**
     @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
    final private static int initialWidth = 800;
    final private static int initialHeight = 420;
    final private static int minWidth = 800;
    final private static int minHeight = 500;
}
