package projectIo.projectLoad.oneTrackLoad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import view.editSpace.editPane.EditSpase;
import view.editSpace.editPane.NoteRect;

public class LoadProject {
    private EditSpase root;
    private String projectName;
    private ArrayList<NoteRect> notes;

    public LoadProject(String projectName){;
        this.projectName = projectName;
        this.notes = new ArrayList<>();
    }

    public ArrayList<NoteRect> loadNoteRect(EditSpase root, int noteHeight){
        this.root = root;
        ArrayList<String> strings = this.parseString(this.projectName);
        long noteId,noteLength,noteStartTick;
        int notePich;
        for(String s:strings){
            System.out.println(s);
        }
        for(int i=0;i<strings.size();i++){
            if(strings.get(i).equals("{")){
                i+=1;
                noteId = Long.parseLong(
                    strings.get(i).split(":")[1].split(",")[0]
                );
                i+=1;
                notePich = Integer.parseInt(
                    strings.get(i).split(":")[1].split(",")[0]
                );
                i+=1;
                noteLength = Long.parseLong(
                    strings.get(i).split(":")[1].split(",")[0]
                );
                i+=1;
                noteStartTick = Long.parseLong(
                    strings.get(i).split(":")[1].split(",")[0]
                );
                NoteRect rect = new NoteRect(
                    this.root,
                    notePich,
                    noteHeight,noteLength,
                    noteStartTick
                );
                rect.setNoteId(noteId);
                this.notes.add(rect);
            }
        }
        return this.notes;
    }
    public ArrayList<String> parseString(String fileName){
        ArrayList<String> tmpStrings = new ArrayList<>();

        BufferedReader fin = null;
        try{
            fin = new BufferedReader(new FileReader(fileName));
            String line = fin.readLine();
            while(line != null){
                tmpStrings.add(line);
                line = fin.readLine();
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
            tmpStrings = new ArrayList<>();
        }finally{
            try{
                fin.close();
            }catch(Exception ee){}
        }
        return tmpStrings;
    }
}
