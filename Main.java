import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import midi.conductor.Conductor;
import view.editSpace.editPane.EditSpase;
import view.editSpace.editPane.NoteRect;

public class Main extends Application{
    EditSpase editer;
    int tempo = 120;
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
        playButton.setOnAction(event -> playEventHandler(event));

        this.editer = new EditSpase();
        ScrollPane editRoot = editer.getEditSpaseRoot();

        root.getChildren().addAll(previewSpase, playButton, editRoot);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void playEventHandler(ActionEvent event){
        Conductor midiCon = new Conductor(this.tempo);
        ArrayList<NoteRect> noteRects = this.editer.getNotes();
        int notePich,volume;
        long startTick,length;
        for(NoteRect nr:noteRects){
            notePich    = nr.getNotePich();
            volume      = 120;
            startTick   = nr.getNoteStartTick();
            length      = nr.getNoteLength();

            midiCon.setNotes(notePich, volume, startTick, length);
        }

        midiCon.play(0);
    }
}