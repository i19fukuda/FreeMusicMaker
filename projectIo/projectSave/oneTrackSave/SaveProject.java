package projectIo.projectSave.oneTrackSave;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import view.editSpace.editPane.NoteRect;

public class SaveProject {
    private ArrayList<NoteRect> noterects;
    public SaveProject(ArrayList<NoteRect> noteRects){
        this.noterects = noteRects;
    }

    public void saveAll(String filename){
        String noteId,notePich,noteLength,noteStartTick;
        String printString;

        ArrayList<String> strings = new ArrayList<>();
        for(NoteRect noterect:this.noterects){
            noteId          = Long.toString(noterect.getNoteId());
            notePich        = Integer.toString(noterect.getNotePich());
            noteLength      = Long.toString(noterect.getNoteLength());
            noteStartTick   = Long.toString(noterect.getNoteStartTick());

            printString =   "{\n"
                            + "\"noteId\":"       + noteId      +    ",\n"
                            + "\"notePich\":"     + notePich    +    ",\n"
                            + "\"noteLength\":"   + noteLength  +    ",\n"
                            + "\"noteStartTick\":" + noteStartTick + "\n},";
            strings.add(printString);
        }
        this.printStrings(filename, strings);

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
