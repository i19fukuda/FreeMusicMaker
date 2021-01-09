package view.trackLine.mix;
// 音をまぜまぜできる

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.editSpace.editPane.Note;
import view.editSpace.editPane.NoteRect;
import view.trackLine.TrackLine;

public class Mix {
    ArrayList<Note> notes;
    TrackLine line;
    HBox ocRootHB;

    Stage stage;

    int[] octPichs = {-36, -24, -12, 0, 12, 24, 36, 48};
    VBox[] octVB = new VBox[octPichs.length];

    TextField[] inputFealFields;
    double[] volums;


    public Mix(TrackLine line){
        this.notes = new ArrayList<>();
        this.line = line;

        this.ocRootHB = new HBox();
        this.stage = new Stage();
        this.stage.setTitle("Mix");
        this.stage.setWidth(800);
        this.stage.setHeight(300);
        Scene scene = new Scene(this.ocRootHB);
        stage.setScene(scene);

        inputFealFields = new TextField[this.octPichs.length];
        volums = new double[this.octPichs.length];

        for(int i=0; i<inputFealFields.length; i++){
            Label tmpLb = new Label(
                Integer.toString(this.octPichs[i] / 12)
                + "オクターブ"
            );
            inputFealFields[i] = new TextField();
            inputFealFields[i].setText("0");
            inputFealFields[i].setOnAction(
                event -> actionEventHandler(event)
            );

            this.octVB[i] = new VBox();
            this.octVB[i].getChildren().addAll(tmpLb, inputFealFields[i]);
            this.ocRootHB.getChildren().add(this.octVB[i]);
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
                if(vol>=100){
                    this.showErrorDialog("out of range:" + vol);
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

    private void actionEventHandler(ActionEvent event){
        for(TextField field:this.inputFealFields){
            try{
                double imput = Double.parseDouble(field.getText());
                if(
                    imput > 100
                    || imput<0
                ){
                    field.setText("0-100の数値");
                }
            } catch (NumberFormatException e){
                showErrorDialog(e.getMessage());
                field.setText("0-100の数値");
            }
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
