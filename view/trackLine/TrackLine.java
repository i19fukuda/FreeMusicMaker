package view.trackLine;

import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import view.editSpace.editPane.Note;
import view.trackBox.TrackBox;
import view.trackLine.ctrlBox.Controls;

public class TrackLine {
    private ArrayList<TrackBox> trackBoxs;

    private AnchorPane          lineRoot;

    private AnchorPane  controllRoot;
    private Controls controls;

    private int     trackId;
    private double  lineHeight;
    private double  lineWidth;

    final private int MAX_VOL = 127;
    private double vol;

    private boolean isSolo;
    private boolean isMute;

    public TrackLine(int trackId, double lineHeight, double lineWidth){

        this.trackBoxs  = new ArrayList<>();
        this.trackBoxs.add(new TrackBox(50, 0,1000));

        this.lineRoot   = new AnchorPane();

        this.trackId    = trackId;
        this.lineHeight = lineHeight;
        this.lineWidth  = lineWidth;

        this.vol = 127;


        // contrllのセット
        this.controllRoot   = new AnchorPane();

        this.controls = new Controls(this);

        Rectangle trackBox  = this.trackBoxs.get(0).getRect();

        VBox ctrlBox = controls.getctrlBoxRoot();

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
        //System.out.println("clicked");
    }

    public ArrayList<Note> getMixedNotes(){
        ArrayList<Note> notes = new ArrayList<>();
        notes = this.controls.getMix().getNotes();
        return notes;
    }

    //0-127のまじの値を格納
    //割合で入力されたときの場合にのみ仕様
    public void setMasterVol(double volumeRange){
        if(volumeRange>1.0){
            volumeRange = 1.0;
            showErrorDialog("out of range(volumeRange):" +volumeRange);
        }
        this.vol = volumeRange * MAX_VOL;
    }
    //0-127のまじの値を格納
    public void setMasterVol(int volume){
        if(volume>127||volume<0){
            showErrorDialog("out of range(volume):" +volume);
            volume = 127;
        }
        this.controls.setMasterVol(volume);
        this.vol = volume;
    }

    public int getMasterVol(){
        int masVol = (int) this.vol;
        System.out.println(masVol);
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
        instNo = this.controls.getElectInst().getInstNo();
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
        this.controls.getElectInst().setInstNo(no);
    }
    public void setInstNo(int instNo){
        this.controls.getElectInst().setInstNo(instNo);
    }

    public String getTrackName(){
        String inputString = this.controls.getTrackNameFl().getText();
        return inputString;
    }

    public void setIsSolo(boolean isSolo){
        this.isSolo = isSolo;
    }
    public boolean isSolo(){
        return this.isSolo;
    }

    public void setIsMute(boolean isMute){
        this.isMute = isMute;
    }
    public boolean isMute(){
        return isMute;
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
