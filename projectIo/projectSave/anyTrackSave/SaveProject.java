package projectIo.projectSave.anyTrackSave;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import view.editSpace.editPane.NoteRect;
import view.trackBox.TrackBox;
import view.trackLine.TrackLine;

public class SaveProject {
    private ArrayList<TrackLine> lines;

    public SaveProject(ArrayList<TrackLine> lines){
        this.lines = lines;
    }

    public void saveAll(String fileName){
        String trackId,noteId,notePich,noteLength,noteStartTick;
        String printString;
        ArrayList<String> strings = new ArrayList<>();
        for(int lineId = 0; lineId < lines.size(); lineId++){
            for(TrackBox box:this.lines.get(lineId).getBoxs()){
                for(NoteRect noterect:box.getNotes()){
                    trackId         = Integer.toString(lineId);
                    noteId          = Long.toString(noterect.getNoteId());
                    notePich        = Integer.toString(noterect.getNotePich());
                    noteLength      = Long.toString(noterect.getNoteLength());
                    noteStartTick   = Long.toString(noterect.getNoteStartTick());

                    printString = "{\n"
                            + "\"trackId\":"      + trackId     +    ",\n"
                            + "\"noteId\":"       + noteId      +    ",\n"
                            + "\"notePich\":"     + notePich    +    ",\n"
                            + "\"noteLength\":"   + noteLength  +    ",\n"
                            + "\"noteStartTick\":" + noteStartTick + "\n},";
                    strings.add(printString);
                }
            }
        }
        printStrings(fileName, strings);
    }

    public void printStrings(String outFileName, ArrayList<String> strings){
        PrintWriter fout = null;
        try{
            fout = new PrintWriter(new BufferedWriter(new FileWriter(outFileName)));
            for(String s:strings){
                fout.println(s);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try{
                fout.close();
            }catch(Exception ee){}
        }
    }
}