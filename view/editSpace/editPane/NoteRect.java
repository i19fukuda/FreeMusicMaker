package view.editSpace.editPane;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author i19fukuda
 * エディットスペースで描画されるような音符の情報をもった矩形を保持するクラス。
 */
public class NoteRect extends Note{
    //ここのフィールドに置かれるnoteTick等は
    //まじの値を代入すること．
    //表示のための座標じゃない．

    private Rectangle rect;

    private boolean isElected;

    private double changeRate;

    private double rectHeight;
    private double rectWidth  = 12;

    private EditSpase root;
    /**
     * @param root 根となるエディットスペース
     * @param notePich 音の高さ(0-127)
     * @param noteHeight 矩形の高さ
     * @param noteLength 音の長さ(tick単位)
     * @param noteStartTick 音が始まるtick
     */
    public NoteRect(
        EditSpase   root,
        int         notePich,
        double      noteHeight,
        long        noteLength,
        long        noteStartTick
    ){
        super(
            System.currentTimeMillis(),
            notePich,
            noteLength,
            noteStartTick,
            127
        );

        setRoot(root);
        this.rectHeight = noteHeight;
        this.setNoteLength(noteLength);

        this.isElected = false;

        this.changeRate = this.root.getBAR_WIDTH_RATE() * 6.0;

        //System.out.println("まじのnotePich=" + this.notePich);
        //System.out.println("まじのnoteLength=" + this.noteLength);
        //System.out.println("まじのnoteStartTick=" + this.noteStartTick);

        this.rect = new Rectangle();
        this.rect.setFill(Color.MAGENTA);
        this.rect.setStroke(Color.RED);
        this.rect.setHeight(this.rectHeight);
        this.rect.setWidth(this.rectWidth - 2);
        this.rect.setOpacity(0.5);

        this.rect.setOnMouseClicked(
            event -> clickEventHandler(event)
        );

    }

    private void clickEventHandler(MouseEvent event){;
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
    /**
     * ただ音を長くするだけのメソッド
     */
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
    /**
     * ただ音を短くするだけのメソッド
     */
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
    /**
     * ただ音を完全に削除するだけのメソッド
     */
    public void justRemoveNote(){
        this.root.removeNoteRect(this);
    }
    private void removeNote(){
        this.root.removeElectedNotes();
        if(! isElected){
            this.justRemoveNote();
        }
    }
    /**
     * 自分自身を選択済みとしてセットするメソッド
     * 内部的には自分自身の選択済みフラグを反転させているだけです。
     */
    public void electNote(){
        this.isElected = ! this.isElected;
        if(isElected){
            this.rect.setFill(Color.BLUE);
        } else{
            this.rect.setFill(Color.MAGENTA);
        }
    }
    /**
     * 選択を解除するメソッド
     */
    public void unElectNote(){
        this.isElected = false;
        this.rect.setFill(Color.MAGENTA);
    }
    /**
     * 問答無用で音を選択済みにセットするメソッド
     */
    public void electNoteMust(){
        this.isElected = true;
        this.rect.setFill(Color.BLUE);
    }

    private void setRoot(EditSpase root){
        this.root = root;
    }

    public EditSpase getRoot(){
        return this.root;
    }

    @Override

    public void setNoteLength(long noteLength){
        super.setNoteLength(noteLength);
        // widthプロパティーを変更することで矩形の
        // 幅の初期値を変更する.
        double lengthRate = this.root.getBAR_WIDTH_RATE();
        double lengthPadding = 2;
        this.rectWidth = (noteLength * lengthRate) - lengthPadding;
    }


    public void setRect(Rectangle rect){
        this.rect = rect;
    }

    public Rectangle getRect(){
        return this.rect;
    }

    /**
     * 音が選択されているかを返す。
     * @return trueなら選択済み
     */
    public boolean isElected(){
        return this.isElected;
    }
}
