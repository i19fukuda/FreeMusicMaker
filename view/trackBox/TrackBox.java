package view.trackBox;

import java.util.ArrayList;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.editSpace.editPane.EditSpase;
import view.editSpace.editPane.NoteRect;
import view.trackLine.TrackLine;

public class TrackBox {
    private TrackLine rootLine;

    private Rectangle trackBoxRect;

    private EditSpase editSpase;

    private double boxHeight;
    private double boxWidth;

    private ArrayList<NoteRect> notes;

    public TrackBox(TrackLine rootLine ,double height, double startTck, double width){
        this.rootLine = rootLine;
        this.editSpase  = new EditSpase(this.rootLine);
        this.notes      = this.editSpase.getNotes();
        this.boxHeight  = height;
        this.boxWidth   = width;
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

    public EditSpase getEditRoot(){
        return this.editSpase;
    }

    public ArrayList<NoteRect> getNotes(){
        this.notes = this.editSpase.getNotes();
        return this.notes;
    }
}
