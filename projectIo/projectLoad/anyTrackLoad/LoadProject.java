package projectIo.projectLoad.anyTrackLoad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import view.editSpace.editPane.EditSpase;
import view.editSpace.editPane.NoteRect;
import view.trackLine.TrackLine;

public class LoadProject {
    String fileName;
    int tempo = 0;

    public LoadProject(String filename){
        this.fileName = filename;
    }

    public ArrayList<TrackLine> loadAll(
        String fileName,double lineWidth, double lineHeight
    ){
        long noteId,noteLength,noteStartTick;
        int trackId,notePich;
        int lineSize,tempo;
        int[] insts;

        ArrayList<String> strings = parseString(this.fileName);
        // メタ情報を抜き出す
        int tmpI = 1;
        lineSize = Integer.parseInt(
            strings.get(tmpI).split(":")[1].split(",")[0]
        );
        tmpI++;
        tempo = Integer.parseInt(
            strings.get(tmpI).split(":")[1].split(",")[0]
        );
        this.tempo = tempo;

        // 楽器の登録
        tmpI = 5;
        insts = new int[lineSize];
        String tmpString;
        tmpString = strings.get(tmpI);
        for(int i = 0;i<lineSize;i++){
            insts[i] = Integer.parseInt(
                tmpString.split(":")[1].split(",")[i]
            );
        }
        ArrayList<TrackLine> trackLines = new ArrayList<>();
        for(int i = 0;i<lineSize;i++){
            TrackLine ln = new TrackLine(i, lineHeight, lineWidth);
            trackLines.add(ln);
            ln.setInstNo(insts[i]);
        }

        for(int i=6;i<strings.size();i++){
            if(strings.get(i).equals("{")){
                i+=1;
                trackId = Integer.parseInt(
                    strings.get(i).split(":")[1].split(",")[0]
                );
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
                EditSpase editRoot = trackLines.get(trackId)
                                    .getBoxs().get(0).getEditRoot();
                NoteRect noteRect = editRoot.createNoteRect(
                                    notePich, noteLength, noteStartTick, noteId
                );
                editRoot.addNoteRect(noteRect);
            }
        }
        for(TrackLine line:trackLines){
            //System.out.println("load and set notes:" + line.getBoxs().get(0).getEditRoot().getNotes().size());
            line.getBoxs().get(0).getEditRoot().createAndSetNoteRecs();
            //System.out.println(line.getBoxs().get(0).getEditRoot().getNotes().size());
        }
        return trackLines;
    }

    public int getTempo(){
        int tmpTempo = 120;
        if(this.tempo != 0){
            tmpTempo = this.tempo;
        }
        return tmpTempo;
    }

    public ArrayList<String> parseString(String fileName){
        ArrayList<String> tmpStrings = new ArrayList<String>();

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
