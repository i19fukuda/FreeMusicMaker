package view.editSpace.editPane;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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

    private double rectHeight;
    private double rectWidth  = 12;

    private EditSpase root;

    public NoteRect(
        EditSpase   root,
        int         notePich,
        double      noteHeight,
        long        noteLength,
        long        noteStartTick
        ){

        setRoot(root);
        this.rectHeight = noteHeight;
        setNotePich(notePich);
        setNoteLength(noteLength);
        setNoteStartTick(noteStartTick);
        setNoteId(System.currentTimeMillis());

        //System.out.println("まじのnotePich=" + this.notePich);
        //System.out.println("まじのnoteLength=" + this.noteLength);
        //System.out.println("まじのnoteStartTick=" + this.noteStartTick);

        this.rect = new Rectangle();
        this.rect.setFill(Color.BLACK);
        this.rect.setStroke(Color.RED);
        this.rect.setHeight(this.rectHeight);
        this.rect.setWidth(this.rectWidth -2);

        this.rect.setOnMouseClicked(
            event -> clickEventHandler(event)
        );
        this.rect.setOnScroll(event -> scrollEventHandler(event));
    }

    public void clickEventHandler(MouseEvent event){
        // todo
        // ノートをクリックしたときの動作を記述する
        // とりあえず一回クリックしたら赤くなるようにした
        this.rect.setFill(Color.RED);
        if(event.getButton() == MouseButton.SECONDARY){
            this.rect.setFill(Color.BLUE);
            this.root.removeNoteRect(this);
        }
    }

    public void scrollEventHandler(ScrollEvent event){
        if(event.getDeltaX()>0){
            this.rect.setWidth(this.rect.getWidth()+6);
        }
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
