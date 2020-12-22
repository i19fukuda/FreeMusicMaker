import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
    int inst = 0;
    TextField inTenpoField;
    TextField insField;
    public static void main(String[] argas){
        Application.launch(argas);
    }

    public void start(Stage stage){
        stage.setTitle("hi");
        stage.setWidth(1900);
        stage.setHeight(1040);

        VBox root = new VBox();
        VBox playCh = new VBox();
        VBox tempoCh = new VBox();
        VBox instCh = new VBox();

        // 本来ならプレビューを置くスペース
        // 機能限定版ではとりあえずスペースだけ確保してある．
        Rectangle previewSpase = new Rectangle(1920,100,Color.BLUE);

        HBox controllBox = new HBox();

        // 再生ボタン
        // 本来なら上部やトラックごとに配置
        // 機能限定版ではとりあえず置いとく
        Label playLabel = new Label("押して再生");
        Button playButton = new Button("PLAY");
        playButton.setOnAction(event -> playEventHandler(event));
        playCh.getChildren().addAll(playLabel, playButton);

        // テンポ入力フィールド
        // 機能限定版にてとりあえずおいてある
        Label tenpoINLabel = new Label("テンポを入力してください");
        this.inTenpoField = new TextField("テンポを入力!");
        this.inTenpoField.setOnAction(event -> inTenpoAction(event));
        tempoCh.getChildren().addAll(tenpoINLabel,this.inTenpoField);

        // 楽器入力フィールド
        // 機能限定版ではとりあえず数値による入力
        // 本番環境ではコンボボックス?
        Label instLabel = new Label("楽器を数値で入力");
        this.insField = new TextField("0");
        this.insField.setOnAction(event -> instAction(event));
        instCh.getChildren().addAll(instLabel, this.insField);

        controllBox.getChildren().addAll(
            playCh, tempoCh, instCh
        );

        this.editer = new EditSpase();
        ScrollPane editRoot = editer.getEditSpaseRoot();

        root.getChildren().addAll(previewSpase, controllBox, editRoot);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void inTenpoAction(Event event){
        int tmpTempo;
        try{
            tmpTempo = Integer.parseInt(this.inTenpoField.getText());
        } catch (NumberFormatException e){
            tmpTempo = 120;
        }
        if(tmpTempo<5 || tmpTempo > 400){
            tmpTempo = 120;
        }
        this.inTenpoField.setText(Integer.toString(tmpTempo));
        this.tempo = tmpTempo;
        // System.out.println(this.tempo);
    }

    public void instAction(Event event){
        int tmpInst;
        try{
            tmpInst = Integer.parseInt(this.insField.getText());
        }catch(NumberFormatException e){
            tmpInst=(0);
        }
        this.inst = tmpInst;
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

        //テスト！！！！！！
        midiCon.changeInstrument(1,this.inst);

        midiCon.play(0);
    }
}