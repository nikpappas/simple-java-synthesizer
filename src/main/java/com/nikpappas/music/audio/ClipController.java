package com.nikpappas.music.audio;

import javax.sound.sampled.LineUnavailableException;

public interface ClipController extends AutoCloseable {
    void play(int midinote) throws LineUnavailableException;

    void stop(int midinote);
}
