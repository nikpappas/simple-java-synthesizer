package com.nikpappas.music.midi.reciever;

import com.nikpappas.music.audio.ClipController;
import com.nikpappas.music.audio.MixedClipController;
import com.nikpappas.music.audio.WaveGenerator;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MixSynthReceiver implements Receiver, ClipController {

    private final SynthReceiver synth = new SynthReceiver(this);

    private final List<MixedClipController> clipControllers = new ArrayList<>();

    @Override
    public void send(MidiMessage message, long timeStamp) {
        synth.send(message, timeStamp);
    }

    @Override
    public void close() {
        clipControllers.forEach(r -> close());
    }

    public void add(MixedClipController clipController) {
        if (!clipControllers.isEmpty()) {
            float volume = 1.0f / (clipControllers.size() + 1);
            clipController.setVolume(volume);
            clipControllers.forEach(cc -> cc.setVolume(volume));
        }
        clipControllers.add(clipController);

    }

    public MixedClipController remove(int i) {
        try {
            clipControllers.get(i).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clipControllers.remove(i);
    }

    @Override
    public void play(int midinote) {

        for (int i = 0; i < clipControllers.size(); i++) {
            System.out.println("cc" + i);
            try {
                clipControllers.get(i).play(midinote);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop(int midinote) {
        clipControllers.forEach(r -> r.stop(midinote));
    }

    public List<GraphedSynth> getGraphedSynths() {
        return clipControllers.stream().map(cc -> (GraphedSynth) cc).collect(toList());
    }

    public void setWaveGenerator(int i, WaveGenerator waveGenerator) {
        MixedClipController controller = clipControllers.get(i);
        controller.setWaveGenerator(waveGenerator);
        controller.rebuffer();

    }

    public void setDetune(int i, double detune) {
        MixedClipController controller = clipControllers.get(i);
        clipControllers.get(i).setDetune(detune);
        controller.rebuffer();

    }

    public void setVolume(int i, float volume) {
        MixedClipController controller = clipControllers.get(i);
        clipControllers.get(i).setVolume(volume);
        controller.rebuffer();

    }

}
