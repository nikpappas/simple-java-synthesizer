package com.nikpappas.music.midi.reciever;

import com.nikpappas.music.audio.SineSynth;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.sampled.LineUnavailableException;

public class SynthReceiver implements Receiver {
    private SineSynth sine = new SineSynth();


    @Override
    public void send(MidiMessage message, long timeStamp) {
        int leftNibble = message.getStatus() & 0xf0;

        byte[] bytes = message.getMessage();
        // These statuses have MIDI channel numbers and data (except
        // 0xf0 thru 0xff)
        switch (leftNibble) {
            case 0x80:
                sine.stop(byteToInt(bytes[1]));
                break;
            case 0x90:
                try {
                    if (bytes[2] == 0) {
                        sine.stop(byteToInt(bytes[1]));
                    } else {
                        sine.play(byteToInt(bytes[1]));
                    }
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void close() {
        sine.close();
    }

    private int byteToInt(byte b) {
        return b & 0xff;
    }

}