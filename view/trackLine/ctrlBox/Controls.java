package view.trackLine.ctrlBox;

import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import view.trackLine.ElectInst;
import view.trackLine.TrackLine;
import view.trackLine.mix.Mix;

public class Controls {
    VBox ctrlRoot;
    TrackLine trackLine;


    Mix mix;
    ElectInst electInst;

    TextField trackNameFl;
    Button mixButton;
    Slider masterVolSlider;
    MenuBar electInstMenubar;

    public Controls(TrackLine trackLine){
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

        this.ctrlRoot.setBorder(
            new Border(
                new BorderStroke(
                    Color.BLACK,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT
                )
            )
        );
    }

    public VBox getctrlBoxRoot(){
        return this.ctrlRoot;
    }

    private void createMixButton(){
        this.mix = new Mix(this.trackLine);
        this.mixButton = new Button("Mix");
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
                            Integer.toString(
                                this.trackLine.getTrackId()
                            )
                            );
    }
    public TextField getTrackNameFl(){
        return this.trackNameFl;
    }

    private void createMasterVolSlider(){
        this.masterVolSlider = new Slider();
        this.masterVolSlider.setMin(0);
        this.masterVolSlider.setMax(100);
        this.masterVolSlider.setValue(100);
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
    public Slider getMasterVolSlider(){
        return this.getMasterVolSlider();
    }
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
    public MenuBar getElectInstMenubar(){
        return this.electInstMenubar;
    }
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
