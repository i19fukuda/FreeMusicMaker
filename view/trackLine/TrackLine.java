package view.trackLine;

import java.util.ArrayList;

import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import view.trackBox.TrackBox;

public class TrackLine {
    private ArrayList<TrackBox> trackBoxs;

    private AnchorPane          lineRoot;

    private AnchorPane  controllRoot;

    private TextField   trackName;

    private int     trackId;
    private double  lineHeight;
    private double  lineWidth;

    // 楽器選択のメニューバー
    ElectInst electInst;

    public TrackLine(int trackId, double lineHeight, double lineWidth){

        this.trackBoxs  = new ArrayList<>();
        this.trackBoxs.add(new TrackBox(50, 0, 1900));

        this.lineRoot   = new AnchorPane();

        this.trackId = trackId;
        this.lineHeight = lineHeight;
        this.lineWidth = lineWidth;

        this.electInst = new ElectInst();
        MenuBar instMenubar = this.electInst.getMenuBar();

        // contrllのセット
        this.controllRoot   = new AnchorPane();

        this.trackName = new TextField(Integer.toString(trackId));
        Rectangle trackBox = this.trackBoxs.get(0).getRect();
        HBox ctrlBox = new HBox();
        ctrlBox.getChildren().addAll(
            this.trackName,
            instMenubar
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
            System.out.println(
                "trackLine114 set InstNo(String instNo):"
                + e.getMessage()
            );
            no = 0;
        }
        this.setInstNo(no);
    }
    public void setInstNo(int instNo){
        this.electInst.setInstNo(instNo);
    }
}
