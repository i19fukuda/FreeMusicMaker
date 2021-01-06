package view.editSpace.editPane;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class EditSpase {
    Stage editStage = new Stage();
    final private int BAR_WIDTH_RATE = 6;
    // 何倍に拡大して表示するか
    // PPQ * 4拍 * 倍率
    final private int BAR_WIDTH = 24 * 4 *BAR_WIDTH_RATE;
    // 4分音符は24tick
    final private int QUAETER_NOTE_WIDTH = BAR_WIDTH/4;
    final private int QUAETER_NOTE_HEIGHT = 18;

    // クオンタイズ
    // デフォルトで16分音符でいい感じに修正
    private int quantize = QUAETER_NOTE_WIDTH/4;

    // デフォルトの音の長さ
    // デフォルトで16分音符が入力される．
    private int defNoteLen = QUAETER_NOTE_WIDTH / 4;

    // スクロールペインの最大の大きさ
    private int maxRootHeight = 2286;
    private int maxRootWidth = 60000;
    // 実際にユーザが入力するところ
    private ScrollPane editSpaseRoot;
    private GridPane   editAndshowRoot;
    private AnchorPane editSpase;
    private ScrollPane pichSupportSp;
    private AnchorPane pichCheckSpase;
    private ScrollPane quaeterChSp;
    private AnchorPane quaeterCkSpase;

    // 補助線，マウスに沿って移動
    private Line xSupportLine;
    private Line ySupportLine;

    // NoteRectを記憶し，呼び出し元に渡せるようにする．
    private ArrayList<NoteRect> notes;

    public EditSpase(){
        init();
        this.editSpase.setOnMouseClicked(
            event -> clickEventHandler(event)
        );
        this.editSpase.setOnMouseMoved(
            event -> mouseMuveEventHandler(event)
        );
    }

    public void clickEventHandler(MouseEvent event){
        if(
            event.getClickCount() == 2
            && event.getButton() == MouseButton.PRIMARY
            && ! event.isControlDown()
            && ! event.isShiftDown()
            ){
            //System.out.println("2 click!!");
            setNoteRect(event);
        }
    }

    private void mouseMuveEventHandler(MouseEvent event){
        double x,y;
        x = event.getX();
        y = event.getY();

        this.xSupportLine.setStartY(y);
        this.xSupportLine.setStartX(0);
        this.xSupportLine.setEndY(y);
        this.xSupportLine.setEndX(maxRootWidth);

        this.ySupportLine.setStartY(0);
        this.ySupportLine.setStartX(x);
        this.ySupportLine.setEndY(this.maxRootHeight);
        this.ySupportLine.setEndX(x);
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
            this.QUAETER_NOTE_HEIGHT,
            noteLength,
            noteStartTick
        );

        Rectangle rect = noteRect.getRect();
        this.notes.add(noteRect);

        setRect(rect, x, y);
    }


    public NoteRect createNoteRect(
        int         notePich,
        double      noteHeight,
        long        noteLength,
        long        noteStartTick,
        long        noteId
        )
    {
        NoteRect nr = new NoteRect(
            this,
            notePich,
            noteHeight,
            noteLength,
            noteStartTick
        );
        nr.setNoteId(noteId);
        return nr;
    }
    public NoteRect createNoteRect(
        int         notePich,
        long        noteLength,
        long        noteStartTick,
        long        noteId
    ){
        NoteRect nr = this.createNoteRect(
            notePich,
            this.QUAETER_NOTE_HEIGHT,
            noteLength,
            noteStartTick,
            noteId
        );
        return nr;
    }

    public void addNoteRect(NoteRect noteRect){
        this.notes.add(noteRect);
    }

    public void loadAndSetNoteRects(ArrayList<NoteRect> notes){
        double x, y;
        int notePich;
        long noteStartTick;
        for(NoteRect note : notes){
            notePich = note.getNotePich();
            noteStartTick = note.getNoteStartTick();
            x = noteStartTick * BAR_WIDTH_RATE;
            y = notePich * QUAETER_NOTE_HEIGHT;
            this.setRect(note.getRect(), x, y);
            note.setNoteLength(note.getNoteLength());
            this.notes.add(note);
        }
    }

    // notesの中のものを全部書き出す
    public void createAndSetNoteRecs(){
        double x, y;
        int notePich;
        long noteStartTick;
        for(NoteRect note : this.notes){
            notePich = note.getNotePich();
            noteStartTick = note.getNoteStartTick();
            x = noteStartTick * BAR_WIDTH_RATE;
            y = notePich * QUAETER_NOTE_HEIGHT;
            note.setNoteLength(note.getNoteLength());
            this.setRect(note.getRect(), x, y);
        }
    }

    // this.editSpaseに長方形を追加するだけのメソッド
    private void setRect(Rectangle rect, double x, double y){
        //System.out.println("rect setted");
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

    public void longerElectedNotes(){
        ArrayList<NoteRect> electedNotes = this.getElectedNotes();
        for(NoteRect note:electedNotes){
            note.justLongerNote();
        }
    }
    public void shorterElectedNotes(){
        ArrayList<NoteRect>electedNotes = this.getElectedNotes();
        for(NoteRect note:electedNotes){
            note.justShorterNote();
        }
    }
    public void removeElectedNotes(){
        ArrayList<NoteRect> electedNotes = this.getElectedNotes();
        for(NoteRect note:electedNotes){
            note.justRemoveNote();
        }
    }

    private ArrayList<NoteRect> getElectedNotes(){
        ArrayList<NoteRect> electedNotes = new ArrayList<>();
        for(NoteRect note:this.notes){
            if(note.isElected()){
                electedNotes.add(note);
            }
        }
        return electedNotes;
    }

    private void scrollEventHandler(ScrollEvent event){
        double dx, dy;
        dx = this.editSpaseRoot.getHvalue();
        dy = this.editSpaseRoot.getVvalue();
        this.pichSupportSp.setVvalue(dy);
        this.quaeterChSp.setHvalue(dx);
    }

    public void init(){
        this.notes          = new ArrayList<>();
        this.editSpaseRoot  = new ScrollPane();
        this.editSpaseRoot.setOnScroll(event -> scrollEventHandler(event));
        this.editAndshowRoot= new GridPane();
        this.editSpase      = new AnchorPane();

        this.pichSupportSp  = new ScrollPane();
        this.pichCheckSpase = new AnchorPane();
        this.quaeterChSp    = new ScrollPane();
        this.quaeterCkSpase = new AnchorPane();

        this.xSupportLine = new Line(0, 0, 0, 0);
        this.ySupportLine = new Line(0, 0, 0, 0);
        this.xSupportLine.setStroke(Color.MAGENTA);
        this.ySupportLine.setStroke(Color.MAGENTA);
        this.editSpase.getChildren().addAll(
            this.xSupportLine, this.ySupportLine
        );

        ArrayList<Line> xLine   = new ArrayList<>();
        ArrayList<Line> yLine   = new ArrayList<>();
        ArrayList<Label> xLabel = new ArrayList<>();
        ArrayList<Label> yLabel = new ArrayList<>();

        Border border = new Border(
            new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT
            )
        );

        // 縦線を引く
        for(
            int xPoint = 0;
            xPoint < this.maxRootWidth;
            xPoint += this.QUAETER_NOTE_WIDTH / 4
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

            if(xPoint % (QUAETER_NOTE_WIDTH) == 0){
                Label tmpLabel = new Label(
                    Integer.toString(xPoint / BAR_WIDTH_RATE / 24)
                );
                AnchorPane.setTopAnchor(tmpLabel, 0.0);
                AnchorPane.setLeftAnchor(tmpLabel,(double)xPoint);
                tmpLabel.setBorder(border);
                xLabel.add(tmpLabel);

            }
        }

        for(
            int yPoint = 0;
            yPoint < this.maxRootHeight;
            yPoint += this.QUAETER_NOTE_HEIGHT
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
            if((yPoint / this.QUAETER_NOTE_HEIGHT) % 12 == 0){
                tmpLabel.setText(
                    "C" + (yPoint / this.QUAETER_NOTE_HEIGHT) / 12
                );
            }
            if((yPoint / this.QUAETER_NOTE_HEIGHT) % 12 == 4){
                tmpLabel.setText(
                    "E" + (yPoint / this.QUAETER_NOTE_HEIGHT) / 12
                );
            }
            if((yPoint / this.QUAETER_NOTE_HEIGHT) % 12 == 7){
                tmpLabel.setText(
                    "G" + (yPoint / this.QUAETER_NOTE_HEIGHT) / 12
                );
            }
            tmpLabel.setText(String.format("%3s",tmpLabel.getText()));

            AnchorPane.setTopAnchor(tmpLabel, (double)yPoint);
            AnchorPane.setLeftAnchor(tmpLabel,0.0);
            tmpLabel.setMinWidth(100);
            tmpLabel.setBorder(border);
            yLabel.add(tmpLabel);
        }

        // 可変長配列に入ったLineを乗っけていく
        for(Line l : xLine){
            this.editSpase.getChildren().add(l);
        }
        for(Line l : yLine){
            this.editSpase.getChildren().add(l);
        }
        for(Label l : xLabel){
            this.quaeterCkSpase.getChildren().add(l);
        }
        for(Label l : yLabel){
            this.pichCheckSpase.getChildren().add(l);
        }
        this.pichSupportSp.setContent(this.pichCheckSpase);
        this.pichSupportSp.setVbarPolicy(ScrollBarPolicy.NEVER);

        this.quaeterChSp.setContent(this.quaeterCkSpase);
        this.quaeterChSp.setHbarPolicy(ScrollBarPolicy.NEVER);

        this.editSpaseRoot.setContent(this.editSpase);

        GridPane.setColumnIndex(this.quaeterChSp, 1);
        GridPane.setRowIndex(   this.quaeterChSp, 0);
        GridPane.setColumnIndex(this.pichSupportSp, 0);
        GridPane.setRowIndex(   this.pichSupportSp, 1);
        GridPane.setColumnIndex(    this.editSpaseRoot, 1);
        GridPane.setRowIndex(       this.editSpaseRoot, 1);

        this.editAndshowRoot.getChildren().addAll(
            this.pichSupportSp,
            this.quaeterChSp,
            this.editSpaseRoot
        );

        Scene scene = new Scene(this.editAndshowRoot);
        this.editStage.setScene(scene);
    }

    public void showStage(){
        this.editStage.show();
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

    public int getQUAETER_NOTE_HEIGHT(){
        return this.QUAETER_NOTE_HEIGHT;
    }

    public Stage getEditStage(){
        return this.editStage;
    }
}
