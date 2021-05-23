package com.nikpappas.music.audio;

import processing.core.PApplet;
import processing.event.KeyEvent;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;


public class StreamedSynth extends PApplet {
    private boolean play = false;
    private SourceDataLine dataLine;
    public static final int SAMPLE_RATE = 44100;
    private byte[] buf;

    public static void main(String[] args) {
        PApplet.main(Thread.currentThread().getStackTrace()[1].getClassName());
    }


    @Override
    public void settings() {

        AudioFormat af = new AudioFormat(
                SAMPLE_RATE,
                8,  // sample size in bits
                2,  // channels
                true,  // signed
                false  // bigendian
        );
        buf = initBuf(500, 440, SAMPLE_RATE);
        try  {
            dataLine = AudioSystem.getSourceDataLine(af);
            dataLine.open(af, 1000);
            dataLine.start();
            System.out.println(dataLine.getLineInfo());
            System.out.println(dataLine.isActive());
            System.out.println(dataLine.isRunning());
            System.out.println(dataLine.isOpen());

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        size(300, 400);
    }

    @Override
    public void setup() {

        background(33);
    }

    @Override
    public void draw() {
        if (buf == null) {
            buf = initBuf(500, 440, SAMPLE_RATE);
        }

        if (play) {

            System.out.println(dataLine.getLineInfo());
            System.out.println(dataLine.isActive());
            System.out.println(dataLine.isRunning());
            System.out.println(dataLine.isOpen());
            System.out.println(dataLine.available());
            System.out.println(dataLine.getFramePosition());
            dataLine.write(buf, 0, buf.length);

//            if(dataLine.available()<10){
//                dataLine.write(buf, 0, buf.length);
//            }
        } else {
            if (dataLine.isRunning()){
                dataLine.drain();
                dataLine.stop();
            }
        }

        dataLine.drain();
        delay(200);
    }

    private byte[] initBuf(int length, float frequency, int sampleRate) {

        float timeIncrement = pow(sampleRate, -1);
        byte[] temp = new byte[length * 2];
        for (int i = 0; i < length; i++) {
            temp[i * 2] = Integer
                    .valueOf(round(
                            127.0f * sin(2 * PI * frequency * i * timeIncrement)))
                    .byteValue();
            temp[i * 2 + 1] = temp[i * 2];

        }
        return temp;
    }


    @Override
    public void keyPressed(KeyEvent ke) {
        play = !play;
        System.out.println("Playing... " + play);
    }


}
