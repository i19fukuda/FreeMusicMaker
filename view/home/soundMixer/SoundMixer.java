package view.home.soundMixer;

import java.util.ArrayList;

import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.home.soundMixer.lineInfo.LineInfo;
import view.trackLine.TrackLine;

// 各トラックのマスターボリュームの調節及びソロ，ミュートの登録
public class SoundMixer {
    private ArrayList<TrackLine> lines;

    private ArrayList<LineInfo> lineInfos;

    private VBox soundMixerRoot;
    private HBox lineInfoRoot;

    ToggleGroup groupSolo;

    public SoundMixer(ArrayList<TrackLine> lines){
        this.lines = lines;
        this.lineInfos = new ArrayList<>();
        this.lineInfoRoot = new HBox();
        this.soundMixerRoot = new VBox();


        for(TrackLine line:lines){
            LineInfo lineInfo = new LineInfo(line);
            this.lineInfos.add(
                lineInfo
            );
            this.lineInfoRoot.getChildren().add(
                        lineInfo.getLineInfoRoot()
            );
        }

        this.soundMixerRoot.getChildren().add(
            this.lineInfoRoot
        );
    }

    public void addLineInfo(TrackLine line){
        LineInfo lineInfo = new LineInfo(line);
        this.lineInfos.add(lineInfo);
        this.lineInfoRoot.getChildren().add(
            lineInfo.getLineInfoRoot()
        );
    }

    public VBox getSoundMixerRoot(){
        return this.soundMixerRoot;
    }
    public HBox getLineInfoRoot(){
        return this.lineInfoRoot;
    }

    // TrackLineにソロの情報をセットする
    public void setSoloTrack(){
        for(LineInfo info:this.lineInfos){
            info.getTrackLine().setIsSolo(info.isSolo());
        }
    }

    // TrackLineにミュートの情報をセットする
    public void setMuteTrack(){
        for(LineInfo info:this.lineInfos){
            info.getTrackLine().setIsMute(info.isMute());
        }
    }

    public ArrayList<TrackLine> getLines(){
        return this.lines;
    }

    public void removeLineInfo(int trackId){
        TrackLine targetLine;
        LineInfo targetInfo;
        remove: for(TrackLine line:this.lines){
            if(line.getTrackId() == trackId){
                targetLine = line;
                this.lines.remove(line);
                for(LineInfo info:this.lineInfos){
                    if(info.getTrackLine() == targetLine){
                        targetInfo = info;

                        this.lineInfoRoot.getChildren().remove(
                            targetInfo.getLineInfoRoot()
                        );

                        break remove;
                    }
                }
            }
        }
    }
}
