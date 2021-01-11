package view.home;

import java.io.File;
import java.util.ArrayList;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import midi.conductor.Conductor;
import projectIo.projectLoad.anyTrackLoad.LoadProject;
import projectIo.projectSave.anyTrackSave.SaveProject;
import view.editSpace.editPane.Note;
import view.editSpace.editPane.NoteRect;
import view.home.soundMixer.SoundMixer;
import view.trackBox.TrackBox;
import view.trackLine.TrackLine;

public class Home {
    private GridPane root;
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
    //ソロとかミュートとか
    private SoundMixer soundMixer;

    // 背景
    private Background blacBackground;

    public Home(){
        this.blacBackground = new Background(
            new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)
        );

        this.root       = new GridPane();
        this.root.setBackground(this.blacBackground);

        this.lines      = new ArrayList<>();

        this.ctrlRoot   = new HBox();

        this.inTenpoFL  = new TextField("120");

        this.soundMixer = new SoundMixer(this.lines);

        this.playButton = new Button("play");
        Image playImage = new Image(
            getClass().getResourceAsStream("../../image/play_button.png")
        );
        ImageView playImageView = new ImageView(playImage);
        playImageView.setFitWidth(15);
        playImageView.setFitHeight(15);
        this.playButton.setGraphic(playImageView);
        this.playButton.setOnMouseClicked(
            event ->playEventHandler(event)
        );


        this.stopButton = new Button("stop");
        Image stopImage = new Image(
            getClass().getResourceAsStream("../../image/stop_button.png")
        );
        ImageView stopImageView = new ImageView(stopImage);
        stopImageView.setFitWidth(15);
        stopImageView.setFitHeight(15);
        this.stopButton.setGraphic(stopImageView);
        this.stopButton.setOnMouseClicked(
            event -> stopEventHandler(event)
        );

        this.addLineButton = new Button(" addLine ");
        Image addLineImage = new Image(
            getClass().getResourceAsStream("../../image/plus.png")
        );
        ImageView addLineImageView = new ImageView(addLineImage);
        addLineImageView.setFitWidth(15);
        addLineImageView.setFitHeight(15);
        this.addLineButton.setGraphic(addLineImageView);
        this.addLineButton.setOnMouseClicked(
            event -> addLineHandler(event)
        );


        this.removeLineButton = new Button(" remove Line ");
        Image removeLineImage = new Image(
            getClass().getResourceAsStream("../../image/minus.png")
        );
        ImageView removeLineImageView = new ImageView(removeLineImage);
        removeLineImageView.setFitWidth(15);
        removeLineImageView.setFitHeight(15);
        this.removeLineButton.setGraphic(removeLineImageView);
        this.removeLineButton.setOnMouseClicked(
            event -> removeLineEventHandler(event)
        );
        
        this.linesVBox  = new VBox();
        VBox.setVgrow(this.linesVBox, Priority.ALWAYS);
        this.linesVBox.setPrefHeight(2000);
        this.linesVBox.setBackground(this.blacBackground);

        this.lineRoot   = new ScrollPane();
        this.lineRoot.setBackground(this.blacBackground);

        this.ctrlRoot.getChildren().addAll(
            this.playButton,
            this.stopButton,
            this.addLineButton,
            this.removeLineButton,
            this.inTenpoFL
        );


        this.addLine(0, lineHeight, lineWidth);

        this.lineRoot.setContent(this.linesVBox);

        this.soundMixer = new SoundMixer(this.lines);


        GridPane.setConstraints(this.ctrlRoot, 0, 0);
        GridPane.setConstraints(this.lineRoot, 0, 1);
        GridPane.setConstraints(this.soundMixer.getSoundMixerRoot(), 0, 2);
        RowConstraints row0 = new RowConstraints();
        row0.setPrefHeight(100);
        RowConstraints row1 = new RowConstraints(100,5000,Double.MAX_VALUE);
        row1.setVgrow(Priority.ALWAYS);
        RowConstraints row2 = new RowConstraints();
        row2.setPrefHeight(100);

        this.root.getRowConstraints().addAll(row0,row1,row2);

        this.root.getChildren().addAll(
            this.ctrlRoot,
            this.lineRoot,
            this.soundMixer.getSoundMixerRoot()
        );
        this.root.setOnKeyPressed(
            event -> keyEventHandler(event)
        );
        this.root.setBackground(this.blacBackground);
    }

    public void addLineHandler(MouseEvent event){
        int lineId = this.lines.size();
        addLine(lineId, lineHeight, lineWidth);
    }

    public void addLine(int trackId,double lineHeight,double lineWidth){
        TrackLine line = new TrackLine(trackId, lineHeight, lineWidth);
        this.lines.add(line);
        this.linesVBox.getChildren().add(line.getLineRoot());
        this.soundMixer.addLineInfo(line);
    }
    public void addLine(TrackLine line){
        this.lines.add(line);
        this.linesVBox.getChildren().add(line.getLineRoot());
        this.soundMixer.addLineInfo(line);
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
        this.soundMixer.removeLineInfo(lineId);
    }

    public void saveProject(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("save file");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("text file", "*.txt")
        );
        File file = fileChooser.showSaveDialog(new Stage());
        if(file != null){
            SaveProject sp = new SaveProject(lines);
            sp.saveAll(file, this.getTempo());
        }
    }
    public void saveEventHandler(Event event){
        this.saveProject();
    }

    public  void loadEventHandler(Event event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("load file");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("text file", "*.txt")
        );
        File file = fileChooser.showOpenDialog(new Stage());

        if(file != null){
            LoadProject lp = new LoadProject(file);

            this.linesVBox.getChildren().clear();
            this.soundMixer.getLineInfoRoot().getChildren().clear();

            ArrayList<TrackLine> linesTmp = lp.loadAll(
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

    public GridPane getHomeRoot(){
        return this.root;
    }


    // todo
    public void playEventHandler(Event event){
        int tempo = this.getTempo();
        this.conductor = new Conductor(tempo);

        this.soundMixer.setMuteTrack();
        this.soundMixer.setSoloTrack();

        int volume,instNo;

        //ソロパートのセット
        if(isSoloInLines()){
            int soloLineId = -1;
            for(TrackLine line:this.lines){
                if(line.isSolo()){
                    soloLineId = this.lines.indexOf(line);
                    break;
                }
            }
            if(soloLineId == -1){
                System.out.println("ソロパートが見つかりません");
            }

            TrackLine line = lines.get(soloLineId);

            for(TrackBox box:line.getBoxs()){
                for(Note note:box.getNotes()){
                    this.setNote(
                        soloLineId,
                        line.getInstNo(),
                        line.getMasterVol(),
                        note
                    );
                }
            }
        } else {
            // muteされていないもの以外のノートを登録
            for(int lineNo=0; lineNo<this.lines.size(); lineNo++){
                TrackLine line = lines.get(lineNo);
                instNo = line.getInstNo();

                if(line.isMute()) continue;

                volume = line.getMasterVol();

                for(Note note:line.getMixedNotes()){
                    this.setNote(
                        lineNo,
                        instNo,
                        note.getVolum(),
                        note
                    );
                }

                for(TrackBox box : line.getBoxs()){
                    for(NoteRect note:box.getNotes()){
                        this.setNote(
                            lineNo,
                            instNo,
                            volume,
                            note
                        );
                    }
                }
            }
        }
        this.conductor.play(0);
    }

    private void setNote(int trackId, int instNo,int volume, Note note){
        int notePich;
        long startTick,length;
        System.out.println("noteVol = " + volume);
        notePich    = note.getNotePich();
        startTick   = note.getNoteStartTick();
        length      = note.getNoteLength();

        //System.out.println(lines.get(lineNo).getTrackId());
        this.conductor.setNotes(
            trackId,
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

    private boolean isSoloInLines(){
        boolean flag = false;
        for(TrackLine line:this.lines){
            if(line.isSolo()){
                flag = true;
                break;
            }
        }
        return flag;
    }
}
