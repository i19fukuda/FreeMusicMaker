import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import view.home.Home;

/**
 * このアプリのメインクラスです。ステージの用意などがされます。
 * @author i19fukuda1k
 * @version 0.0.1
 * @see view.home
 */

public class Main extends Application{
    private Home home;
    private VBox root;

    private MenuBar     menubar;
    private Menu        mFile;
    private MenuItem    miSave,miLoad;
    /**
     * メインのクラス
     * デバッグの際にメインメソッドが見つからない問題を回避
     * @param argas コマンドライン引数
     */
    public static void main(String[] argas){
        Application.launch(argas);
    }

    /**
     * javaFxのアプリケーションをスタートする
     */
    public void start(Stage stage){
        stage.setWidth(1900);
        stage.setHeight(1040);
        stage.setTitle("FreeMusicMaker");

        this.home = new Home();
        this.root = new VBox();

        menuInit();

        root.getChildren().addAll(
            this.menubar, home.getHomeRoot()
        );



        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.show();
    }

    private void menuInit(){
        this.menubar    = new MenuBar();
        this.mFile      = new Menu("file");

        this.miSave     = new MenuItem("Save project    ctrl + S");
        this.miSave.setOnAction(
            event -> this.home.saveEventHandler(event)
        );

        this.miLoad     = new MenuItem("Load project");
        this.miLoad.setOnAction(
            event -> home.loadEventHandler(event)
        );

        this.menubar.getMenus().addAll(this.mFile);
        this.mFile.getItems().addAll(
            this.miSave, this.miLoad
        );
    }
}