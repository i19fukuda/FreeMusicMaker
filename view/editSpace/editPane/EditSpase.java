package view.editSpace.editPane;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class EditSpase {
    final private int BAR_WIDTH_RATE = 3;

    final private int BAR_WIDTH = 24 * BAR_WIDTH_RATE * 4;
    // 4分音符は24tick
    final private int QUAETER_NOTE_WIDTH = BAR_WIDTH/4;
    final private int QUAETER_NOTE_HEIGHT = 25;

    // スクロールペインの最大の大きさ
    private int maxRootHeight = 3175;
    private int maxRootWidth = 6000;
    // 実際にユーザが入力するところ
    private ScrollPane editSpaseRoot;
    private AnchorPane editSpase;
    // NoteRectを記憶し，呼び出し元に渡せるようにする．
    private ArrayList<NoteRect> notes;

    public EditSpase(){
        init();
    }

    public void init(){
        this.notes = new ArrayList<>();
        this.editSpaseRoot = new ScrollPane();
        this.editSpase = new AnchorPane();

        ArrayList<Line> xLine = new ArrayList<>();
        ArrayList<Line> yLine = new ArrayList<>();
        ArrayList<Label> xLabel = new ArrayList<>();
        ArrayList<Label> yLabel = new ArrayList<>();

        // 縦線を引く
        for(
            int xPoint = 0;
            xPoint< this.maxRootWidth;
            xPoint+=this.QUAETER_NOTE_WIDTH / 4
        ){
            Line tmpLine = new Line(
                xPoint, 0,
                xPoint, this.maxRootHeight
                );
            if(xPoint % this.QUAETER_NOTE_WIDTH == 0){
                // 4分音符の長さの線を赤くする
                tmpLine.setStroke(Color.RED);
                if(xPoint % this.BAR_WIDTH == 0){
                    tmpLine.setStroke(Color.BLUE);
                }
            }
            xLine.add(tmpLine);

            if(xPoint % 100 == 0){
                Label tmpLabel = new Label(Integer.toString(xPoint));
                AnchorPane.setTopAnchor(tmpLabel, 0.0);
                AnchorPane.setLeftAnchor(tmpLabel,(double)xPoint);
                xLabel.add(tmpLabel);
            }
        }

        for(
            int yPoint = 0;
            yPoint< this.maxRootHeight;
            yPoint+=this.QUAETER_NOTE_HEIGHT
        ){
            Line tmpLine = new Line(
                0, yPoint,
                this.maxRootWidth, yPoint
                );
            yLine.add(tmpLine);

            Label tmpLabel = new Label(Integer.toString(yPoint / this.QUAETER_NOTE_HEIGHT));
            AnchorPane.setTopAnchor(tmpLabel, (double)yPoint);
            AnchorPane.setLeftAnchor(tmpLabel,0.0);
            yLabel.add(tmpLabel);
        }

        // 可変長配列に入ったLineを乗っけていく
        for(Line l:xLine){
            this.editSpase.getChildren().add(l);
        }
        for(Line l:yLine){
            this.editSpase.getChildren().add(l);
        }
        for(Label l:xLabel){
            this.editSpase.getChildren().add(l);
        }
        for(Label l:yLabel){
            this.editSpase.getChildren().add(l);
        }

        this.editSpaseRoot.setContent(this.editSpase);
    }

    public ScrollPane getEditSpaseRoot(){
        return this.editSpaseRoot;
    }

    public ArrayList<NoteRect> getNotes(){
        return this.notes;
    }
}
