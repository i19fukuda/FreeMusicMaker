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

public class Conductor {
    private int         tempo;
    private Sequence    sequence;
    private Sequencer   sequencer;
    private ArrayList<Track> tracks;

    public Conductor(int tempo){
        this.tempo = tempo;
        try{
            this.sequence = new Sequence(Sequence.PPQ,24);
            MetaMessage mmes = new MetaMessage();
            int l = 60*1000000/this.tempo;
            mmes.setMessage(0x51,new byte[]{(byte)(l/65536), (byte)(l%65536/256), (byte)(l%256)},3);
            //曲のメタ情報が入るトラック
            this.tracks = new ArrayList<>();
            createTrackAndSetMetaMessage(mmes);

            // 曲のデータが入るトラック
            createTrackAndSetMetaMessage(mmes);

            this.sequencer = MidiSystem.getSequencer(false);
            Receiver receiver = MidiSystem.getReceiver();
            this.sequencer.getTransmitter().setReceiver(receiver);
            //this.sequencer.open();
            //this.sequencer.setSequence(this.sequence);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //トラックが指定されてない場合は1に振る
    public void setNotes(int note,int volume,long tick, long length){
        setNotes(1, note, volume,tick, length);
    }
    public void setNotes(int trackId, int note,int volume,long tick, long length){
        if(note > 127){
            System.out.println("out of range:" + note + "  set 255");
            note = 127;
        }
        if(volume > 127){
            System.out.println("out of range:" + volume + "set 255");
            volume = 127;
        }
        try{
            ShortMessage messageOn = new ShortMessage();
            messageOn.setMessage(ShortMessage.NOTE_ON, note, volume);

            ShortMessage messageOff = new ShortMessage();
            messageOff.setMessage(ShortMessage.NOTE_OFF, note, 0);

            MidiEvent eventOn = new MidiEvent(messageOn,tick);
            MidiEvent eventOff = new MidiEvent(messageOff, tick + length);

            this.sequence.getTracks()[trackId].add(eventOn);
            this.sequence.getTracks()[trackId].add(eventOff);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    //実際に音を鳴らすメソッド
    public void play(long startTick){
        MyMidiPlayer player = new MyMidiPlayer(this.sequencer,this.sequence);
        player.start();
    }

    //新しくトラックを作ってメタ情報を埋め込むメソッド
    public void createTrackAndSetMetaMessage(MetaMessage mmes){
        Track track = this.sequence.createTrack();
        this.tracks.add(track);
        track.add(new MidiEvent(mmes, 0));
    }
}

class MyMidiPlayer extends Thread{
    Sequencer sequencer;
    Sequence sequence;
    public MyMidiPlayer(Sequencer sequencer, Sequence sequence){
        this.sequencer = sequencer;
        this.sequence = sequence;
    }
    public void run(){
        try{
            this.sequencer.open();
            System.out.println("Sequencer open");
            this.sequencer.setSequence(sequence);
            System.out.println("sequencer start");
            this.sequencer.start();
            while(this.sequencer.isRunning()){
                Thread.sleep(100);
            }
            this.sequencer.stop();
            while(this.sequencer.isRunning()){
                Thread.sleep(100);
            }
            this.sequencer.close();
            //this.sequencer.setTickPosition(0L);
            System.out.println("sequencer stoped,closed succece");
        } catch (Exception e){
            try{
                this.sequencer.stop();
                this.sequencer.close();
                System.out.println("sequencer stoped,closed with error");

            } catch (Exception ee){}
            System.out.println(e.getMessage());
        }
    }
}