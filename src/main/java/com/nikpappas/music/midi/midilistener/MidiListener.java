package com.nikpappas.music.midi.midilistener;

import javax.sound.midi.*;
import java.io.Closeable;

import static javax.sound.midi.Sequence.*;

public class MidiListener implements Runnable{
    public static void main(String[] args) {
        MidiListener app = new MidiListener();
        app.run();
    }

    public void run(){
        for (MidiDevice.Info a : MidiSystem.getMidiDeviceInfo()) {
            System.out.println(a);
        }
        Sequencer seq = null;
        Transmitter seqTrans = null;
        Synthesizer synth = null;
        Receiver synthRcvr = null;
        try {
            seq = MidiSystem.getSequencer();
            seqTrans = seq.getTransmitter();
            synth = MidiSystem.getSynthesizer();
            synthRcvr = synth.getReceiver();
            seqTrans.setReceiver(synthRcvr);
            System.out.println(seq);
            System.out.println(seqTrans);
            System.out.println(synth);
            System.out.println(synthRcvr);
            seq.open();
            Sequence sequence = new Sequence(SMPTE_30, 100);
            seq.setSequence(sequence);
            seq.startRecording();
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            System.out.println(e);
        } finally {
            close(synthRcvr);
            close(synth);
            close(seqTrans);
            close(seq);

        }

    }
    public static void close(AutoCloseable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
