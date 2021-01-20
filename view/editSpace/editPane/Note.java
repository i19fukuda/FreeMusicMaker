package view.editSpace.editPane;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * ただの音符の情報
 * 表示することはない
 * @author i19fukuda1k
 */
public class Note {
    private long noteId, noteLength, noteStartTick;
    private int  notePich, volum;

    public Note(
        long    noteId,
        int     notePich,
        long    noteLength,
        long    noteStartTick,
        int     volum
    ){
        this.setNoteId(noteId);
        this.setNotePich(notePich);
        this.noteLength = noteLength;
        this.setNoteStartTick(noteStartTick);
        this.setVolum(volum);
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

    public int getVolum(){
        return this.volum;
    }

    public void setVolum(int volum){
        if(volum > 127){
            showErrorDialog("out of range");
        }else {
            this.volum = volum;
        }
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
