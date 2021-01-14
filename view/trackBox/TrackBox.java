package view.trackBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import view.editSpace.editPane.EditSpase;
import view.editSpace.editPane.NoteRect;
import view.trackLine.TrackLine;

public class TrackBox {
    private TrackLine rootLine;

    private Rectangle trackBoxRect;

    private EditSpase editSpase;

    private double boxHeight;
    private double boxWidth;

    private ToggleGroup groupBoxColor;

    private ArrayList<NoteRect> notes;

    private ContextMenu colorChooser;

    public TrackBox(TrackLine rootLine ,double height, double startTck, double width){
        this.rootLine = rootLine;
        this.editSpase  = new EditSpase(this.rootLine);
        this.notes      = this.editSpase.getNotes();
        this.boxHeight  = height;
        this.boxWidth   = width;
        this.groupBoxColor = new ToggleGroup();

        this.colorChooser = this.createColorChooser();


        createRect(this.boxWidth, this.boxHeight);
    }

    public void createRect(double boxWidth, double boxHeight){
        this.trackBoxRect = new Rectangle(boxWidth, boxHeight);
        this.trackBoxRect.setFill(Color.DARKRED);
        this.trackBoxRect.setStroke(Color.RED);
        this.trackBoxRect.setOnMouseClicked(
            event -> rectClickEventHandler(event)
        );

        Tooltip toolTip = new Tooltip();
        toolTip.setText(
            "右クリックしてエディターを開く\n"
            + "右クリックで色を変更する"
        );

        Tooltip.install(this.trackBoxRect, toolTip);
    }

    public void rectClickEventHandler(MouseEvent event){
        if(event.getButton() == MouseButton.PRIMARY){
            this.editSpase.showStage();
        } else if(event.getButton() == MouseButton.SECONDARY){
            this.colorChooser.show(this.trackBoxRect,event.getX(), event.getY());
        }
    }

    public Rectangle getRect(){
        return this.trackBoxRect;
    }

    public EditSpase getEditRoot(){
        return this.editSpase;
    }

    public ArrayList<NoteRect> getNotes(){
        this.notes = this.editSpase.getNotes();
        return this.notes;
    }

    private ContextMenu createColorChooser(){
        ContextMenu contextMenu = new ContextMenu();
        Map<String, Color> colorMap = new HashMap<String, Color>(){
            {
                put("AQUA",             Color.AQUA);
                put("BLUE",             Color.BLUE);
                put("DARKBLUE",         Color.DARKBLUE);
                put("BROWN",            Color.BROWN);
                put("DARKSLATEGREY",    Color.DARKSLATEGREY);
                put("GREEN",            Color.GREEN);
                put("MAGENTA",          Color.MAGENTA);
                put("PURPLE",           Color.PURPLE);
                put("YELLOW",           Color.YELLOW);
                put("DARKRED",          Color.DARKRED);
                put("RED",              Color.RED);
            }
        };
        for(String colorSt:colorMap.keySet()){
            Label colorSample = new Label("  ");
            colorSample.setBackground(
                new Background(
                    new BackgroundFill(
                        colorMap.get(colorSt),
                        CornerRadii.EMPTY ,
                        Insets.EMPTY
                    )
                )
            );
            RadioMenuItem item = new RadioMenuItem(colorSt, colorSample);
            item.setToggleGroup(this.groupBoxColor);

            item.setOnAction(event -> changeColorEvent(event, colorMap.get(colorSt)));

            contextMenu.getItems().add(item);
        }

        return contextMenu;
    }

    private void changeColorEvent(ActionEvent event, Color color){
        this.trackBoxRect.setFill(color);
    }
}
