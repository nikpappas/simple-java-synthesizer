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
    public static final int MAX_VOL = 127;
    public static final WaveGenerator DEFAULT_WAVE_GENERATOR = (angle, time) -> Long.valueOf(
            round(sin(angle * time) * MAX_VOL))
            .byteValue();

    private final WaveGenerator waveGenerator;
    private float volume = 1.0f;
    private byte[] buffer;

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
//        int wavelengths = 10;
        int sampleRate = 44100;
        AudioFormat af = new AudioFormat(
                sampleRate,
                8,  // sample size in bits
                2,  // channels
                true,  // signed
                false  // bigendian
        );

        double timeIncrement = Math.pow(sampleRate, -1);
        double frequency = frequencyForMidi(midinote);


        double angle = 2 * frequency * PI;

        double baseClipLength = sampleRate / 8.0; // 1/4s

        long clipLength = round(baseClipLength);

        byte[] buf = new byte[2 * (int) clipLength];
        System.out.println(clipLength);
        int finalLength = 0;
        for (int i = 0; i < clipLength; i++) {
            double time = (i * timeIncrement);
            byte val = (byte) (volume * waveGenerator.generate(angle, time));
            buf[i * 2] = val;
            buf[(i * 2) + 1] = val;
            if (val == -buf[2] && val < 0 && buf[i * 2 - 2] < val) {
                finalLength = 2 * i + 2;
            }
        }

//        System.out.println("finalLength " + finalLength);
//        System.out.println("bufLength " + buf.length);
//        System.out.println("buf[buf.length - 1] - " + buf[buf.length - 1]);
//        System.out.println("buf[2*finalLength - 1]" + buf[2 * finalLength - 1]);


        try {
            byte[] b = null;
            if (finalLength > 1) {
                b = Arrays.copyOfRange(buf, 0, finalLength);
            } else {
                b = buf;
            }
            buffer = b;
            System.out.println("b.length " + b.length);
            System.out.println(b[0]);
            System.out.println(b[1]);
            System.out.println(b[2]);
            System.out.println(b[3]);
            System.out.println(b[b.length - 3]);
            System.out.println(b[b.length - 4]);
            System.out.println(b[b.length - 1]);
            System.out.println(b[b.length - 2]);
            System.out.println(finalLength);
            AudioInputStream ais = new AudioInputStream(
                    new ByteArrayInputStream(b),
                    af,
                    b.length / 2);

//            clip.addLineListener((a) -> {
//                System.out.println(a.getFramePosition());
//            });
//            System.out.println(clip.getLineInfo());
//            System.out.println(clip.getLineInfo());
            clip.open(ais);
            clip.setFramePosition(0);

            //            Arrays.stream(clip.getControls()).forEach(System.out::println);

//            clip.setLoopPoints(Long.valueOf(clipLength / 2).intValue(), -1);
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
        clips.forEach((i, c) -> {
            if (c != null) {
                c.stop();
                c.close();
            }
        });
    }
}
