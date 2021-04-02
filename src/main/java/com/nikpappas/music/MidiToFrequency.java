package com.nikpappas.music;

import java.util.HashMap;
import java.util.Map;

public class MidiToFrequency {
    private static final Map<Integer, Double> MIDI_TO_FREQUENCY = new HashMap<>();
    static {
        MIDI_TO_FREQUENCY.put(28, 41.2);
        MIDI_TO_FREQUENCY.put(29, 43.65);
        MIDI_TO_FREQUENCY.put(30, 46.25);
        MIDI_TO_FREQUENCY.put(31, 49.00);
        MIDI_TO_FREQUENCY.put(32, 51.91);
        // A1
        MIDI_TO_FREQUENCY.put(33, 55.00);
        MIDI_TO_FREQUENCY.put(34, 58.27);
        MIDI_TO_FREQUENCY.put(35, 61.74);
        MIDI_TO_FREQUENCY.put(36, 65.41);
        MIDI_TO_FREQUENCY.put(37, 69.3);
        MIDI_TO_FREQUENCY.put(38, 73.42);
        MIDI_TO_FREQUENCY.put(39, 77.78);
        MIDI_TO_FREQUENCY.put(40, 82.41);
        MIDI_TO_FREQUENCY.put(41, 87.31);
        MIDI_TO_FREQUENCY.put(42, 92.5);
        MIDI_TO_FREQUENCY.put(43, 98.0);
        MIDI_TO_FREQUENCY.put(44, 103.83);
        // A2
        MIDI_TO_FREQUENCY.put(45, 110.0);
        MIDI_TO_FREQUENCY.put(46, 116.54);
        MIDI_TO_FREQUENCY.put(47, 123.47);
        MIDI_TO_FREQUENCY.put(48, 130.81);
        MIDI_TO_FREQUENCY.put(49, 138.59);
        MIDI_TO_FREQUENCY.put(50, 146.83);
        MIDI_TO_FREQUENCY.put(51, 155.56);
        MIDI_TO_FREQUENCY.put(52, 164.81);
        MIDI_TO_FREQUENCY.put(53, 174.61);
        MIDI_TO_FREQUENCY.put(54, 185.0);
        MIDI_TO_FREQUENCY.put(55, 196.0);
        MIDI_TO_FREQUENCY.put(56, 207.65);
        // A3
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
        // A4
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
        // A5
        MIDI_TO_FREQUENCY.put(81, 880.0);
        MIDI_TO_FREQUENCY.put(82, 932.33);
        MIDI_TO_FREQUENCY.put(83, 987.77);
        MIDI_TO_FREQUENCY.put(84, 1046.66);
        MIDI_TO_FREQUENCY.put(85, 1108.73);
        MIDI_TO_FREQUENCY.put(86, 1174.66);
        MIDI_TO_FREQUENCY.put(87, 1244.51);
        MIDI_TO_FREQUENCY.put(88, 1318.51);
        MIDI_TO_FREQUENCY.put(89, 1396.91);
        MIDI_TO_FREQUENCY.put(90, 1479.98);
        MIDI_TO_FREQUENCY.put(91, 1567.98);
        MIDI_TO_FREQUENCY.put(92, 1661.22);
        // A6
        MIDI_TO_FREQUENCY.put(93, 1760.00);
        MIDI_TO_FREQUENCY.put(94, 1864.66);
        MIDI_TO_FREQUENCY.put(95, 1975.53);
        MIDI_TO_FREQUENCY.put(96, 2093.00);
        MIDI_TO_FREQUENCY.put(97, 2217.46);
        MIDI_TO_FREQUENCY.put(98, 2349.32);
        MIDI_TO_FREQUENCY.put(99, 2489.02);
        MIDI_TO_FREQUENCY.put(100, 2637.02);
        MIDI_TO_FREQUENCY.put(101, 2793.83);
        MIDI_TO_FREQUENCY.put(102, 2959.96);
        MIDI_TO_FREQUENCY.put(103, 3135.96);
    }

    public static double frequencyForMidi(int midinote){
        return MIDI_TO_FREQUENCY.getOrDefault(midinote, 220.0);
    }
}
