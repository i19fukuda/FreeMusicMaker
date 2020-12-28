package view.trackLine;

import java.util.ArrayList;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import view.trackBox.TrackBox;

public class TrackLine {
    private ArrayList<TrackBox> trackBoxs;

    private AnchorPane          lineRoot;

    private AnchorPane  controllRoot;
    // 楽器の番号
    private TextField   instNo;
    private TextField   trackName;

    private int     trackId;
    private double  lineHeight;
    private double  lineWidth;

    public TrackLine(int trackId, double lineHeight, double lineWidth){

        this.trackBoxs  = new ArrayList<>();

        this.lineRoot   = new AnchorPane();

        this.trackId = trackId;
        this.lineHeight = lineHeight;
        this.lineWidth = lineWidth;

        // contrllのセット
        this.controllRoot   = new AnchorPane();

        this.instNo = new TextField("0");
        this.trackName = new TextField(Integer.toString(trackId));
        HBox ctrlBox = new HBox();
        ctrlBox.getChildren().addAll(this.trackName, this.instNo);
        AnchorPane.setTopAnchor(ctrlBox, 0.0);
        AnchorPane.setLeftAnchor(ctrlBox, 0.0);
        this.controllRoot.getChildren().addAll(ctrlBox);

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
}
