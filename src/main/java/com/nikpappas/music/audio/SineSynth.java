package com.nikpappas.music.audio;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

public class SineSynth {

    private Map<Integer, Double> ANGULAR_VELOCITY_LOOKUP= new HashMap<>();
    {
        ANGULAR_VELOCITY_LOOKUP.put(69, 440.0);
        ANGULAR_VELOCITY_LOOKUP.put(70, 466.16);
        ANGULAR_VELOCITY_LOOKUP.put(71, 493.88);
        ANGULAR_VELOCITY_LOOKUP.put(72, 523.25);
        ANGULAR_VELOCITY_LOOKUP.put(73, 554.37);
        ANGULAR_VELOCITY_LOOKUP.put(74, 587.33);
        ANGULAR_VELOCITY_LOOKUP.put(81, 880.0);
    }
    private Map<Integer, Clip> clips = new HashMap<>();

    public static void main(String[] args) throws LineUnavailableException {
        SineSynth app = new SineSynth();
        app.play(220);
        app.stop(220);
    }

    public void play(int midinote) throws LineUnavailableException {
        System.out.println("play("+midinote+")");
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
        byte[] buf = new byte[2 * midinote * wavelengths];
        AudioFormat af = new AudioFormat(
                sampleRate,
                8,  // sample size in bits
                2,  // channels
                true,  // signed
                false  // bigendian
        );

        int maxVol = 127;
        double frequency = ANGULAR_VELOCITY_LOOKUP.getOrDefault(midinote, 220.0);

        long clipLength = round(((double) wavelengths*sampleRate)/frequency);
        System.out.println(clipLength);
        for (int i = 0; i < clipLength; i++) {
            double angle = (i * timeIncrement)*2 *  frequency*PI;
            buf[i * 2] = getByteValue(angle);
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
            clip.loop( Clip.LOOP_CONTINUOUSLY );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /** Loops the current Clip until a commence false is passed. */
    public void stop(int midinote) {
        System.out.println("stop("+midinote+")");
        Clip clip = clips.get(midinote);
        if ( clip != null ) {
            clip.stop();
            clip.close();
        }
    }

    /**
     * Provides the byte value for this point in the sinusoidal wave.
     */
    private static byte getByteValue(double angle) {
        int maxVol = 127;
        return (new Integer(
                (int) round(
                        Math.sin(angle) * maxVol))).
                byteValue();
    }
    public void close(){
        clips.forEach((i,c)->{
            if(c!=null){
                c.stop();
                c.close();
            }
        });
    }
}
