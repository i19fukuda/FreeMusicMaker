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
    private Rectangle rect;
    private long noteId;
    private long noteLength;
    private int notePich;
    private long noteStartTick;

    private double rectHeight = 20;
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

        this.rect = new Rectangle();
        this.rect.setFill(Color.BLACK);

        this.rect.setOnMouseClicked(event -> clickEventHandler(event));
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
