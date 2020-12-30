package view.home;

import java.util.ArrayList;

import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import midi.conductor.Conductor;
import view.editSpace.editPane.NoteRect;
import view.trackBox.TrackBox;
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
    VBox linesVBox;
    ScrollPane lineRoot;

    public Home(){
        this.root       = new VBox();
        this.lines      = new ArrayList<>();
        this.ctrlRoot   = new HBox();
        this.inTenpoFL  = new TextField("120");
        this.playButton = new Button("play");
        this.playButton.setOnMouseClicked(event ->playEventHandler(event));
        this.addLineButton = new Button(" addLine ");
        this.addLineButton.setOnMouseClicked(event -> addLineHandler(event));
        this.linesVBox  = new VBox();
        this.lineRoot   = new ScrollPane();

        this.ctrlRoot.getChildren().addAll(
            this.playButton, this.addLineButton, this.inTenpoFL
        );


        this.addLine(0, lineHeight, lineWidth);

        this.lineRoot.setContent(this.linesVBox);

        this.root.getChildren().addAll(this.ctrlRoot,this.lineRoot);
    }

    public void addLineHandler(MouseEvent event){
        int lineId = this.lines.size();
        addLine(lineId, lineHeight, lineWidth);
    }

    public void addLine(int trackId,double lineHeight,double lineWidth){
        TrackLine line = new TrackLine(trackId, lineHeight, lineWidth);
        this.lines.add(line);
        this.linesVBox.getChildren().add(line.getLineRoot());
    }

    public VBox getHomeRoot(){
        return this.root;
    }


    // todo
    public void playEventHandler(Event event){
        int tenpo = Integer.parseInt(this.inTenpoFL.getText());
        Conductor conductor = new Conductor(tenpo);

        int notePich,volume;
        long startTick,length;
        for(int lineNo = 0;lineNo<lines.size();lineNo++){
            conductor.createTrack();
            conductor.changeInstrument(
                lineNo,
                lines.get(lineNo).getInstNo()
            );
            System.out.println("trackId = "+ lineNo);
            System.out.println("inst changed" + lines.get(lineNo).getInstNo());
            for(TrackBox box:lines.get(lineNo).getBoxs()){
                for(NoteRect note:box.getNotes()){
                    notePich    = note.getNotePich();
                    volume      = 120;
                    startTick   = note.getNoteStartTick();
                    length      = note.getNoteLength();

                    System.out.println(lines.get(lineNo).getTrackId());
                    conductor.setNotes(
                        lines.get(lineNo).getTrackId(),
                        notePich,
                        volume,
                        startTick,
                        length
                    );
                }
            }
        }
        conductor.play(0);

    }
}
