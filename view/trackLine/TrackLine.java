package view.trackLine;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import view.editSpace.editPane.Note;
import view.trackBox.TrackBox;
import view.trackLine.mix.Mix;

public class TrackLine {
    private ArrayList<TrackBox> trackBoxs;

    private AnchorPane          lineRoot;

    private AnchorPane  controllRoot;

    private TextField   volumField;
    private TextField   trackName;

    private Button  mixButton;
    private Mix     mix;

    private int     trackId;
    private double  lineHeight;
    private double  lineWidth;

    final private int MAX_VOL = 127;
    private double vol;

    // 楽器選択のメニューバー
    ElectInst electInst;

    public TrackLine(int trackId, double lineHeight, double lineWidth){

        this.trackBoxs  = new ArrayList<>();
        this.trackBoxs.add(new TrackBox(50, 0,1000));

        this.lineRoot   = new AnchorPane();
        this.volumField = new TextField("100");
        this.volumField.setOnAction(
            event -> volChangeEventHandler(event)
        );

        this.trackId    = trackId;
        this.lineHeight = lineHeight;
        this.lineWidth  = lineWidth;

        this.vol = 1.0;

        this.electInst      = new ElectInst();
        MenuBar instMenubar = this.electInst.getMenuBar();

        // contrllのセット
        this.controllRoot   = new AnchorPane();

        this.trackName      = new TextField(Integer.toString(trackId));
        Rectangle trackBox  = this.trackBoxs.get(0).getRect();

        this.mixButton = new Button("mix");
        this.mixButton.setOnMouseClicked(
            event -> mixEventHandler(event)
        );
        this.mix = new Mix(this);

        HBox ctrlBox    = new HBox();
        VBox ctrlKit    = new VBox();
        ctrlKit.getChildren().addAll(
            this.trackName,
            this.volumField
        );
        ctrlBox.getChildren().addAll(
            ctrlKit,
            instMenubar,
            this.mixButton
        );
        AnchorPane.setTopAnchor(ctrlBox, 0.0);
        AnchorPane.setLeftAnchor(ctrlBox, 0.0);

        AnchorPane.setTopAnchor(trackBox, 0.0);
        AnchorPane.setLeftAnchor(trackBox, 400.0);

        this.controllRoot.getChildren().addAll(ctrlBox, trackBox);

        // lineRootのセット
        this.lineRoot.getChildren().addAll(this.controllRoot);
        this.lineRoot.setPrefHeight(lineHeight);
        this.lineRoot.setPrefWidth(lineWidth);
        this.lineRoot.setOnMouseClicked(
            event -> clickEventHandler(event)
        );
    }

    public void clickEventHandler(MouseEvent event){
        //TrackBox trackBox = new TrackBox(50, notes, widthRate)
        System.out.println("clicked");
    }

    public void mixEventHandler(MouseEvent event){
        mix.showMixer();
    }

    public ArrayList<Note> getMixedNotes(){
        ArrayList<Note> notes = new ArrayList<>();
        notes = this.mix.getNotes();
        return notes;
    }

    private void volChangeEventHandler(ActionEvent event){
        String inputText = this.volumField.getText();
        int tmpVol = 100;
        try{
            tmpVol = Integer.parseInt(inputText);
        } catch (NumberFormatException e){
            tmpVol = 100;
            showErrorDialog(e.getMessage());
        }
        this.setMasterVol(tmpVol * 0.01);
    }

    public void setMasterVol(double volume){
        if(volume>1.0){
            volume = 1.0;
            showErrorDialog("out of range");
        }
        this.vol = volume;
    }

    public int getMasterVol(){
        int masVol = (int) (MAX_VOL * this.vol);
        return masVol;
    }

    public AnchorPane getLineRoot(){
        return this.lineRoot;
    }

    public int getTrackId(){
        return this.trackId;
    }

    public double getWidth(){
        return this.lineWidth;
    }
    public void setWidth(double width){
        this.lineWidth = width;
    }

    public double getHeight(){
        return this.lineHeight;
    }
    public void setHeight(double height){
        this.lineHeight = height;
    }

    public ArrayList<TrackBox> getBoxs(){
        return this.trackBoxs;
    }

    public int getInstNo(){
        int instNo;
        instNo = this.electInst.getInstNo();
        return instNo;
    }
    public void setInstNo(String instNo){
        int no;
        try{
            no = Integer.parseInt(instNo);
        }catch(NumberFormatException e){
            this.showErrorDialog(e.getMessage());
            System.out.println(
                "trackLine114 set InstNo(String instNo):"
                + e.getMessage()
            );
            no = 0;
        }
        this.setInstNo(no);
        this.electInst.setInstNo(no);
    }
    public void setInstNo(int instNo){
        this.electInst.setInstNo(instNo);
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
