package view.home;

import java.util.ArrayList;

import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import midi.conductor.Conductor;
import projectIo.projectLoad.anyTrackLoad.LoadProject;
import projectIo.projectSave.anyTrackSave.SaveProject;
import view.editSpace.editPane.Note;
import view.editSpace.editPane.NoteRect;
import view.trackBox.TrackBox;
import view.trackLine.TrackLine;

public class Home {
    private VBox root;
    private ArrayList<TrackLine> lines;

    private double lineHeight   = 40;
    private double lineWidth    = 6000;

    // いろいろ表示するところ
    private HBox ctrlRoot;
    private TextField inTenpoFL;
    private Button playButton;
    private Button stopButton;
    private Button addLineButton;
    private Button removeLineButton;
    // trackLineがずらーってなるところ
    private VBox linesVBox;
    private ScrollPane lineRoot;

    // midiコンダクター
    private Conductor conductor;

    public Home(){
        this.root       = new VBox();
        this.lines      = new ArrayList<>();
        this.ctrlRoot   = new HBox();
        this.inTenpoFL  = new TextField("120");

        this.playButton = new Button("play");
        this.playButton.setOnMouseClicked(
            event ->playEventHandler(event)
        );

        this.stopButton = new Button("stop");
        this.stopButton.setOnMouseClicked(
            event -> stopEventHandler(event)
        );

        this.addLineButton = new Button(" addLine ");
        this.addLineButton.setOnMouseClicked(
            event -> addLineHandler(event)
        );
        this.removeLineButton = new Button(" remove Line ");
        this.removeLineButton.setOnMouseClicked(
            event -> removeLineEventHandler(event)
        );
        
        this.linesVBox  = new VBox();
        this.lineRoot   = new ScrollPane();
        this.lineRoot.prefHeight(Double.MAX_VALUE);

        this.ctrlRoot.getChildren().addAll(
            this.playButton,
            this.stopButton,
            this.addLineButton,
            this.removeLineButton,
            this.inTenpoFL
        );


        this.addLine(0, lineHeight, lineWidth);

        this.lineRoot.setContent(this.linesVBox);

        this.root.getChildren().addAll(
            this.ctrlRoot,this.lineRoot
        );
        this.root.setOnKeyPressed(
            event -> keyEventHandler(event)
        );
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
    public void addLine(TrackLine line){
        this.lines.add(line);
        this.linesVBox.getChildren().add(line.getLineRoot());
    }

    public void removeLineEventHandler(MouseEvent event){
        TextInputDialog inputDialog = new TextInputDialog(
                                        "トラックの番号を入力してください"
                                    );
        inputDialog.showAndWait();
        int lineNo;
        try{
            lineNo = Integer.parseInt(
                        inputDialog.getEditor().getText()
            );
            this.removeLine(lineNo);
        } catch (NumberFormatException e){
            System.out.println(e.getMessage());
        }
    }

    public void removeLine(int lineId){
        this.linesVBox.getChildren().remove(lineId);
        this.lines.remove(lineId);
    }

    public void saveProject(){
        String fileName = "Project.txt";
        SaveProject sp = new SaveProject(lines);
        sp.saveAll(fileName, this.getTempo());

        Alert alert = new Alert(
            AlertType.INFORMATION,
            "保存されました",
            ButtonType.OK
        );
        alert.showAndWait();
    }
    public void saveEventHandler(Event event){
        this.saveProject();
    }

    public  void loadEventHandler(Event event){
        String fileName = "Project.txt";
        LoadProject lp  =new LoadProject(fileName);

        this.linesVBox.getChildren().clear();

        ArrayList<TrackLine> linesTmp = lp.loadAll(
                                            fileName,
                                            lineWidth,
                                            lineHeight
                                        );
        //System.out.println(linesTmp.size());
        for(TrackLine line:linesTmp){
            //System.out.println("line added!");
            this.addLine(line);
        }
        this.lines = linesTmp;
        this.inTenpoFL.setText(Integer.toString(lp.getTempo()));
    }

    private void keyEventHandler(KeyEvent event){
        if(event.isControlDown()){
            if(event.getCode() == KeyCode.S){
                this.saveProject();
                //System.out.println("save!");
            }
        } else {
        }
    }

    public VBox getHomeRoot(){
        return this.root;
    }


    // todo
    public void playEventHandler(Event event){
        int tempo = this.getTempo();
        this.conductor = new Conductor(tempo);

        int notePich,volume,instNo;
        long startTick,length;
        for(int lineNo = 0; lineNo<this.lines.size(); lineNo++){
            /*
            System.out.println("trackId = " + lineNo);
            System.out.println(
                "inst changed" + this.lines.get(lineNo).getInstNo()
            );
            */

            for(Note note:lines.get(lineNo).getMixedNotes()){
                notePich = note.getNotePich();
                volume = note.getVolum();
                startTick = note.getNoteStartTick();
                instNo =  lines.get(lineNo).getInstNo();
                length = note.getNoteLength();
                this.setNote(
                    lineNo,
                    notePich,
                    volume,
                    startTick,
                    instNo,
                    length
                );
            }
            //0-127のまじの値
            volume = this.lines.get(lineNo).getMasterVol();
            for(TrackBox box : lines.get(lineNo).getBoxs()){
                for(NoteRect note:box.getNotes()){
                        notePich    = note.getNotePich();
                        startTick   = note.getNoteStartTick();
                        length      = note.getNoteLength();

                        //System.out.println(lines.get(lineNo).getTrackId());
                        this.setNote(
                            lines.get(lineNo).getTrackId(),
                            notePich,
                            volume,
                            startTick,
                            this.lines.get(lineNo).getInstNo(),
                            length
                            );
                        }
                    }
            //System.out.println(conductor.getTrackSize());
        }
        conductor.play(0);
    }

    private void setNote(
        int lineNo,
        int notePich,
        int volume,
        long startTick,
        int instNo,
        long length
    ){
        conductor.setNotes(
            lineNo,
            notePich,
            volume,
            startTick,
            instNo,
            length
        );
    }

    public void stopEventHandler(MouseEvent event){
        conductor.stop();
    }

    public int getTempo(){
        int tempo = 120;
        try{
            tempo = Integer.parseInt(this.inTenpoFL.getText());
        }catch(NumberFormatException e){
            //System.out.println(e.getMessage());
            tempo = 120;
            showErrorDialog(e.getMessage());
        }
        //System.out.println("tempo seted : " + tempo);
        return tempo;
    }

    private void showErrorDialog(String errorMessage){
        Alert errorDialog = new Alert(
                            AlertType.ERROR,
                            errorMessage,
                            ButtonType.CLOSE
                            );
        errorDialog.showAndWait();
    }
}
