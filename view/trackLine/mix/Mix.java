package view.trackLine.mix;
// 音をまぜまぜできる

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import view.editSpace.editPane.Note;
import view.editSpace.editPane.NoteRect;
import view.trackLine.TrackLine;

public class Mix {
    ArrayList<Note> notes;
    TrackLine line;
    VBox ocRootHB;

    Stage stage;

    int[] octPichs = {-36, -24, -12, 0, 12, 24, 36, 48};
    VBox[] octVB = new VBox[octPichs.length];

    Slider[] intputSliders;

    double[] volums;

    private Background darckGrayBackGround;


    public Mix(TrackLine line){
        this.notes = new ArrayList<>();
        this.line = line;

        this.darckGrayBackGround = new Background(
            new BackgroundFill(
                Color.DARKSLATEGRAY,
                CornerRadii.EMPTY,
                Insets.EMPTY
            )
        );

        this.ocRootHB = new VBox();
        this.ocRootHB.setBackground(this.darckGrayBackGround);
        this.stage = new Stage();
        this.stage.setTitle("Mix");
        this.stage.setWidth(800);
        this.stage.setHeight(500);
        Scene scene = new Scene(this.ocRootHB);
        stage.setScene(scene);

        this.intputSliders = new Slider[this.octPichs.length];
        volums = new double[this.octPichs.length];

        for(int i=0; i<this.intputSliders.length; i++){
            Label tmpLb = new Label(
                Integer.toString(this.octPichs[i] / 12)
                + "オクターブ"
            );
            tmpLb.setTextFill(Color.WHITESMOKE);
            intputSliders[i] = new Slider();
            intputSliders[i].setMin(0);
            intputSliders[i].setMax(100);
            intputSliders[i].setShowTickLabels(true);
            intputSliders[i].setShowTickMarks(true);
            intputSliders[i].setValue(0);
            this.octVB[i] = new VBox();
            this.octVB[i].getChildren().addAll(tmpLb, intputSliders[i]);
            this.ocRootHB.getChildren().add(this.octVB[i]);
        }

        for(int i=0; i<volums.length; i++){
            volums[i] = 0.0;
        }
    }

    private void setNotes(){
        for(int i=0; i< this.intputSliders.length; i++){
            double vol;
            vol = this.intputSliders[i].getValue();
            if(vol>100){
                this.showErrorDialog("Mix setNotes : out of range : " + vol);
            }

            this.volums[i] = vol * 0.01;
        }

        long noteId, noteLength, noteStartTick;
        int  notePich, volum;
        ArrayList<Note> createdNotes = new ArrayList<>();
        for(NoteRect noteRect:this.line.getBoxs().get(0).getNotes()){
            noteId          = noteRect.getNoteId();
            noteLength      = noteRect.getNoteLength();
            noteStartTick   = noteRect.getNoteStartTick();
            notePich        = noteRect.getNotePich();
            volum           = noteRect.getVolum();

            for(int i = 0; i< this.octPichs.length; i++){
                notePich = notePich + this.octPichs[i];
                volum = (int) (volum * this.volums[i]);
                if(
                    notePich <128
                    && notePich>=0
                    && volum <128
                ){
                    createdNotes.add(
                    new Note(
                        noteId,
                        notePich,
                        noteLength,
                        noteStartTick,
                        volum
                        )
                    );
                }
            }
        }

        for(Note note:createdNotes){
            this.notes.add(note);
        }
    }

    public ArrayList<Note> getNotes(){
        this.setNotes();
        return this.notes;
    }

    public void showMixer(){
        stage.show();
    }

    private void showErrorDialog(String errorMessage){
        Alert errorDialog = new Alert(
                            AlertType.ERROR,
                            errorMessage,
                            ButtonType.CLOSE
                            );
        errorDialog.showAndWait();
    }
}
