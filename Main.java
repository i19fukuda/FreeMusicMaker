import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.editSpace.editPane.EditSpase;
import view.home.Home;

public class Main extends Application{
    EditSpase editer;
    int tempo = 120;
    int inst = 0;
    TextField inTenpoField;
    TextField insField;
    TextField inSaveFileName;
    TextField inLoadFileName;
    public static void main(String[] argas){
        Application.launch(argas);
    }

    public void start(Stage stage){
        stage.setWidth(1900);
        stage.setHeight(1040);
        Home home = new Home();
        VBox root = new VBox();
        root.getChildren().add(home.getHomeRoot());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}