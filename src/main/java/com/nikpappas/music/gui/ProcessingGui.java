package com.nikpappas.music.gui;

import com.nikpappas.music.audio.SineSynth;
import com.nikpappas.music.midi.midilistener.KeyboardToMessage;
import com.nikpappas.music.midi.reciever.GraphedSynth;
import com.nikpappas.music.midi.reciever.MixSynthReceiver;
import processing.core.PApplet;
import processing.event.KeyEvent;

import java.awt.*;
import java.util.List;

import static com.nikpappas.music.audio.SineSynth.DEFAULT_WAVE_GENERATOR;
import static com.nikpappas.music.audio.SineSynth.MAX_VOL;

public class ProcessingGui extends PApplet {
    private MixSynthReceiver mixerReceiver;

    // The minimum boilerplate code I could get to.
    public static void main(String[] args) {
        PApplet.main(Thread.currentThread().getStackTrace()[1].getClassName());
    }


    Color[] colors = {
            new Color(130, 40, 80),
            new Color(130, 40, 160),
            new Color(30, 140, 80),
            new Color(130, 40, 80),
            new Color(130, 40, 80),
            new Color(130, 40, 80),
    };
    private KeyboardToMessage midiListener;

    private Thread midiThread;

    @Override
    public void settings() {
        size(600, 400);
    }


    @Override
    public void setup() {
        mixerReceiver = new MixSynthReceiver();
        mixerReceiver.add(new SineSynth(DEFAULT_WAVE_GENERATOR));
        midiListener = new KeyboardToMessage(mixerReceiver);
        midiThread = new Thread(midiListener);
        midiThread.start();

    }


    @Override
    public void draw() {
        background(66);
        List<GraphedSynth> synths = mixerReceiver.getGraphedSynths();
        int rectHeight = height / synths.size();

        for (int i = 0; i < synths.size(); i++) {
            fill(colors[i].getRGB());
            int rectStartY = rectHeight * i;
            rect(0, rectStartY, width, rectHeight);
            byte[] buf = synths.get(i).getBuffer();

            if (buf != null) {
                for (int j = 0; j < buf.length; j += 2) {
                    point(j * width / buf.length, rectStartY + rectHeight / 2.0f + (buf[j] / ((float) 1.5f*MAX_VOL/synths.size())) * min(rectHeight / 4.0f, 200));
                }
            }
        }

        delay(200);
    }

    @Override
    public void exit() {
        midiThread.stop();
        super.exit();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("keypressed");
        final float randomNumber = random(.95f, 1.05f);
        System.out.println("adding mew synth -- deetune:" + randomNumber);
        mixerReceiver.add(new SineSynth(
                (angle, time) -> Long.valueOf(
                        Math.round(Math.sin((angle * randomNumber) * time) * MAX_VOL))
                        .byteValue()
        ));
    }
}
