package com.nikpappas.music;

import java.util.HashMap;
import java.util.Map;

public class MidiToFrequency {
    private static final Map<Integer, Double> MIDI_TO_FREQUENCY = new HashMap<>();
    static {
        MIDI_TO_FREQUENCY.put(57, 220.0);
        MIDI_TO_FREQUENCY.put(58, 233.08);
        MIDI_TO_FREQUENCY.put(59, 246.94);
        MIDI_TO_FREQUENCY.put(60, 261.63);
        MIDI_TO_FREQUENCY.put(61, 277.18);
        MIDI_TO_FREQUENCY.put(62, 293.66);
        MIDI_TO_FREQUENCY.put(63, 311.13);
        MIDI_TO_FREQUENCY.put(64, 329.63);
        MIDI_TO_FREQUENCY.put(65, 349.23);
        MIDI_TO_FREQUENCY.put(66, 369.99);
        MIDI_TO_FREQUENCY.put(67, 392.0);
        MIDI_TO_FREQUENCY.put(68, 415.30);
        MIDI_TO_FREQUENCY.put(69, 440.0);
        MIDI_TO_FREQUENCY.put(70, 466.16);
        MIDI_TO_FREQUENCY.put(71, 493.88);
        MIDI_TO_FREQUENCY.put(72, 523.25);
        MIDI_TO_FREQUENCY.put(73, 554.37);
        MIDI_TO_FREQUENCY.put(74, 587.33);
        MIDI_TO_FREQUENCY.put(75, 622.25);
        MIDI_TO_FREQUENCY.put(76, 659.25);
        MIDI_TO_FREQUENCY.put(77, 698.46);
        MIDI_TO_FREQUENCY.put(78, 739.99);
        MIDI_TO_FREQUENCY.put(79, 783.99);
        MIDI_TO_FREQUENCY.put(80, 830.61);
        MIDI_TO_FREQUENCY.put(81, 880.0);
        MIDI_TO_FREQUENCY.put(82, 932.33);
        MIDI_TO_FREQUENCY.put(83, 987.77);
        MIDI_TO_FREQUENCY.put(84, 1046.66);
        MIDI_TO_FREQUENCY.put(85, 1108.73);
        MIDI_TO_FREQUENCY.put(86, 1174.66);
        MIDI_TO_FREQUENCY.put(87, 1244.51);
    }

    public static double frequencyForMidi(int midinote){
        return MIDI_TO_FREQUENCY.getOrDefault(midinote, 220.0);
    }
}
