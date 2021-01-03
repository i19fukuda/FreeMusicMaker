package view.trackLine;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public class ElectInst {
    MenuBar menubar;
    Menu rootM;

    final private int[][] MENU_INST_RANGES = {
        {0   ,7},
        {8   ,15},
        {16  ,23},
        {24  ,31},
        {32  ,39},
        {40  ,47},
        {48  ,55},
        {56  ,63},
        {64  ,71},
        {72  ,79},
        {80  ,87},
        {88  ,95},
        {96  ,103},
        {104 ,111},
        {112 ,119},
        {120 ,127},
        {128 ,129},
    };

    // メニューの名前
    final private String[] MENU_INST_NAMES = {
        "Piano",
        "Chromatic Percussion",
        "Organ",
        "Guitar",
        "Bass",
        "Strings",
        "Ensenble",
        "Brass",
        "Reed",
        "Pipe",
        "Synth Lead",
        "Synth Pad",
        "Synth Effects",
        "Ethnic",
        "Percussive",
        "Sound effects",
        "Doram set",
    };
    private String[]        instNames;
    private RadioMenuItem[] radioMenuItems;
    private ToggleGroup     groupElect;

    private int electedInstNo;

    final private int INST_BROCK_SIZE = 17;

    private Menu[] menus = new Menu[INST_BROCK_SIZE];
    // Ensemble
    public ElectInst(){
        this.menubar = new MenuBar();
        this.groupElect = new ToggleGroup();
        this.rootM = new Menu("inst");

        initMenus();
        initInstNames();
        initRadioMenuItems();

        setRadioMenuItemsToMenu();

        setMenuToRootM();
        this.menubar.getMenus().addAll(this.rootM);
    }

    private void electedActionHandler(ActionEvent event,RadioMenuItem radioMenuItem){
        //System.out.println("cliecked!!");
        for(int i=0; i<this.radioMenuItems.length; i++){
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
        String instName = this.instNames[instNo];
        this.rootM.setText(instName);
    }


    private void initMenus(){
        for(int i=0; i<INST_BROCK_SIZE; i++){
            this.menus[i] = new Menu(this.MENU_INST_NAMES[i]);
        }
    }

    private void initRadioMenuItems(){
        RadioMenuItem[] tmpRadioMenuItems = new RadioMenuItem[
                                                this.instNames.length
                                            ];
        for(int i =0; i<tmpRadioMenuItems.length; i++){
            RadioMenuItem menu = new RadioMenuItem(this.instNames[i]);
            tmpRadioMenuItems[i] = menu;
            menu.setToggleGroup(this.groupElect);
            menu.setOnAction(event -> electedActionHandler(event, menu));
        }
        this.radioMenuItems = tmpRadioMenuItems;
    }

    private void setRadioMenuItemsToMenu(){
        for(int i=0; i<INST_BROCK_SIZE; i++){
            for(int j=MENU_INST_RANGES[i][0]; j<MENU_INST_RANGES[i][1]; j++){
                //System.out.println("i= " + i + " j= " + j);
                this.menus[i].getItems().add(this.radioMenuItems[j]);
            }
        }
    }

    private void setMenuToRootM(){
        for(Menu menu : this.menus){
            this.rootM.getItems().add(menu);
        }
    }

    private void initInstNames(){
        String[] tmpStringNames = {
            //0-7
            "Acoustic Piano	アコースティックピアノ",
            "Bright Piano	ブライトピアノ",
            "Electric Grand Piano	エレクトリック・グランドピアノ",
            "Honky-tonk Piano	ホンキートンクピアノ",
            "Electric Piano	エレクトリックピアノ",
            "Electric Piano 2	FMエレクトリックピアノ",
            "Harpsichord	ハープシコード",
            "Clavi	クラビネット",
            //8-15
            "Celesta	チェレスタ",
            "Glockenspiel	グロッケンシュピール",
            "Musical box	オルゴール",
            "Vibraphone	ヴィブラフォン",
            "Marimba	マリンバ",
            "Xylophone	シロフォン",
            "Tubular Bell	チューブラーベル",
            "Dulcimer	ダルシマー",
            //16-23
            "Drawbar Organ	ドローバーオルガン",
            "Percussive Organ	パーカッシブオルガン",
            "Rock Organ	ロックオルガン",
            "Church organ	チャーチオルガン",
            "Reed organ	リードオルガン",
            "Accordion	アコーディオン",
            "Harmonica	ハーモニカ",
            "Tango Accordion	タンゴアコーディオン",
            //24-31
            "Acoustic Guitar (nylon)	アコースティックギター（ナイロン弦）",
            "Acoustic Guitar (steel)	アコースティックギター（スチール弦）",
            "Electric Guitar (jazz)	ジャズギター",
            "Electric Guitar (clean)	クリーンギター",
            "Electric Guitar (muted)	ミュートギター",
            "Overdriven Guitar	オーバードライブギター",
            "Distortion Guitar	ディストーションギター",
            "Guitar harmonics	ギターハーモニクス",
            //21-39
            "Acoustic Bass	アコースティックベース",
            "Electric Bass (finger)	フィンガー・ベース",
            "Electric Bass (pick)	ピック・ベース",
            "Fretless Bass	フレットレスベース",
            "Slap Bass 1	スラップベース 1",
            "Slap Bass 2	スラップベース 2",
            "Synth Bass 1	シンセベース 1",
            "Synth Bass 2	シンセベース 2",
            //40-47
            "Violin	ヴァイオリン",
            "Viola	ヴィオラ",
            "Cello	チェロ",
            "Double bass	コントラバス",
            "Tremolo Strings	トレモロ",
            "Pizzicato Strings	ピッチカート",
            "Orchestral Harp	ハープ",
            "Timpani	ティンパニ",
            //48-55
            "String Ensemble 1	ストリングアンサンブル",
            "String Ensemble 2	スローストリングアンサンブル",
            "Synth Strings 1	シンセストリングス",
            "Synth Strings 2	シンセストリングス2",
            "Voice Aahs	声「アー」",
            "Voice Oohs	声「ドゥー」",
            "Synth Voice	シンセヴォイス",
            "Orchestra Hit	オーケストラヒット",
            //56-63
            "Trumpet	トランペット",
            "Trombone	トロンボーン",
            "Tuba	チューバ",
            "Muted Trumpet	ミュートトランペット",
            "French horn	フレンチ・ホルン",
            "Brass Section	ブラスセクション",
            "Synth Brass 1	シンセブラス 1",
            "Synth Brass 2	シンセブラス 2",
            //64-71
            "Soprano Sax	ソプラノサックス",
            "Alto Sax	アルトサックス",
            "Tenor Sax	テナーサックス",
            "Baritone Sax	バリトンサックス",
            "Oboe	オーボエ",
            "English Horn	イングリッシュホルン",
            "Bassoon	ファゴット",
            "Clarinet	クラリネット",
            //72-79
            "Piccolo	ピッコロ",
            "Flute	フルート",
            "Recorder	リコーダー",
            "Pan Flute	パンフルート",
            "Blown Bottle	ブロウンボトル（吹きガラス瓶）",
            "Shakuhachi	尺八",
            "Whistle	口笛",
            "Ocarina	オカリナ",
            //80-87
            "Lead 1 (square)	正方波",
            "Lead 2 (sawtooth)	ノコギリ波",
            "Lead 3 (calliope)	カリオペリード",
            "Lead 4 (chiff)	チフリード",
            "Lead 5 (charang)	チャランゴリード",
            "Lead 6 (voice)	声リード",
            "Lead 7 (fifths)	フィフスズリード",
            "Lead 8 (bass + lead)	ベース + リード",
            //88-95
            "Pad 1 (Fantasia)	ファンタジア",
            "Pad 2 (warm)	ウォーム",
            "Pad 3 (polysynth)	ポリシンセ",
            "Pad 4 (choir)	クワイア",
            "Pad 5 (bowed)	ボウ",
            "Pad 6 (metallic)	メタリック",
            "Pad 7 (halo)	ハロー",
            "Pad 8 (sweep)	スウィープ",
            //96-103
            "FX 1 (rain)	雨",
            "FX 2 (soundtrack)	サウンドトラック",
            "FX 3 (crystal)	クリスタル",
            "FX 4 (atmosphere)	アトモスフィア",
            "FX 5 (brightness)	ブライトネス",
            "FX 6 (goblins)	ゴブリン",
            "FX 7 (echoes)	エコー[要曖昧さ回避]",
            "FX 8 (sci-fi)	サイファイ",
            //104-111
            "Sitar	シタール",
            "Banjo	バンジョー",
            "Shamisen	三味線",
            "Koto	琴",
            "Kalimba	カリンバ",
            "Bagpipe	バグパイプ",
            "Fiddle	フィドル",
            "Shanai	シャハナーイ",
            //112-119
            "Tinkle Bell	ティンクルベル",
            "Agogo	アゴゴ",
            "Steel Drums	スチールドラム",
            "Woodblock	ウッドブロック",
            "Taiko Drum	太鼓",
            "Melodic Tom	メロディックタム",
            "Synth Drum	シンセドラム",
            "Reverse Cymbal	逆シンバル",
            //120-127
            "Guitar Fret Noise	ギターフレットノイズ",
            "Breath Noise	ブレスノイズ",
            "Seashore	海岸",
            "Bird Tweet	鳥の囀り",
            "Telephone Ring	電話のベル",
            "Helicopter	ヘリコプター",
            "Applause	拍手",
            "Gunshot	銃声",
            //ドラム 特別[128]
            "Percussion ドラム"
        };
        this.instNames = tmpStringNames;
    }
}
