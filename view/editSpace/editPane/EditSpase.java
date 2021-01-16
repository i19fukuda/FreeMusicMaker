package view.editSpace.editPane;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import midi.conductor.Conductor;
import view.trackLine.TrackLine;

public class EditSpase {
    Stage editStage;

    private TrackLine rootLine;

    // 何倍に拡大して表示するか
    final private int BAR_WIDTH_RATE = 6;
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

    //
    private Background blackBackGround;
    private Background whiteBackGround;

    // 補助線，マウスに沿って移動
    private Line xSupportLine;
    private Line ySupportLine;
    private double mouseX,mouseY;

    // NoteRectを記憶し，呼び出し元に渡せるようにする．
    private ArrayList<NoteRect> notes;

    public EditSpase(TrackLine rootLine){
        init(rootLine);
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
        this.mouseX = event.getX();
        this.mouseY = event.getY();

        this.xSupportLine.setStartY(mouseY);
        this.xSupportLine.setStartX(0);
        this.xSupportLine.setEndY(mouseY);
        this.xSupportLine.setEndX(maxRootWidth);

        this.ySupportLine.setStartY(0);
        this.ySupportLine.setStartX(mouseX);
        this.ySupportLine.setEndY(this.maxRootHeight);
        this.ySupportLine.setEndX(mouseX);

        this.scrollEventHandler();
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

        double iy = y / QUAETER_NOTE_HEIGHT;

        // System.out.println("x=" + x + " y=" + y);

        int  notePich       = 127 - (int) iy;
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
            y = (127 - notePich) * QUAETER_NOTE_HEIGHT;
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
            y = (127 - notePich) * QUAETER_NOTE_HEIGHT;
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

    // すべてのnoteRectの選択を解除するメソッド
    private void unElectAllNote(){
        for(NoteRect noteRict:this.notes){
            noteRict.unElectNote();
        }
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
        this.scrollEventHandler();
    }
    private void scrollEventHandler(){
        double dx, dy;
        dx = this.editSpaseRoot.getHvalue();
        dy = this.editSpaseRoot.getVvalue();
        this.pichSupportSp.setVvalue(dy);
        this.quaeterChSp.setHvalue(dx);
    }

    private void keyEventHandler(KeyEvent event){
        KeyCode inputCode = event.getCode();
        if(event.isControlDown()){
            if(inputCode == KeyCode.A){
                // すべてのノートを選択する
                for(NoteRect note:this.notes){
                    note.electNoteMust();
                }
            } else if(inputCode == KeyCode.C){
                //選択されたノートの長さ，高さの相対位置をクリップボードにpush
                this.pushClipBord();
            } else if(inputCode == KeyCode.V){
                //マウスのある位置にクリップボードの中身に貼り付け
                this.pasetNote();
            } else if(inputCode == KeyCode.W){
                this.editStage.close();
            }
        }
    }

    //クリックされたときのイベントハンドラ
    private void mouseClickEventHandler(MouseEvent event){
        //クリックされたらすべてのノートの選択を解除する
        if(
                ! event.isControlDown()
            &&  ! event.isShiftDown()
        ){
            this.unElectAllNote();
        }
    }

    private void quickPlayEvent(MouseEvent event, int notePich){
        Conductor conductor = new Conductor(120);

        int instNo,volume;
        long startTick,length;

        instNo = this.rootLine.getInstNo();
        volume = 124;
        startTick = 0;
        length = 24;


        conductor.setNotes(
            notePich,
            volume,
            startTick,
            instNo,
            length
        );

        conductor.play(0);
    }


    private void pushClipBord(){
        long tmpStartTick,startTick,length;
        int notePich,tmpNotePich;
        String pushWord = "";
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();


        startTick = this.getElectedNotes().get(0).getNoteStartTick();
        length = this.getElectedNotes().get(0).getNoteLength();
        notePich = this.getElectedNotes().get(0).getNotePich();

        //クリップボードに相対位置を記録
        for(NoteRect note:this.getElectedNotes()){
            tmpStartTick = note.getNoteStartTick();
            tmpNotePich = note.getNotePich();
            length = note.getNoteLength();

            

            pushWord    += Long.toString(tmpStartTick - startTick)
                        + ","
                        + Long.toString(length)
                        + ","
                        + Integer.toString(tmpNotePich - notePich)
                        + "\n";
            startTick = tmpStartTick;
            notePich = tmpNotePich;

            content.putString(pushWord);
            clipboard.setContent(content);

        }
    }

    private void pasetNote(){
        Clipboard clipboard = Clipboard.getSystemClipboard();

        double x = this.mouseX;
        double y = this.mouseY;

        x = x - (x % this.quantize);
        y = y - (y % QUAETER_NOTE_HEIGHT);

        double iy = y / QUAETER_NOTE_HEIGHT;

        // クリップボードの値を追加
        int  notePich       = 127 - (int) iy;
        long noteLength     = defNoteLen / BAR_WIDTH_RATE;
        long noteStartTick  = (long) x / BAR_WIDTH_RATE;

        String[] inputLineContens;

        ArrayList<NoteRect> tmpNoteRects = new ArrayList<>();

        for(String stringLine:clipboard.getString().split("\n")){
            // startTick,length,notePich\n
            inputLineContens = stringLine.split(",");

            noteStartTick += Long.parseLong(inputLineContens[0]);

            noteLength = Long.parseLong(inputLineContens[1]);

            notePich =  notePich
                        + Integer.parseInt(inputLineContens[2]);

            System.out.println(noteStartTick);

            NoteRect noteRect = new NoteRect(
                                this,
                                notePich,
                                this.QUAETER_NOTE_HEIGHT,
                                noteLength,
                                noteStartTick
            );
            tmpNoteRects.add(noteRect);
        }
        for(NoteRect noteRect:tmpNoteRects){
            this.addNoteRect(noteRect);
            this.setRect(
                noteRect.getRect(),
                noteRect.getNoteStartTick() * BAR_WIDTH_RATE,
                (127 - noteRect.getNotePich()) * QUAETER_NOTE_HEIGHT
            );
        }
    }

    public void init(TrackLine rootLine){
        this.editStage = new Stage();
        this.editStage.setTitle("editor");
        this.editStage.setWidth(1900);
        this.editStage.setHeight(1040);

        this.rootLine = rootLine;

        this.blackBackGround = new Background(
            new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY
            )
        );
        this.whiteBackGround = new Background(
            new BackgroundFill(
                Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY
            )
        );



        this.notes          = new ArrayList<>();

        this.editSpaseRoot  = new ScrollPane();
        this.editSpaseRoot.setBackground(this.blackBackGround);
        this.editSpaseRoot.setOnScroll(
            event -> scrollEventHandler(event)
        );

        this.editSpaseRoot.setOnKeyPressed(
            event -> keyEventHandler(event)
        );
        this.editSpaseRoot.setOnMouseClicked(
            event -> mouseClickEventHandler(event)
        );

        this.editAndshowRoot= new GridPane();
        this.editAndshowRoot.setBackground(this.blackBackGround);

        this.editSpase      = new AnchorPane();
        this.editSpase.setBackground(this.blackBackGround);
        Tooltip editSpaseTP = new Tooltip();
        editSpaseTP.setText("ダブルクリックで入力");
        Tooltip.install(this.editSpase, editSpaseTP);

        this.pichSupportSp  = new ScrollPane();
        Tooltip pichSupportTP = new Tooltip();
        pichSupportTP.setText("クリックで聞く");
        Tooltip.install(this.pichSupportSp, pichSupportTP);

        this.pichCheckSpase = new AnchorPane();
        this.pichCheckSpase.setBackground(this.blackBackGround);

        this.quaeterChSp    = new ScrollPane();
        this.quaeterChSp.setBackground(this.blackBackGround);

        this.quaeterCkSpase = new AnchorPane();
        this.quaeterCkSpase.setBackground(this.blackBackGround);


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
                Color.DARKBLUE,
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
            tmpLine.setStroke(Color.WHITE);
            tmpLine.setStrokeWidth(0.5);
            tmpLine.setOpacity(0.5);
            if(xPoint % this.QUAETER_NOTE_WIDTH == 0){
                // 4分音符の長さの線を赤くする
                tmpLine.setStroke(Color.RED);
                if(xPoint % this.BAR_WIDTH == 0){
                    tmpLine.setStroke(Color.CYAN);
                    tmpLine.setOpacity(1.0);
                }
            }
            xLine.add(tmpLine);

            if(xPoint % (QUAETER_NOTE_WIDTH) == 0){
                Label tmpLabel = new Label(
                // ラベルの文字．1ベースにするために+1
                    Double.toString(((double)((xPoint / BAR_WIDTH_RATE / 24)) / 4.0) + 1)
                );
                tmpLabel.setPrefHeight(50);
                tmpLabel.setTextFill(Color.MAGENTA);
                AnchorPane.setTopAnchor(tmpLabel, 0.0);
                AnchorPane.setLeftAnchor(tmpLabel,(double)xPoint);
                //tmpLabel.setBorder(border);
                //tmpLabel.setMinWidth(BAR_WIDTH);
                xLabel.add(tmpLabel);

            }
        }
        // 横線を引く
        int yPoint;
        int yPointU;
        for(
            yPointU = 0;
            yPointU <= this.maxRootHeight;
            yPointU += this.QUAETER_NOTE_HEIGHT
        ){
            yPoint = this.maxRootHeight - yPointU;
            Line tmpLine = new Line(
                0, yPointU + this.QUAETER_NOTE_HEIGHT,
                this.maxRootWidth, yPointU + this.QUAETER_NOTE_HEIGHT
            );
            tmpLine.setStroke(Color.WHITE);
            tmpLine.setStrokeWidth(0.5);
            tmpLine.setOpacity(0.5);
            if(
                ((yPoint / this.QUAETER_NOTE_HEIGHT) % 12 == 4)
            ||  ((yPoint / this.QUAETER_NOTE_HEIGHT) % 12 == 7)
            ){
                tmpLine.setStroke(Color.YELLOWGREEN);
                tmpLine.setOpacity(0.7);
            }
            if(yPoint % (this.QUAETER_NOTE_HEIGHT * 12) == 0){
                tmpLine.setStroke(Color.AQUA);
                tmpLine.setOpacity(1.0);
            }
            yLine.add(tmpLine);

            Label tmpLabel = new Label(
                Integer.toString(yPointU / this.QUAETER_NOTE_HEIGHT)
            );
            final int TMP_NOTE_Pich = yPointU / this.QUAETER_NOTE_HEIGHT;
            tmpLabel.setOnMouseClicked(
                event -> quickPlayEvent(
                    event,
                    TMP_NOTE_Pich
                )
            );

            int tmpNoteIn12 = (yPointU / this.QUAETER_NOTE_HEIGHT) % 12;
            if(tmpNoteIn12 == 0){
                tmpLabel.setText(
                    "C" + (yPointU / this.QUAETER_NOTE_HEIGHT) / 12
                );
                tmpLabel.setBackground(this.whiteBackGround);
                tmpLine.setOpacity(0.7);
            }
            if(tmpNoteIn12 % 12 == 4){
                tmpLabel.setText(
                    "E" + (yPointU / this.QUAETER_NOTE_HEIGHT) / 12
                );
                tmpLine.setOpacity(0.7);

            }
            if(tmpNoteIn12 % 12 == 7){
                tmpLabel.setText(
                    "G" + (yPointU / this.QUAETER_NOTE_HEIGHT) / 12
                );
                tmpLine.setOpacity(0.7);
            }
            if(
                    tmpNoteIn12 == 0
                ||  tmpNoteIn12 == 2
                ||  tmpNoteIn12 == 4
                ||  tmpNoteIn12 == 5
                ||  tmpNoteIn12 == 7
                ||  tmpNoteIn12 == 9
                ||  tmpNoteIn12 == 11){
                    tmpLabel.setBackground(this.whiteBackGround);
                    tmpLabel.setMaxWidth(100);
                } else {
                    tmpLabel.setBackground(this.blackBackGround);
                    tmpLabel.setMaxWidth(50);
                }
            tmpLabel.setText(String.format("%5s",tmpLabel.getText()));

            AnchorPane.setTopAnchor(tmpLabel, (double)yPoint);
            // System.out.println(yPoint);
            AnchorPane.setLeftAnchor(tmpLabel,0.0);
            tmpLabel.setPrefWidth(150);
            tmpLabel.setBorder(border);
            tmpLabel.setTextFill(Color.MAGENTA);
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
        this.pichSupportSp.setMinWidth(100);

        this.quaeterChSp.setContent(this.quaeterCkSpase);
        this.quaeterChSp.setHbarPolicy(ScrollBarPolicy.NEVER);

        this.editSpaseRoot.setContent(this.editSpase);

        GridPane.setColumnIndex(this.quaeterChSp  , 1);
        GridPane.setRowIndex(   this.quaeterChSp  , 0);
        GridPane.setColumnIndex(this.pichSupportSp, 0);
        GridPane.setRowIndex(   this.pichSupportSp, 1);
        GridPane.setColumnIndex(this.editSpaseRoot, 1);
        GridPane.setRowIndex(   this.editSpaseRoot, 1);

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
