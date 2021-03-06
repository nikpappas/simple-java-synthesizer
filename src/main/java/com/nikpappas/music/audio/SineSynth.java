package com.nikpappas.music.audio;

import com.nikpappas.music.midi.reciever.GraphedSynth;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.nikpappas.music.MidiToFrequency.frequencyForMidi;
import static java.lang.Math.*;

public class SineSynth implements MixedClipController, GraphedSynth {

    private final Map<Integer, Clip> clips = new HashMap<>();
    private final Map<Integer, Clip> releaseClips = new HashMap<>();

    public static final int SAMPLE_RATE = 44100;
    public static final double TIME_INCREMENT = Math.pow(SAMPLE_RATE, -1);

    public static final double BASE_CLIP_LENGTH = SAMPLE_RATE / 8.0;
    public static final AudioFormat AUDIO_FORMAT = new AudioFormat(
            SAMPLE_RATE,
            8,  // sample size in bits
            2,  // channels
            true,  // signed
            false  // bigendian
    );

    public static final int MAX_VOL = 127;
    public static final WaveGenerator DEFAULT_WAVE_GENERATOR = WaveGenerator.SINE;

    private WaveGenerator waveGenerator;
    private double detune;
    private float volume = 1.0f;
    private byte[] buffer;


    @Override
    public void setWaveGenerator(WaveGenerator waveGenerator) {
        this.waveGenerator = waveGenerator;
    }

    @Override
    public void setDetune(double detune) {
        System.out.println(detune);
        this.detune = detune;
    }

    @Override
    public void rebuffer() {
        clips.forEach((k, v) -> {
            if (v.isRunning()) {
                stop(k);
                try {
                    play(k);
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setVolume(float volume) {
        if (volume > 1) {
            throw new IllegalArgumentException("Volume cannot be greater than 1");
        }
        this.volume = volume;
    }

    public static void main(String[] args) throws LineUnavailableException {
        SineSynth app = new SineSynth();
        app.play(220);
        app.stop(220);
    }

    public SineSynth() {
        this(DEFAULT_WAVE_GENERATOR);
    }

    public SineSynth(WaveGenerator waveGenerator) {
        this(waveGenerator, 0);
    }

    public SineSynth(WaveGenerator waveGenerator, double detune) {
        this.waveGenerator = waveGenerator;
        this.detune = detune;
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

        double frequency = frequencyForMidi(midinote) * (1 + detune);


        double angle = 2 * frequency * PI;


        int clipLength = (int) round(BASE_CLIP_LENGTH) * (midinote < 50 ? 2 : 1);
        byte[] buf = new byte[2 * clipLength];
        System.out.println(clipLength);
        int finalLength = 0;
        for (int i = 0; i < clipLength; i++) {
            double time = (i * TIME_INCREMENT);
            byte val = (byte) round(volume * waveGenerator.generate(angle, time));
            buf[i * 2] = val;
            buf[(i * 2) + 1] = val;
            if (abs(val + buf[2]) < 2 && val < 0 && buf[i * 2 - 2] < val) {
                finalLength = 2 * i + 2;
            }
        }


        try {
            byte[] b = finalLength > 1 ? Arrays.copyOfRange(buf, 0, finalLength) : buf;
            buffer = b;
            System.out.println("b.length " + b.length);

            AudioInputStream ais = new AudioInputStream(
                    new ByteArrayInputStream(b),
                    AUDIO_FORMAT,
                    b.length / 2);

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
            clip.flush();
            clip.stop();
            clip.close();
        }

    }

    @Override
    public byte[] getBuffer() {
        return buffer;
    }

    public void close() {
        releaseClips.forEach((i, c) -> {
            if (c != null) {
                c.stop();
                c.close();
            }
        });
        clips.forEach((i, c) -> {
            if (c != null) {
                c.stop();
                c.close();
            }
        });
    }
}
