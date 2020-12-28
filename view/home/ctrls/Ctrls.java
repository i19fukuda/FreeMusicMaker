package view.home.ctrls;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Ctrls {
    HBox ctrlRoot;

    VBox inInstRoot;
    TextField inInstField;

    VBox inTempoRoot;
    TextField inTempoField;

    VBox playRoot;
    Button playButton;

    public Ctrls(){
        this.ctrlRoot       = new HBox();

        this.inInstRoot     = new VBox();
        this.inInstField    = new TextField("0");
        Label instLabel     = new Label(" 楽器を入力 ");
        this.inInstRoot.getChildren().addAll(
            instLabel,this.inInstField
        );

        this.inTempoRoot    = new VBox();
        this.inTempoField   = new TextField("0");
        Label tempoLabel    = new Label("0");
        this.inTempoRoot.getChildren().addAll(
            tempoLabel, this.inTempoField
        );

        this.playRoot       = new VBox();
        this.playButton     = new Button("play");
    }
}
