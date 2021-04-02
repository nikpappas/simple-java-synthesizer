package com.nikpappas.music.midi.reciever;

import com.nikpappas.music.audio.ClipController;
import com.nikpappas.music.audio.MixedClipController;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.List;

public class MixSynthReceiver implements Receiver, ClipController {

    private SynthReceiver synth = new SynthReceiver(this);

    private List<MixedClipController> clipControllers = new ArrayList<>();

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

    @Override
    public void play(int midinote) {
        clipControllers.forEach(r -> {
            try {
                r.play(midinote);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void stop(int midinote) {
        clipControllers.forEach(r -> r.stop(midinote));

    }
}
