package com.nikpappas.music.audio;

import javax.sound.sampled.*;

public class AudioWriter {
    public static byte[] buf = new byte[]{1, 2, 3, 4, 5, 6, 7,8};
    public static void main(String[] args) throws LineUnavailableException {
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : infos){

            System.out.println(info);
            System.out.println(info.getDescription());
            System.out.println(info.getVersion());
            System.out.println(info.getName());
            System.out.println("---");
            System.out.println("---");
        }
        System.out.println(Port.Info.LINE_OUT);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(Port.Info.LINE_OUT);

        System.out.println(line.getLineInfo());
        line.open();

        line.write(buf, 0, buf.length);


//        AudioSystem.getLine();
    }
}
