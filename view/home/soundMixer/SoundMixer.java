package view.home.soundMixer;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import view.home.soundMixer.lineInfo.LineInfo;
import view.trackLine.TrackLine;

/**
 * @author i19fukuda1k
 * 各トラックのマスターボリュームの調節およびソロ、ミュートの登録
 */
public class SoundMixer {
    private ArrayList<TrackLine> lines;

    private ArrayList<LineInfo> lineInfos;

    private ScrollPane soundMixerRoot;
    private HBox lineInfoRoot;

    private Background blacBackground;

    ToggleGroup groupSolo;

    /**
     * @param lines すべてのトラックライン
     */
    public SoundMixer(ArrayList<TrackLine> lines){
        this.blacBackground = new Background(
            new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)
        );

        this.lines = lines;
        this.lineInfos = new ArrayList<>();

        this.lineInfoRoot = new HBox();
        HBox.setHgrow(this.lineInfoRoot, Priority.ALWAYS);
        this.lineInfoRoot.setPrefWidth(2000);
        this.lineInfoRoot.setBackground(this.blacBackground);
        this.lineInfoRoot.setBorder(
            new Border(
                new BorderStroke(
                    Color.RED,
                    BorderStrokeStyle.DASHED,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT
                    )
            )
        );
        this.soundMixerRoot = new ScrollPane();
        this.soundMixerRoot.setBackground(this.blacBackground);
        this.soundMixerRoot.setMinHeight(100);
        HBox.setHgrow(this.soundMixerRoot, Priority.ALWAYS);
        this.soundMixerRoot.setPrefWidth(2000);

        this.groupSolo = new ToggleGroup();


        for(TrackLine line:lines){
            LineInfo lineInfo = new LineInfo(line, this.groupSolo);
            this.lineInfos.add(
                lineInfo
            );
            this.lineInfoRoot.getChildren().add(
                        lineInfo.getLineInfoRoot()
            );
        }

        this.soundMixerRoot.setContent(
            this.lineInfoRoot
        );
    }

    /**
     * 新しく生成されたトラックラインに対してその情報をセットする
     * @param line 新しく生成されたトラック
     */
    public void addLineInfo(TrackLine line){
        LineInfo lineInfo = new LineInfo(line, this.groupSolo);
        this.lineInfos.add(lineInfo);
        this.lineInfoRoot.getChildren().add(
            lineInfo.getLineInfoRoot()
        );
    }
    /**
     * サウンドミキサーのルートを返す
     * @return サウンドミキサーのルート
     */
    public ScrollPane getSoundMixerRoot(){
        return this.soundMixerRoot;
    }
    public HBox getLineInfoRoot(){
        return this.lineInfoRoot;
    }

    /**
     * トラックラインにソロの状態をセットする
     */
    public void setSoloTrack(){
        for(LineInfo info:this.lineInfos){
            info.getTrackLine().setIsSolo(info.isSolo());
        }
    }

    /**
     * トラックラインにミュートの状態をセットする
     */
    public void setMuteTrack(){
        for(LineInfo info:this.lineInfos){
            info.getTrackLine().setIsMute(info.isMute());
        }
    }
    /**
     * 保持しているトラックラインをすべて返す
     * @return 保持しているすべてのトラックライン
     */
    public ArrayList<TrackLine> getLines(){
        return this.lines;
    }
    /**
     * トラックラインの削除の際のLineInfoの削除
     * @param line 削除するトラックライン
     * @param indexOf 削除するトラックラインのID
     */
    public void removeLineInfo(TrackLine line, int indexOf){
        this.lines.remove(line);
        this.lineInfoRoot.getChildren().remove(indexOf);
        this.lines.remove(indexOf);
    }
}
