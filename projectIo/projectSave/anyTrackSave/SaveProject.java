package projectIo.projectSave.anyTrackSave;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import view.editSpace.editPane.NoteRect;
import view.trackBox.TrackBox;
import view.trackLine.TrackLine;

/**
 * @author i19fukuda1k
 * プロジェクトを保存するメソッド
 * @see projectIo.projectLoad.anyTrackLoad
 */
public class SaveProject {
    private ArrayList<TrackLine> lines;
    /**
     * @param lines ルートのすべてのトラックライン
     */
    public SaveProject(ArrayList<TrackLine> lines){
        this.lines = lines;
    }
    /**
     * プロジェクトの情報をすべて保存するメソッド
     * @param file 出力先ファイル
     * @param tempo 保存するテンポ
     */
    public void saveAll(File file, int tempo){
        ArrayList<String> strings = new ArrayList<>();
        String printString;

        //最初にメタ情報を埋め込む
        String lineSize, tempoSt;
        lineSize = Integer.toString(lines.size());
        tempoSt   = Integer.toString(tempo);

        printString = "meta:{\n"
                    + "\"lineSize\":"   +   lineSize    +   ",\n"
                    + "\"tempo\":"      +   tempoSt     +   ",\n"
                    + "},\n";
        strings.add(printString);

        //楽器を配列で書き出す
        printString = "insts:";
        int inst;
        for(TrackLine line:lines){
            inst = line.getInstNo();
            printString += Integer.toString(inst);
            printString += ",";
        }
        printString += "\n";

        strings.add(printString);

        //トラックごとのマスターボリュームを書き出す
        printString = "volums:";
        int trackMVo;
        for(TrackLine line:lines){
            trackMVo = line.getMasterVol();
            printString += Integer.toString(trackMVo);
            printString += ",";
        }
        printString += "\n";
        strings.add(printString);


        String trackId,noteId,notePich,noteLength,noteStartTick;
        for(int lineId = 0; lineId < lines.size(); lineId++){
            for(TrackBox box : this.lines.get(lineId).getBoxs()){
                for(NoteRect noterect : box.getNotes()){
                    trackId         = Integer.toString(
                                        lineId
                                        );
                    noteId          = Long.toString(
                                        noterect.getNoteId()
                                        );
                    notePich        = Integer.toString(
                                        noterect.getNotePich()
                                        );
                    noteLength      = Long.toString(
                                        noterect.getNoteLength()
                                        );
                    noteStartTick   = Long.toString(
                                        noterect.getNoteStartTick()
                                        );

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
        printStrings(file, strings);
    }

    private void printStrings(File outFile, ArrayList<String> strings){
        PrintWriter fout = null;
        try{
            fout = new PrintWriter(new BufferedWriter(new FileWriter(outFile)));
            for(String s : strings){
                fout.println(s);
            }
        }catch(Exception e){
            showErrorDialog(e.getMessage());
            System.out.println(e.getMessage());
        }finally{
            try{
                fout.close();
            }catch(Exception ee){}
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