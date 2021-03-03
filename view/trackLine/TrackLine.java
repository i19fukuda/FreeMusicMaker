package view.trackLine;

import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.editSpace.editPane.Note;
import view.trackBox.TrackBox;
import view.trackLine.ctrlBox.Controls;
/**
 * @author i19fukuda1k
 * @see view.trackBox
 * @see view.editSpace
 * トラックの情報をGUIで仮想化するためのクラス
 * トラックごとの処理を行う
 */
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

    /**
     * @param trackId トラックの番号
     * @param lineHeight トラックの高さ
     * @param lineWidth トラックの長さ
     */

    public TrackLine(int trackId, double lineHeight, double lineWidth){

        this.trackBoxs  = new ArrayList<>();
        this.trackBoxs.add(new TrackBox(this, lineHeight, 0,lineWidth));

        this.lineRoot   = new AnchorPane();
        this.lineRoot.setBorder(
            new Border(
                new BorderStroke(
                    Color.BLACK,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT
                )
            )
        );

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

    private void clickEventHandler(MouseEvent event){
        //TrackBox trackBox = new TrackBox(50, notes, widthRate)
        //System.out.println("clicked");
    }

    /**
     * ミキサーによってミックスされ、生成されたノートを返す
     * @return ミックスされたNoteの配列
     * @see view.editSpace.editPane
     * @see view.trackLine.mix
     */

    public ArrayList<Note> getMixedNotes(){
        ArrayList<Note> notes = new ArrayList<>();
        notes = this.controls.getMix().getNotes();
        return notes;
    }

    /**
     * トラックのマスターボリュームをセットするメソッド。
     * 0-127までの値がセットされます。
     * @param volumeRange 0-1.0の割合で音量
     */
    public void setMasterVol(double volumeRange){
        if(volumeRange>1.0){
            volumeRange = 1.0;
            showErrorDialog("out of range(volumeRange):" +volumeRange);
        }
        this.vol = volumeRange * MAX_VOL;
    }
    //0-127のまじの値を格納
    /**
     * トラックのマスターボリュームをセットするメソッド
     * 0-127までの値がセットされます。
     * @param volume 0-127までの音量の値。
     */
    public void setMasterVol(int volume){
        if(volume>127||volume<0){
            showErrorDialog("out of range(volume):" +volume);
            volume = 127;
        }
        this.controls.setMasterVol(volume);
        this.vol = volume;
    }

/**
 * マスターボリュームを0-127の値で返します。
 * @return マスターボリューム(0-127)
 */
    public int getMasterVol(){
        int masVol = (int) this.vol;
        System.out.println(masVol);
        return masVol;
    }

    /**
     * TrackLineが持っている最上位のパーツを返します。
     * @return TrackLineのルート
     */
    public AnchorPane getLineRoot(){
        return this.lineRoot;
    }

    /**
     * トラックIDを返します。
     * @return トラックID
     */
    public int getTrackId(){
        return this.trackId;
    }
    /**
     * トラックの幅を返します。
     * @return トラックの幅
     */
    public double getWidth(){
        return this.lineWidth;
    }
    /**
     * 虎久野幅をセットします。
     * @param width トラックの幅
     */
    public void setWidth(double width){
        this.lineWidth = width;
    }
    /**
     * トラックの高さを返します
     * @return トラックの高さ
     */
    public double getHeight(){
        return this.lineHeight;
    }

    /**
     * トラックの高さをセットします。
     * @param height トラックの高さ
     */
    public void setHeight(double height){
        this.lineHeight = height;
    }

    /**
     * トラックが保持するトラックボックスの可変長配列を返します。
     * @return トラックが保持するすべてのトラックボックス
     */
    public ArrayList<TrackBox> getBoxs(){
        return this.trackBoxs;
    }
    /**
     * トラックにセットされている楽器の番号を返します
     * @see view.trackLine
     * @return 楽器の番号
     */
    public int getInstNo(){
        int instNo;
        instNo = this.controls.getElectInst().getInstNo();
        return instNo;
    }

    /**
     * トラックに楽器をセットします。
     * 文字列を数値に変換してセットします。
     * @see view.trackLine
     * @param instNo 楽器の番号
     */
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
    /**
     * トラックに楽器をセットします。
     * @see view.trackLine
     * @param instNo 楽器の番号
     */
    public void setInstNo(int instNo){
        this.controls.getElectInst().setInstNo(instNo);
    }

    /**
     * トラックにつけられているトラックの名前を得られます。
     * @return トラックの名前
     */
    public String getTrackName(){
        String inputString = this.controls.getTrackNameFl().getText();
        return inputString;
    }
    /**
     * トラックがソロかどうかをセットできます。
     * @param isSolo trueならソロにセット
     */
    public void setIsSolo(boolean isSolo){
        this.isSolo = isSolo;
    }
    /**
     * トラックがソロかどうかをゲットします
     * @return trueならソロ
     */
    public boolean isSolo(){
        return this.isSolo;
    }

    /**
     * トラックをミュートにセットできます
     * @param isMute trueならミュート
     */
    public void setIsMute(boolean isMute){
        this.isMute = isMute;
    }
    /**
     * トラックがミュートがゲットできます。
     * @return trueならミュート
     */
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
