import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import view.editSpace.editPane.EditSpase;

public class Main extends Application{
    public static void main(String[] argas){
        Application.launch(argas);
    }

    public void start(Stage stage){
        stage.setTitle("hi");
        stage.setWidth(1900);
        stage.setHeight(1040);

        VBox root = new VBox();

        // 本来ならプレビューを置くスペース
        // 機能限定版ではとりあえずスペースだけ確保してある．
        Rectangle previewSpase = new Rectangle(1920,400,Color.BLUE);

        // 再生ボタン
        // 本来なら上部やトラックごとに配置
        // 機能限定版ではとりあえず置いとく
        Button playButton = new Button("PLAY");

        EditSpase editer = new EditSpase();
        ScrollPane editRoot = editer.getEditSpaseRoot();

        root.getChildren().addAll(previewSpase, playButton, editRoot);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}