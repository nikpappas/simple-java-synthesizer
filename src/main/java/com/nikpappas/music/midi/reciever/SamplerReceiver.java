package com.nikpappas.music.midi.reciever;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SamplerReceiver implements Receiver {
    public SamplerReceiver() {

    }

//    private init() {
//        loadClip();
//    }

    @Override
    public void send(MidiMessage message, long timeStamp) {

    }

    @Override
    public void close() {

    }

    public Clip loadClip(String filename) {
        Clip in = null;

        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(filename));
            in = AudioSystem.getClip();
            in.open(audioIn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return in;
    }
}
