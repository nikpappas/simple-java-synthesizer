package com.nikpappas.music.audio;

public class HarmonicsWaveGenerator implements WaveGenerator {

    public WaveGenerator mainWaveGenerator;

    public HarmonicsWaveGenerator(WaveGenerator mainWaveGenerator) {
        this.mainWaveGenerator = mainWaveGenerator;
    }

    @Override
    public byte generate(double angle, double time) {
        return (byte) (
                0.7f * mainWaveGenerator.generate(angle, time) +
                        0.3f * mainWaveGenerator.generate(angle, time * 2)
        );
    }
}
