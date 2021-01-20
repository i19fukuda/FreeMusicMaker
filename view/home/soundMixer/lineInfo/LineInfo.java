package view.home.soundMixer.lineInfo;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import view.trackLine.TrackLine;
/**
 * @author i19fukuda1k
 * トラックラインのミュートやソロなどの情報を持つクラス
 */
public class LineInfo {
    private TrackLine line;

    private VBox lineInfoRootVB;
    private VBox soloAndMuteVB;

    private Label trackNameLb;

    private ToggleButton soloButton;
    private ToggleButton muteButton;

    private ToggleGroup groupSolo;
    private ToggleGroup groupSoM;

    private Background blackBackground;
    /**
     * @param line 親のトラックライン
     * @param soloGroup ソロのトグルグループ
     */
    public LineInfo(TrackLine line, ToggleGroup soloGroup){
        this.blackBackground = new Background(
            new BackgroundFill(
                Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY
            )
        );

        this.line = line;


        String trackName = line.getTrackName();
        this.trackNameLb = new Label(trackName);

        this.groupSolo = soloGroup;

        this.soloButton = new ToggleButton("S");
        //this.soloButton.setBackground(this.blackBackground);
        this.soloButton.setToggleGroup(this.groupSolo);

        this.muteButton = new ToggleButton("M");
        //this.muteButton.setBackground(this.blackBackground);
        this.muteButton.setToggleGroup(this.groupSoM);

        this.lineInfoRootVB = new VBox();
        this.lineInfoRootVB.setBorder(
            new Border(
                new BorderStroke(
                    Color.RED,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT
                    )
            )
        );
        this.lineInfoRootVB.setBackground(this.blackBackground);

        this.soloAndMuteVB = new VBox();

        this.groupSoM = new ToggleGroup();

        this.soloAndMuteVB.getChildren().addAll(
            this.soloButton,this.muteButton
        );



        this.lineInfoRootVB.getChildren().addAll(
            this.trackNameLb, this.soloAndMuteVB
        );

    }

    /**
     * ソロかどうかを返す。
     * @return true ならソロ
     */
    public boolean isSolo(){
        boolean flag = this.soloButton.isSelected();
        return flag;
    }
    /**
     * ミュートかどうかを返す
     * @return trueならミュート
     */
    public boolean isMute(){
        boolean flag = this.muteButton.isSelected();
        return flag;
    }

    /**
     * ルートを返す
     * @return ルート
     */
    public VBox getLineInfoRoot(){
        return this.lineInfoRootVB;
    }
    /**
     * 保持しているトラックを返す
     * @return 保持しているトラックライン
     */
    public TrackLine getTrackLine(){
        return this.line;
    }
}
