package view.editSpace.editPane;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class EditSpase {
    final private int BAR_WIDTH_RATE = 3;
    // 何倍に拡大して表示するか
    // PPQ * 4拍 * 倍率
    final private int BAR_WIDTH = 24 * 4 *BAR_WIDTH_RATE;
    // 4分音符は24tick
    final private int QUAETER_NOTE_WIDTH = BAR_WIDTH/4;
    final private int QUAETER_NOTE_HEIGHT = 25;

    // クオンタイズ
    // デフォルトで16分音符でいい感じに修正
    private int quantize = QUAETER_NOTE_WIDTH/4;

    // デフォルトの音の長さ
    // デフォルトで16分音符が入力される．
    private int defNoteLen = QUAETER_NOTE_WIDTH / 4;

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
        this.editSpase.setOnMouseClicked(
            event -> clickEventHandler(event)
        );
    }

    public void clickEventHandler(MouseEvent event){
        if(event.getClickCount() == 2){
            //System.out.println("2 click!!");
            setNoteRect(event);
        }
    }

    public void setNoteRect(MouseEvent event){
        double x = event.getX();
        double y = event.getY();

        /*
        System.out.println(
            "x=" + x +
            " y=" + y +
            " qu=" + this.quantize
        );
        */

        x = x - (x % this.quantize);
        y = y - (y % QUAETER_NOTE_HEIGHT);

        //System.out.println("x=" + x + " y=" + y);

        int  notePich       = (int)y / QUAETER_NOTE_HEIGHT;
        long noteLength     = defNoteLen / BAR_WIDTH_RATE;
        long noteStartTick  = (long) x / BAR_WIDTH_RATE;

        NoteRect noteRect = new NoteRect(
            this,
            notePich,
            noteLength,
            noteStartTick
        );

        Rectangle rect = noteRect.getRect();
        this.notes.add(noteRect);

        setRect(rect, x, y);
    }

    // this.editSpaseに長方形を追加するだけのメソッド
    private void setRect(Rectangle rect,double x,double y){
        AnchorPane.setLeftAnchor(rect, x);
        AnchorPane.setTopAnchor(rect, y);
        this.editSpase.getChildren().add(rect);
    }

    // this.editSpaseにある長方形から指定の
    // オブジェクトを削除するメソッド
    public void removeNoteRect(NoteRect noteRect){
        this.notes.remove(noteRect);
        this.editSpase.getChildren().remove(noteRect.getRect());
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

            if(xPoint % (4*QUAETER_NOTE_WIDTH) == 0){
                Label tmpLabel = new Label(
                    Integer.toString(xPoint / BAR_WIDTH_RATE)
                );
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
            if(yPoint % (this.QUAETER_NOTE_HEIGHT * 12) == 0){
                tmpLine.setStroke(Color.GREEN);
            }
            yLine.add(tmpLine);

            Label tmpLabel = new Label(
                Integer.toString(yPoint / this.QUAETER_NOTE_HEIGHT)
            );
            if((yPoint / this.QUAETER_NOTE_HEIGHT)% 12 == 0){
                tmpLabel.setText("C" + (yPoint / this.QUAETER_NOTE_HEIGHT)/12);
            }
            if((yPoint / this.QUAETER_NOTE_HEIGHT)% 12 == 4){
                tmpLabel.setText("E" + (yPoint / this.QUAETER_NOTE_HEIGHT)/12);
            }
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

    public int getBAR_WIDTH_RATE(){
        return this.BAR_WIDTH_RATE;
    }
}
