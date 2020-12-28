package view.trackBox;

import java.util.ArrayList;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.editSpace.editPane.EditSpase;
import view.editSpace.editPane.NoteRect;

public class TrackBox {

    private Rectangle trackBoxRect;

    private EditSpase editSpase;

    private int widthRate;

    private double boxHeight;
    private double boxWidth;

    // tickはまじの値
    // 倍率とか無視
    private double startTick;
    private double endTick;

    private ArrayList<NoteRect> notes;
    // notesが存在する場合
    public TrackBox(double height,ArrayList<NoteRect> notes,int widthRate){
        this.boxHeight = height;
        this.notes = notes;

        double tmpStartTick =   notes.get(0).getNoteStartTick();
        double tmpEndTick   =   tmpStartTick
                                + notes.get(0).getNoteLength();
        for(NoteRect note:notes){
            tmpStartTick = Math.min(
                tmpStartTick,note.getNoteStartTick()
            );
            tmpEndTick = Math.max(
                tmpEndTick, note.getNoteStartTick()
                + note.getNoteLength()
            );
        }
        this.startTick  =   tmpStartTick;
        this.endTick    =   tmpEndTick;

        this.boxWidth = (
                this.endTick - this.startTick
                )*this.widthRate;

        createRect(this.boxWidth,this.boxHeight);
    }
    // notesがない場合
    public TrackBox(double height, double startTck, double width){
        this.editSpase = new EditSpase();
        this.boxHeight = height;
        this.boxWidth = width;
        this.startTick = startTck;
        this.endTick = startTck + width;
        createRect(this.boxWidth, this.boxHeight);
    }

    public void createRect(double boxWidth, double boxHeight){
        this.trackBoxRect = new Rectangle(boxWidth, boxHeight);
        this.trackBoxRect.setFill(Color.BLUE);
        this.trackBoxRect.setStroke(Color.RED);
        this.trackBoxRect.setOnMouseClicked(
            event -> rectClickEventHandler(event)
        );
    }

    public void rectClickEventHandler(MouseEvent event){
        this.editSpase.showStage();
    }

    public Rectangle getRect(){
        return this.trackBoxRect;
    }
}
