package com.nikpappas.music.audio;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import static com.nikpappas.music.MidiToFrequency.frequencyForMidi;
import static java.lang.Math.*;

public class SineSynth {

    private final Map<Integer, Clip> clips = new HashMap<>();
    public static final int MAX_VOL = 127;
    public static final WaveGenerator DEFAULT_WAVE_GENERATOR = (angle) -> Long.valueOf(
            round(sin(angle) * MAX_VOL))
            .byteValue();

    private final WaveGenerator waveGenerator;

    public static void main(String[] args) throws LineUnavailableException {
        SineSynth app = new SineSynth();
        app.play(220);
        app.stop(220);
    }

    public SineSynth() {
        this(DEFAULT_WAVE_GENERATOR);
    }

    public SineSynth(WaveGenerator waveGenerator) {
        this.waveGenerator = waveGenerator;
    }

    public void play(int midinote) throws LineUnavailableException {
        System.out.println("play(" + midinote + ")");
        Clip clip = clips.get(midinote);
        if (clip != null) {
            clip.stop();
            clip.close();
        } else {
            clip = AudioSystem.getClip();
            clips.put(midinote, clip);
        }
        int wavelengths = 40;
        int sampleRate = 11025;
        double timeIncrement = pow(sampleRate, -1);
        AudioFormat af = new AudioFormat(
                sampleRate,
                8,  // sample size in bits
                2,  // channels
                true,  // signed
                false  // bigendian
        );

        int maxVol = 127;
        double frequency = frequencyForMidi(midinote);

        long clipLength = round(((double) wavelengths * sampleRate) / frequency);
        byte[] buf = new byte[2 * (int) clipLength];
        System.out.println(clipLength);
        for (int i = 0; i < clipLength; i++) {
            double angle = (i * timeIncrement) * 2 * frequency * PI;
            buf[i * 2] = waveGenerator.generate(angle);
            buf[(i * 2) + 1] = buf[i * 2];
        }

        try {
            byte[] b = buf;
            AudioInputStream ais = new AudioInputStream(
                    new ByteArrayInputStream(b),
                    af,
                    buf.length / 2);

            clip.open(ais);
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Loops the current Clip until a commence false is passed.
     */
    public void stop(int midinote) {
        System.out.println("stop(" + midinote + ")");
        Clip clip = clips.get(midinote);
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    public void close() {
        clips.forEach((i, c) -> {
            if (c != null) {
                c.stop();
                c.close();
            }
        });
    }
}
