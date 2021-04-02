package com.nikpappas.music.gui;

import com.nikpappas.music.audio.SineSynth;
import com.nikpappas.music.midi.midilistener.KeyboardToMessage;
import com.nikpappas.music.midi.reciever.MixSynthReceiver;
import processing.core.PApplet;
import processing.event.KeyEvent;

import static com.nikpappas.music.audio.SineSynth.DEFAULT_WAVE_GENERATOR;
import static com.nikpappas.music.audio.SineSynth.MAX_VOL;

public class ProcessingGui extends PApplet {
    private MixSynthReceiver mixerReceiver;

    // The minimum boilerplate code I could get to.
    public static void main(String[] args) {
        PApplet.main(Thread.currentThread().getStackTrace()[1].getClassName());
    }

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
        delay(100);
    }

    @Override
    public void exit() {
        midiThread.stop();
        super.exit();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("keypressed");
        final float randomNumber = random(.95f,1.05f);
        System.out.println("adding mew synth -- deetune:"+randomNumber);
        mixerReceiver.add(new SineSynth(
                (angle, time) -> Long.valueOf(
                        Math.round(Math.sin(angle*randomNumber) * MAX_VOL))
                        .byteValue()
        ));
    }
}
