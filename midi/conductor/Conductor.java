package midi.conductor;

import java.util.ArrayList;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Conductor{
    private int         tempo;
    private Sequence    sequence;
    private Sequencer   sequencer;
    private ArrayList<Track> tracks;
    //曲全体にかかるメタ情報
    final private MetaMessage MMES;

    // 再生にかかわるクラス
    private MyMidiPlayer myMidiPlayer;

    public Conductor(int tempo){
        this.tempo = tempo;
        MMES = new MetaMessage();
        try{
            int l = 60*1000000 / this.tempo;
            MMES.setMessage(
                0x51,
                new byte[]{(byte)(l / 65536),
                (byte)(l % 65536/256),
                (byte)(l % 256)},
                3
            );
        } catch(Exception e){
            this.showErrorDialog(e.getMessage());
            System.out.println(e.getMessage());
        }
        try{
            this.sequence = new Sequence(Sequence.PPQ, 24);
            //曲のメタ情報が入るトラック
            this.tracks = new ArrayList<>();
            createTrackAndSetMetaMessage(MMES);

            // 曲のデータが入るトラック
            createTrackAndSetMetaMessage(MMES);

            this.sequencer = MidiSystem.getSequencer(false);
            Receiver receiver = MidiSystem.getReceiver();
            this.sequencer.getTransmitter().setReceiver(receiver);
            //this.sequencer.open();
            //this.sequencer.setSequence(this.sequence);
        } catch(Exception e){
            showErrorDialog(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    //トラックが指定されてない場合は1に振る
    public void setNotes(
        int notePich,
        int volume,
        long startTick,
        int instNo,
        long length
    ){
        setNotes(
            1,
            notePich,
            volume,
            startTick,
            instNo,
            length
        );
    }
    public void setNotes(
        int trackId,
        int notePich,
        int volume,
        long startTick,
        int instNo,
        long length
    ){
        int isper=0;
        if(instNo == 128){
            isper = 9;
            instNo = 0;
        }
        while(this.sequence.getTracks().length <= trackId + 1){
            this.createTrack();
        }
        if(notePich > 127){
            System.out.println(
                "out of range:" + notePich + "  set 255"
            );
            notePich = 127;
        }
        if(volume > 127){
            System.out.println("out of range:" + volume +127);
            volume = 127;
        }
        try{
            ShortMessage changeProgram = new ShortMessage();
            changeProgram.setMessage(
                ShortMessage.PROGRAM_CHANGE, isper, instNo, 0
            );

            ShortMessage messageOn = new ShortMessage();
            messageOn.setMessage(
                ShortMessage.NOTE_ON, isper, notePich, volume
            );


            // System.out.println("Volume= " + volume);

            ShortMessage messageOff = new ShortMessage();
            messageOff.setMessage(ShortMessage.NOTE_OFF, isper, notePich, 0);

            MidiEvent eventChange = new MidiEvent(changeProgram, startTick);
            MidiEvent eventOn = new MidiEvent(messageOn, startTick);
            MidiEvent eventOff = new MidiEvent(messageOff, startTick + length);

            this.sequence.getTracks()[trackId + 1].add(eventChange);
            this.sequence.getTracks()[trackId + 1].add(eventOn);
            this.sequence.getTracks()[trackId + 1].add(eventOff);

            // System.out.println(trackId);
        }catch(Exception e){
            showErrorDialog(e.getMessage());
            System.out.println(e.getMessage());
        }
    }
    //楽器を変更する関数
    public void changeInstrument(int instrument){
        this.changeInstrument(1,instrument);
    }
    public void changeInstrument(int trackId, int instrument){
        int tId = trackId +1;
        if(this.sequence.getTracks().length <= tId){
            this.createTrack();
        }
        try{
            ShortMessage programChange = new ShortMessage();
            System.out.println(instrument);
            programChange.setMessage(
                ShortMessage.PROGRAM_CHANGE, 0, instrument, 0
            );

            MidiEvent eventChange = new MidiEvent(programChange, 0L);

            this.tracks.get(tId).add(eventChange);
        }catch(Exception e){
            showErrorDialog(e.getMessage());
            System.out.println(e.getMessage());
            try{
                this.sequencer.close();
                this.sequencer.stop();
            }catch(Exception ee){}
        }
    }

    public boolean isRunning(){
        boolean isRunning = this.sequencer.isRunning();
        return isRunning;
    }

    //実際に音を鳴らすメソッド
    public void play(long startTick){
        this.myMidiPlayer = new MyMidiPlayer(this.sequencer, this.sequence);
        myMidiPlayer.start();
    }
    public void play(){
        this.myMidiPlayer.run();
    }
    public void play(long startTick, long endTick){
        this.myMidiPlayer = new MyMidiPlayer(sequencer, sequence, startTick, endTick);
        myMidiPlayer.start();
    }

    //新しくトラックを作ってメタ情報を埋め込むメソッド
    private void createTrackAndSetMetaMessage(MetaMessage mmes){
        Track track = this.sequence.createTrack();
        this.tracks.add(track);
        track.add(new MidiEvent(mmes, 0));
    }

    // 外部から新しくトラックを作ることを受け付ける
    public void createTrack(){
        createTrackAndSetMetaMessage(MMES);
    }

    public int getTrackSize(){
        return this.tracks.size();
    }

    public void stop(){
        if(this.myMidiPlayer.isAlive()){
            System.out.println("stopping!");
            this.myMidiPlayer.stopPlaying();
        }
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

class MyMidiPlayer extends Thread{
    private Sequencer sequencer;
    private Sequence sequence;
    private long startTick;
    private long endTick;
    private boolean isHaveRange;
    // 現在どこまで再生したか保持する
    public MyMidiPlayer(Sequencer sequencer, Sequence sequence){
        this.sequencer = sequencer;
        this.sequence = sequence;
    }
    public MyMidiPlayer(Sequencer sequencer, Sequence sequence, long startTick, long endTick){
        this.isHaveRange = true;
        this.startTick = startTick;
        this.endTick = endTick;
        this.sequencer = sequencer;
        this.sequence = sequence;
    }
    public void run(){
        try{
            if(! this.sequencer.isOpen()){
                this.sequencer.open();
                System.out.println("Sequencer open");
            }
            this.sequencer.setSequence(sequence);
            System.out.println("sequencer start");
            //もしも再生位置の指定があればセットする
            if(isHaveRange){
                this.sequencer.setTickPosition(this.startTick);
            }


            this.sequencer.start();
            while(
                    this.sequencer.isRunning()
                ){
                Thread.sleep(100);
                if(isHaveRange){
                    if(this.sequencer.getTickPosition() > this.endTick){
                        break;
                    }
                }
            }
            this.sequencer.stop();
            while(this.sequencer.isRunning()){
                Thread.sleep(100);
            }
            this.sequencer.close();
            //this.sequencer.setTickPosition(0L);
            System.out.println("sequencer stoped,closed succece");
        } catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            try{
                this.sequencer.stop();
                this.sequencer.close();
                System.out.println("sequencer stoped,closed with error");

            } catch (Exception ee){}
        }
    }
    public void stopPlaying(){
        try{
            this.sequencer.stop();
        } catch(Exception e){
            showErrorDialog(e.getMessage());
        }
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