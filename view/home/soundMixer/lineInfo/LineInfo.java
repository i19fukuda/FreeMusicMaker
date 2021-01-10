package view.home.soundMixer.lineInfo;

import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import view.trackLine.TrackLine;

public class LineInfo {
    private TrackLine line;

    private VBox lineInfoRootVB;
    private VBox soloAndMuteVB;

    private Label trackNameLb;

    private ToggleButton soloButton;
    private ToggleButton muteButton;

    private ToggleGroup groupSoM;

    public LineInfo(TrackLine line){
        this.line = line;


        String trackName = line.getTrackName();
        this.trackNameLb = new Label(trackName);


        this.soloButton = new ToggleButton("S");
        this.soloButton.setToggleGroup(this.groupSoM);

        this.muteButton = new ToggleButton("M");
        this.muteButton.setToggleGroup(this.groupSoM);

        this.lineInfoRootVB = new VBox();
        this.soloAndMuteVB = new VBox();

        this.groupSoM = new ToggleGroup();

        this.soloAndMuteVB.getChildren().addAll(
            this.soloButton,this.muteButton
        );



        this.lineInfoRootVB.getChildren().addAll(
            this.trackNameLb, this.soloAndMuteVB
        );

    }

    public boolean isSolo(){
        boolean flag = this.soloButton.isSelected();
        return flag;
    }

    public boolean isMute(){
        boolean flag = this.muteButton.isSelected();
        return flag;
    }

    public VBox getLineInfoRoot(){
        return this.lineInfoRootVB;
    }

    public TrackLine getTrackLine(){
        return this.line;
    }
}
