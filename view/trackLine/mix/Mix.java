package view.trackLine.mix;
// 音をまぜまぜできる

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import view.editSpace.editPane.Note;
import view.editSpace.editPane.NoteRect;
import view.trackLine.TrackLine;

public class Mix {
    ArrayList<Note> notes;
    TrackLine line;
    HBox ocRootHB;

    Stage stage;

    int[] octPichs = {-24, -12, 0, 12, 24};

    TextField[] inputFealFields;
    double[] volums;


    public Mix(TrackLine line){
        this.notes = new ArrayList<>();
        this.line = line;

        this.ocRootHB = new HBox();
        this.stage = new Stage();
        this.stage.setTitle("Mix");
        this.stage.setWidth(500);
        this.stage.setHeight(300);
        Scene scene = new Scene(this.ocRootHB);
        stage.setScene(scene);

        inputFealFields = new TextField[5];
        volums = new double[5];

        for(int i=0; i<inputFealFields.length; i++){
            inputFealFields[i] = new TextField();
            inputFealFields[i].setText("0");
            ocRootHB.getChildren().add(inputFealFields[i]);
        }
        for(int i=0; i<volums.length; i++){
            volums[i] = 0.0;
        }
    }

    private void setNotes(){
        for(int i=0; i< this.inputFealFields.length; i++){
            double vol;
            try{
                vol = Double.parseDouble(this.inputFealFields[i].getText());
                if(vol>100){
                    this.showErrorDialog("out of range");
                }
            } catch (NumberFormatException e){
                this.showErrorDialog(e.getMessage());
                vol = 100;
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
                createdNotes.add(
                    new Note(
                        noteId,
                        notePich + this.octPichs[i],
                        noteLength,
                        noteStartTick,
                        (int)(volum * this.volums[i])
                    )
                );
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
