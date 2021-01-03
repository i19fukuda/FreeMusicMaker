package view.trackLine;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public class ElectInst {
    MenuBar menubar;
    Menu rootM;
    String[] instNames;
    RadioMenuItem[] radioMenuItems;
    ToggleGroup groupElect;
    boolean isPercussion;
    int electedInstNo;

    Menu            mPiano,     mChromaticPercussion,
                    mOrgan,     mGuitar,
                    mBass,      mStrings,
                    mEnsemble,  mBrass,
                    mReed,      mPipe,
                    mSynthLead, mSynthPad,
                    mSynthEffects,mEthnic,
                    mPercussive,mSoundEffects;
    // piano [1-8]
    RadioMenuItem   iAcPiano,       iBrPiano,
                    iEGPiano,       iHtPiano,
                    iElPiano1,      iEpiano2,
                    iHpSichord,     iClavi;
    // Chromatic Percussion [9-16]
    RadioMenuItem   iCelesta,       iGlockenspiel,
                    iMusicalBox,    iVibph,
                    iMarimba,       iXyloph,
                    iTubularBell,   iDulcimer;
    // Organ [17-24]
    RadioMenuItem   iDraOrg,        iPercssOrg,
                    iRockOrg,       iChurchOrg,
                    iReedOrg,       iAccordion,
                    iHarmonica,     TangoAcord;
    // Guitar [25-32]
    RadioMenuItem   iAcGuitN,       iAcGuitS,
                    iELGuitJ,       iELGuitC,
                    iELGuitM,       iODGuit,
                    iDistGuit,      iGuitH;
    // Bass [33-40]
    RadioMenuItem   iACBass,        iELBassF,
                    iELBassP,       iFRTBass,
                    iStBass1,       iSTBass2,
                    iSyBass1,       iSyBass2;
    // Strings [41-48]
    RadioMenuItem   iViolin,        iViola,
                    iCello,         iDBBass,
                    iTRSt,          iPizzict,
                    iOrHarp,        iTimpani;
    // Ensemble
    public ElectInst(){
        this.menubar = new MenuBar();
        this.groupElect = new ToggleGroup();
        this.rootM = new Menu("inst");

        setInstNames();
        setRadioMenuItems();

        this.mPiano                 = new Menu("pianos");
        this.mChromaticPercussion   = new Menu("Chromatic Percussion");

        this.mPiano.getItems().addAll(this.radioMenuItems[0],this.radioMenuItems[1],this.radioMenuItems[2]);

        this.rootM.getItems().addAll(this.mPiano,this.mChromaticPercussion);
        this.menubar.getMenus().addAll(this.rootM);
    }

    private void electedActionHandler(ActionEvent event,RadioMenuItem radioMenuItem){
        //System.out.println("cliecked!!");
        for(int i=0;i<this.radioMenuItems.length;i++){
            if(radioMenuItem == this.radioMenuItems[i]){
                System.out.println(this.instNames[i]);
                this.electedInstNo = i;
                this.rootM.setText(this.instNames[i]);
            }
        }
    }

    public MenuBar getMenuBar(){
        return this.menubar;
    }
    public int getInstNo(){
        return this.electedInstNo;
    }
    public void setInstNo(int instNo){
        this.electedInstNo = instNo;
    }

    private void setInstNames(){
        String[] tmpStringNames = {
            "Acoustic Piano",
            "Bright Piano",
            "Electric Grand Piano",
        };
        this.instNames = tmpStringNames;
    }
    private void setRadioMenuItems(){
        RadioMenuItem[] tmpRadioMenuItems =new RadioMenuItem[3];
        for(int i =0;i<tmpRadioMenuItems.length;i++){
            RadioMenuItem menu = new RadioMenuItem(this.instNames[i]);
            tmpRadioMenuItems[i] = menu;
            menu.setToggleGroup(this.groupElect);
            menu.setOnAction(event -> electedActionHandler(event, menu));
        }
        this.radioMenuItems = tmpRadioMenuItems;
    }
}
