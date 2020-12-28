package view.home;

import java.util.ArrayList;

import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import midi.conductor.Conductor;
import view.trackLine.TrackLine;

public class Home {
    VBox root;
    ArrayList<TrackLine> lines;

    double lineHeight   = 40;
    double lineWidth    = 6000;

    // いろいろ表示するところ
    HBox ctrlRoot;
    TextField inTenpoFL;
    Button playButton;
    Button addLineButton;
    // trackLineがずらーってなるところ
    VBox linesRoot;

    public Home(){
        this.root       = new VBox();
        this.lines      = new ArrayList<>();
        this.ctrlRoot   = new HBox();
        this.inTenpoFL  = new TextField("120");
        this.playButton = new Button("play");
        this.playButton.setOnMouseClicked(event ->playEventHandler(event));
        this.addLineButton = new Button(" addLine ");
        this.addLineButton.setOnMouseClicked(event -> addLineHandler(event));
        this.linesRoot  = new VBox();

        this.ctrlRoot.getChildren().addAll(
            this.playButton, this.addLineButton, this.inTenpoFL
        );


        this.addLine(0, lineHeight, lineWidth);

        this.root.getChildren().addAll(this.ctrlRoot,this.linesRoot);
    }

    public void addLineHandler(MouseEvent event){
        int lineId = this.lines.size();
        addLine(lineId, lineHeight, lineWidth);
    }

    public void addLine(int trackId,double lineHeight,double lineWidth){
        TrackLine line = new TrackLine(trackId, lineHeight, lineWidth);
        this.lines.add(line);
        this.linesRoot.getChildren().add(line.getLineRoot());
    }

    public VBox getHomeRoot(){
        return this.root;
    }


    // todo
    public void playEventHandler(Event event){
        int tenpo = Integer.parseInt(this.inTenpoFL.getText());
        Conductor conductor = new Conductor(tenpo);

    }
}
