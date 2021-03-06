package view.trackLine.ctrlBox;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import view.trackLine.ElectInst;
import view.trackLine.TrackLine;
import view.trackLine.mix.Mix;

/**
 * @author i19fukuda1k
 * トラックのコントロールに関する部品を提供する。
 */
public class Controls {
    VBox ctrlRoot;
    TrackLine trackLine;


    Mix mix;
    ElectInst electInst;

    TextField trackNameFl;
    Button mixButton;
    Slider masterVolSlider;
    MenuBar electInstMenubar;

    Background blackBackground;

    /**
     * コンストラクタ
     * @param trackLine 親となるトラックライン
     * @see view.trackLine
     */
    public Controls(TrackLine trackLine){
        this.blackBackground = new Background(
            new BackgroundFill(
                Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY
            )
        );

        this.trackLine =  trackLine;
        this.ctrlRoot = new VBox();

        createMixButton();
        createTrackNameFl();
        createMasterVolSlider();
        createElectInstMenubar();

        this.ctrlRoot.getChildren().addAll(
            this.trackNameFl,
            this.electInstMenubar,
            this.mixButton,
            this.masterVolSlider
        );
    }

    /**
     * ルートとなるコントロールを返す
     * @return コントローラ
     */
    public VBox getctrlBoxRoot(){
        return this.ctrlRoot;
    }

    private void createMixButton(){
        this.mix = new Mix(this.trackLine);
        this.mixButton = new Button("Mix");
        this.mixButton.setBackground(this.blackBackground);

        Tooltip mixButtonTT = new Tooltip("クリックして音をミックス");
        Tooltip.install(this.mixButton, mixButtonTT);

        this.mixButton.setOnMouseClicked(
            event -> mix.showMixer()
        );
    }
    public Button getMixButton(){
        return this.mixButton;
    }
    public Mix getMix(){
        return this.mix;
    }

    private void createTrackNameFl(){
        this.trackNameFl = new TextField(
                                "track "
                                + Integer.toString(
                                    this.trackLine.getTrackId()
                                )
                            );
        Tooltip trackNameFlTT = new Tooltip("トラック名を記入");
        Tooltip.install(this.trackNameFl, trackNameFlTT);
        // this.trackNameFl.setBackground(this.blackBackground);
    }
    public TextField getTrackNameFl(){
        return this.trackNameFl;
    }

    private void createMasterVolSlider(){
        this.masterVolSlider = new Slider();
        this.masterVolSlider.setMin(0);
        this.masterVolSlider.setMax(100);
        this.masterVolSlider.setValue(100);
        this.masterVolSlider.setMinWidth(400);
        this.masterVolSlider.setShowTickLabels(true);
        this.masterVolSlider.setShowTickMarks(true);
        this.masterVolSlider.setOnMouseReleased(
            event -> setVolEventHandler(event)
        );
    }
    private void setVolEventHandler(Event event){
        double inputVol = this.masterVolSlider.getValue();
        this.trackLine.setMasterVol(inputVol * 0.01);
    }
    /**
     * スライダーにマスターボリュームをセットする。
     * @param value 0-127の音量
     */
    public void setMasterVol(double value){
        if(value>127 || value < 0){
            showErrorDialog("setMasterVol : out of range(mVol):" + value);
            value = 127;
        }
        this.masterVolSlider.setValue(100 * value / 127);
    }

    private void createElectInstMenubar(){
        this.electInst = new ElectInst();
        this.electInstMenubar = electInst.getMenuBar();
    }

    /**
     * 楽器選択のメニューバーを返す
     * @return メニューﾊﾞー
     */
    public MenuBar getElectInstMenubar(){
        return this.electInstMenubar;
    }
    /**
     * 選択された楽器を返す
     * @return 選択された楽器
     */
    public ElectInst getElectInst(){
        return this.electInst;
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
