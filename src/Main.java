import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("View.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.init();
        primaryStage.setTitle("FXIMViewer//HW6");
        primaryStage.setScene(new Scene(root, 1024, 700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
