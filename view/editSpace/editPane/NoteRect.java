package view.editSpace.editPane;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NoteRect {
    //ここのフィールドに置かれるnoteTick等は
    //まじの値を代入すること．
    //表示のための座標じゃない．

    private Rectangle rect;
    private long noteId;
    private long noteLength;
    private int notePich;
    private long noteStartTick;

    private boolean isElected;

    private double changeRate;

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

        this.isElected = false;

        this.changeRate = this.root.getBAR_WIDTH_RATE() * 6.0;

        //System.out.println("まじのnotePich=" + this.notePich);
        //System.out.println("まじのnoteLength=" + this.noteLength);
        //System.out.println("まじのnoteStartTick=" + this.noteStartTick);

        this.rect = new Rectangle();
        this.rect.setFill(Color.BLACK);
        this.rect.setStroke(Color.RED);
        this.rect.setHeight(this.rectHeight);
        this.rect.setWidth(this.rectWidth - 2);
        this.rect.setOpacity(0.7);

        this.rect.setOnMouseClicked(
            event -> clickEventHandler(event)
        );

    }

    public void clickEventHandler(MouseEvent event){;
        if(
            // shift + 左クリック一回に付き16分音符一個分伸ばす
            (event.getButton() == MouseButton.PRIMARY )
            && event.isShiftDown()
        ){
            this.longerNote();
        } else if(
            // shift + 右クリック一回に付き16分音符一個分短くする
            (event.getButton() == MouseButton.SECONDARY)
            && event.isShiftDown()
            ){
            this.shorterNote();
        } else if(
            // ctrlが押されている場合の右クリックは削除
            event.getButton() == MouseButton.SECONDARY
            && event.isControlDown()
            ){
            this.removeNote();
        }   else if(
            // ctrlが押されている場合の左クリックは選択
            event.getButton() == MouseButton.PRIMARY
            && event.isControlDown()
            ){
            this.electNote();
        }
    }

    public void justLongerNote(){
        if(this.rect.getWidth() < 400){
            this.setNoteLength(this.getNoteLength() + 6);
            this.rect.setWidth(
                this.rect.getWidth() + changeRate
            );
        }
    }
    private void longerNote(){
        this.root.longerElectedNotes();
        if(! isElected){
            this.justLongerNote();
        }
    }

    public void justShorterNote(){
        if(this.rect.getWidth() > changeRate){
            this.setNoteLength(this.getNoteLength() - 6);
            this.rect.setWidth(
                this.rect.getWidth() - changeRate
            );
        }
    }
    private void shorterNote(){
        this.root.shorterElectedNotes();
        if(! isElected){
            this.justShorterNote();
        }
    }
    public void justRemoveNote(){
        this.root.removeNoteRect(this);
    }
    private void removeNote(){
        this.root.removeElectedNotes();
        if(! isElected){
            this.justRemoveNote();
        }
    }
    public void electNote(){
        this.isElected = ! this.isElected;
        if(isElected){
            this.rect.setFill(Color.BLUE);
        } else{
            this.rect.setFill(Color.BLACK);
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

    public boolean isElected(){
        return this.isElected;
    }
}
