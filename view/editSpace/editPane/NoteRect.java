package view.editSpace.editPane;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author i19fukuda
 * 音符の情報を保持し，表示する特殊な矩形
 * javafx.scene.shape.Rectangleを利用する．
 */

public class NoteRect {
    //ここのフィールドに置かれるnoteTick等は
    //まじの値を代入すること．
    //表示のための座標じゃない．

    private Rectangle rect;
    private long noteId;
    private long noteLength;
    private int notePich;
    private long noteStartTick;

    private final double RECT_HEIGHT = 20;
    private double rectWidth  = 12;

    private EditSpase root;

    public NoteRect(
        EditSpase   root,
        int         notePich,
        long        noteLength,
        long        noteStartTick
        ){

        setRoot(root);
        setNotePich(notePich);
        setNoteLength(noteLength);
        setNoteStartTick(noteStartTick);
        setNoteId(System.currentTimeMillis());

        //System.out.println("まじのnotePich=" + this.notePich);
        //System.out.println("まじのnoteLength=" + this.noteLength);
        //System.out.println("まじのnoteStartTick=" + this.noteStartTick);

        this.rect = new Rectangle();
        this.rect.setFill(Color.BLACK);
        this.rect.setHeight(this.RECT_HEIGHT);
        this.rect.setWidth(this.rectWidth);

        this.rect.setOnMouseClicked(
            event -> clickEventHandler(event)
        );
    }

    public void clickEventHandler(MouseEvent event){
        // todo
        // ノートをクリックしたときの動作を記述する
        // とりあえず一回クリックしたら赤くなるようにした
        this.rect.setFill(Color.RED);
    }

    // todo
    // 設定できない値なら弾くようにする必要あり
    public void setRoot(EditSpase root){
        this.root = root;
    }

    public EditSpase getRoot(){
        return this.root;
    }

    public void setNotePich(int notePich){
        this.notePich = notePich;
    }

    public int getNotePich(){
        return this.notePich;
    }

    public void setNoteLength(long noteLength){
        this.noteLength = noteLength;
        // widthプロパティーを変更することで矩形の
        // 幅の初期値を変更する.
        //とりあえず2倍しとく．
        double lengthRate = this.root.getBAR_WIDTH_RATE();
        double lengthPadding = 2;
        this.rectWidth = (noteLength * lengthRate) - lengthPadding;
    }

    public long getNoteLength(){
        return this.noteLength;
    }

    public void setNoteStartTick(long noteStartTick){
        this.noteStartTick = noteStartTick;
    }

    public long getNoteStartTick(){
        return this.noteStartTick;
    }

    public void setNoteId(long noteId){
        this.noteId = noteId;
    }

    public long getNoteId(){
        return this.noteId;
    }
    public void setRect(Rectangle rect){
        this.rect = rect;
    }

    public Rectangle getRect(){
        return this.rect;
    }
}
