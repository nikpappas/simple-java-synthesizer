package com.nikpappas.music.midi.reciever;

import com.nikpappas.music.audio.ClipController;
import com.nikpappas.music.audio.SineSynth;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.sampled.LineUnavailableException;

import static com.nikpappas.music.audio.SineSynth.MAX_VOL;
import static java.lang.Math.*;

public class SynthReceiver implements Receiver {
    private final ClipController controller;

    public SynthReceiver(){
        this(new SineSynth((a, t) -> Long.valueOf(
                round((.6 * sin(a) + .4 * sin(a * 2)) * MAX_VOL*min(sqrt(t)*2, 1)))
                .byteValue()));
    }

    public SynthReceiver(ClipController controller){
        this.controller = controller;
    }


    public static double squareTheSin(double val){
        return val>0?1.0:-1.0;

    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        int leftNibble = message.getStatus() & 0xf0;

        byte[] bytes = message.getMessage();
        // These statuses have MIDI channel numbers and data (except
        // 0xf0 thru 0xff)
        switch (leftNibble) {
            case 0x80:
                controller.stop(byteToInt(bytes[1]));
                break;
            case 0x90:
                try {
                    if (bytes[2] == 0) {
                        controller.stop(byteToInt(bytes[1]));
                    } else {
                        controller.play(byteToInt(bytes[1]));
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
        try {
            controller.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int byteToInt(byte b) {
        return b & 0xff;
    }

}