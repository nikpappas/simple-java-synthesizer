package com.nikpappas.music.gui;

import com.nikpappas.music.audio.HarmonicsWaveGenerator;
import com.nikpappas.music.audio.SineSynth;
import com.nikpappas.music.audio.WaveGenerator;
import com.nikpappas.music.midi.midilistener.KeyboardToMessage;
import com.nikpappas.music.midi.midilistener.MidiDeviceDisplay;
import com.nikpappas.music.midi.reciever.GraphedSynth;
import com.nikpappas.music.midi.reciever.MixSynthReceiver;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

import static com.nikpappas.music.audio.SineSynth.DEFAULT_WAVE_GENERATOR;
import static com.nikpappas.music.audio.SineSynth.MAX_VOL;

public class ProcessingGui extends PApplet {
    private static String midiDevice;
    private MixSynthReceiver mixerReceiver;
    private static final HashMap<Character, Integer> keyToMidiLookup;

    static {
        keyToMidiLookup = new HashMap<>();
        keyToMidiLookup.put('a', 60);
        keyToMidiLookup.put('w', 61);
        keyToMidiLookup.put('s', 62);
        keyToMidiLookup.put('e', 63);
        keyToMidiLookup.put('d', 64);
        keyToMidiLookup.put('f', 65);
        keyToMidiLookup.put('t', 66);
        keyToMidiLookup.put('g', 67);
        keyToMidiLookup.put('y', 68);
        keyToMidiLookup.put('h', 69);
        keyToMidiLookup.put('u', 70);
        keyToMidiLookup.put('j', 71);
        keyToMidiLookup.put('k', 72);
        keyToMidiLookup.put('o', 73);
        keyToMidiLookup.put('l', 74);
        keyToMidiLookup.put('p', 75);
    }

    private int scale = 0;
    String[] scaleName = {"Time", "Full clip"};

    int harmonicCount = 1;
    public static final int CLOSE_MARGIN = 60;
    private final WaveGenerator[] waves = {WaveGenerator.SINE, WaveGenerator.SQUARE, WaveGenerator.TRI, WaveGenerator.SAT_SINE, WaveGenerator.SAW, new HarmonicsWaveGenerator(WaveGenerator.SINE), new HarmonicsWaveGenerator(WaveGenerator.SAW)};
    private boolean pcKeyboard;

    // The minimum boilerplate code I could get to.
    public static void main(String[] args) {
        if (args.length == 1) {
            if ("list".equals(args[0])) {
                MidiDeviceDisplay.main(new String[]{});
                return;
            } else {
                midiDevice = args[0];
            }

        }
        PApplet.main(Thread.currentThread().getStackTrace()[1].getClassName());
    }


    Color[] colors = {
            new Color(130, 40, 80),
            new Color(130, 40, 160),
            new Color(81, 24, 137),
            new Color(30, 74, 140),
            new Color(184, 98, 7),
            new Color(130, 40, 80),
            new Color(90, 49, 203),
            new Color(130, 55, 21),
            new Color(75, 38, 109),
            new Color(105, 14, 111),
            new Color(130, 40, 80),
            new Color(130, 40, 80),
            new Color(130, 40, 80),
    };
    private KeyboardToMessage midiListener;


    private Thread midiThread;

    @Override
    public void settings() {

        size(900, 800);
//        fullScreen();
    }


    @Override
    public void setup() {
        mixerReceiver = new MixSynthReceiver();
        mixerReceiver.add(new SineSynth(DEFAULT_WAVE_GENERATOR));
        midiListener = new KeyboardToMessage(mixerReceiver, midiDevice);
        midiThread = new Thread(midiListener);
        midiThread.start();

    }


    @Override
    public void draw() {
        background(66);
        List<GraphedSynth> synths = mixerReceiver.getGraphedSynths();
        int rectHeight = height / synths.size();

        for (int i = 0; i < synths.size(); i++) {
            stroke(255);
            fill(colors[i].getRGB());
            int rectStartY = rectHeight * i;
            rect(0, rectStartY, width, rectHeight);
            byte[] buf = synths.get(i).getBuffer();
            float zero = rectStartY + rectHeight / 2.0f;
            strokeWeight(2);
            if (buf != null) {
                for (int j = 0; j < buf.length; j += 2) {
                    if (scale == 0) {
                        point(15 + j * (width - 30.0f) / 1105.0f, zero + (buf[j] / (1.5f * MAX_VOL / synths.size())) * min(rectHeight / 4.0f, 200));
                    } else {
                        point(15 + j * (width - 30.0f) / buf.length, zero + (buf[j] / (1.5f * MAX_VOL / synths.size())) * min(rectHeight / 4.0f, 200));
                    }
                }
            }
            stroke(255, 70f);
            line(0, zero, width, zero);
            fill(255, 70);
            textSize(26);
            stroke(0, 70f);
            line(0, rectStartY, width, rectStartY);
            if (synths.size() > 1) {
                text('x', width - CLOSE_MARGIN / 2, rectStartY + CLOSE_MARGIN / 2);
            }
        }

        strokeWeight(1);
        stroke(33, 60);
        line(width / 2, 0, width / 2, height);

        text(scaleName[scale], CLOSE_MARGIN / 2, CLOSE_MARGIN / 2);

        stroke(22);
        line(mouseX, 0, mouseX, height);
        line(0, mouseY, width, mouseY);
        delay(100);
    }

    @Override
    public void exit() {
        midiThread.stop();
        super.exit();
    }


    @Override
    public void keyReleased(KeyEvent e) {
        if(pcKeyboard && keyToMidiLookup.containsKey(e.getKey())){
            stopNote(e.getKey());
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("keypressed");
        char keyChar = e.getKey();
        if (pcKeyboard && keyToMidiLookup.containsKey(keyChar)) {
            playNote(keyChar);
            return;
        }

        if (keyChar == 'a') {
            mixerReceiver.add(new SineSynth(
                    (angle, time) -> Long.valueOf(
                            Math.round(Math.sin(harmonicCount * angle * time) * MAX_VOL))
                            .byteValue()
            ));
        }
        if (keyChar == 'h') {
            harmonicCount++;
            addHarmonic(harmonicCount);
        }
        if (keyChar == 's') {
            scale = (scale + 1) % 2;
        }
        if (keyChar == 'q') {
            pcKeyboard = !pcKeyboard;
        }

    }

    private void playNote(char keyChar) {
        mixerReceiver.play(keyToMidiLookup.get(keyChar));
    }
    private void stopNote(char keyChar) {
        mixerReceiver.stop(keyToMidiLookup.get(keyChar));
    }


    public void addHarmonic(int harmonic) {
        System.out.println("Adding nth harmonic " + harmonicCount);
        mixerReceiver.add(new SineSynth(
                (angle, time) -> Long.valueOf(
                        Math.round(Math.sin(harmonic * angle * time) * MAX_VOL))
                        .byteValue()
        ));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int rectHeight = height / mixerReceiver.getGraphedSynths().size();
        int synthNum = e.getY() / rectHeight;

        System.out.println("dragged: " + e.getY());
        double detune = ((e.getX() - (width / 2.0)) / (width * 1.0) / 2.5);
        mixerReceiver.setDetune(synthNum, detune);

        float volume = ((e.getY() - rectHeight * synthNum) / (rectHeight * 1.0f));
        mixerReceiver.setVolume(synthNum, volume);


    }


    @Override
    public void mouseClicked(MouseEvent e) {
        int synthLength = mixerReceiver.getGraphedSynths().size();
        int rectHeight = height / synthLength;
        int synthNum = e.getY() / rectHeight;

        if (synthLength > 1 && e.getX() > (width - CLOSE_MARGIN)) {
            mixerReceiver.remove(synthNum);
            return;
        }

        System.out.println("clicked: " + e.getY());
        System.out.println("SynthNum: " + synthNum);
        int waveType = floor((e.getY() - (rectHeight * synthNum)) / (rectHeight * 1.0f) * waves.length);

        System.out.println("waveType: " + waveType);

        WaveGenerator toSet = waves[waveType];
        mixerReceiver.setWaveGenerator(synthNum, toSet);

    }

}
