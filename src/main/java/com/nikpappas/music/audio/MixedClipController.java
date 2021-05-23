package com.nikpappas.music.audio;

public interface MixedClipController extends ClipController{
    void setVolume(float volume);
    void setWaveGenerator(WaveGenerator waveGenerator);
    void setDetune(double detune);
    void rebuffer();
}
