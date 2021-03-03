package projectIo.projectLoad.anyTrackLoad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import view.editSpace.editPane.EditSpase;
import view.editSpace.editPane.NoteRect;
import view.trackLine.TrackLine;
/**
 * プロジェクトの読み込みをする機能
 * @author i19fukuda1k
 */
public class LoadProject {
    private File file;
    private int tempo = 0;
    /**
     * @param file 読み込むファイル
     */
    public LoadProject(File file){
        this.file = file;
    }
    /**
     * 指定されているファイルのすべての情報を読み込むメソッド
     * @param lineWidth トラックラインの幅
     * @param lineHeight トラックラインの高さ
     * @return 情報を読み込んだトラックライン
     */
    public ArrayList<TrackLine> loadAll(
        double lineWidth, double lineHeight
    ){
        long noteId,noteLength,noteStartTick;
        int trackId,notePich;
        int lineSize,tempo;
        int[] insts;

        ArrayList<String> strings = parseString(this.file);
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
        tmpI += 3;
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


        int[] mVols = new int[lineSize];
        tmpI += 2;
        tmpString = strings.get(tmpI);
        for(int i = 0;i<lineSize;i++){
            mVols[i] = Integer.parseInt(
                tmpString.split(":")[1].split(",")[i]
            );
        }
        for(int i = 0;i<lineSize;i++){
            trackLines.get(i).setMasterVol(mVols[i]);
        }

        final int NOTE_INFO_START = 9;

        for(int i=NOTE_INFO_START;i<strings.size();i++){
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
                                        notePich,
                                        noteLength,
                                        noteStartTick,
                                        noteId
                                    );
                editRoot.addNoteRect(noteRect);
            }
        }
        for(TrackLine line:trackLines){
            line.getBoxs().get(0).getEditRoot().createAndSetNoteRecs();
        }
        return trackLines;
    }
    /**
     * テンポを返す
     * @return テンポ
     */
    public int getTempo(){
        int tmpTempo = 120;
        if(this.tempo != 0){
            tmpTempo = this.tempo;
        }
        return tmpTempo;
    }

    private ArrayList<String> parseString(File file){
        ArrayList<String> tmpStrings = new ArrayList<String>();

        BufferedReader fin = null;
        try{
            fin = new BufferedReader(new FileReader(file));
            String line = fin.readLine();
            while(line != null){
                tmpStrings.add(line);
                line = fin.readLine();
            }
        } catch(Exception e){
            showErrorDialog(e.getMessage());
            System.out.println(e.getMessage());
            tmpStrings = new ArrayList<>();
        }finally{
            try{
                fin.close();
            }catch(Exception ee){}
        }
        return tmpStrings;
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
