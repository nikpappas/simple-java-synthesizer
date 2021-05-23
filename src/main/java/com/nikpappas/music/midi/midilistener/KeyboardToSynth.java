package com.nikpappas.music.midi.midilistener;

import com.nikpappas.music.midi.reciever.DisplayReceiver;
import com.nikpappas.music.midi.reciever.SynthReceiver;

import javax.sound.midi.*;

import static java.lang.String.format;

/**
 * Create a connection between a musical keyboard (transmitter) and an internal
 * synthesizer.  You should first run {@link MidiDeviceDisplay} to discover the
 * device names for each of these.
 *
 * @author Knute Snortum
 * @version 2017/06/17
 */
public class KeyboardToSynth {


    /**
     * See {@link MidiSystem} for other classes
     */
    private static final String TRANS_PROP_KEY = "javax.sound.midi.Transmitter";
    private static final String SYNTH_PROP_KEY = "javax.sound.midi.Synthesizer";

    /**
     * Name values can have the class name, (see {@link MidiSystem}), the device
     * name or both. Use a pound sign (#) to separate the class and device name.
     * Get device names from the {@link MidiDeviceDisplay} program, or leave
     * empty for default.<p>
     * <p>
     * {@code javax.sound.midi.Transmitter#USB Uno MIDI Interface}<br>
     * {@code javax.sound.midi.Synthesizer#Microsoft MIDI Mapper}<br>
     */
    private static final String TRANS_DEV_NAME = format("%s#%s", TRANS_PROP_KEY, "DigitalKBD-1");
    private static final String SYNTH_DEV_NAME = format("%s#%s", SYNTH_PROP_KEY, "Microsoft MIDI Mapper");


    public static void main(String[] args) {
        new KeyboardToSynth().run();
    }

    private void run() {

        // Get a transmitter and synthesizer from their device names
        // using system properties or defaults
        Transmitter trans = getTransmitter();
        Synthesizer synth = getSynthesizer();

        if (trans == null || synth == null) {
            return;
        }

        // The synthesizer is your MIDI device, which needs to be opened
        if (!synth.isOpen()) {
            try {
                synth.open();
            } catch (MidiUnavailableException e) {
                System.err.println("Error opening synthesizer");
                e.printStackTrace();
                return;
            }
        }

        // You get your receiver from the synthesizer, then set it in
        // your transmitter.  Optionally, you can create an implementation
        // of Receiver to display the messages before they're sent.
        try {
            Receiver receiver = synth.getReceiver();
//            DisplayReceiver displayReceiver = new DisplayReceiver(receiver); // optional

//            DisplayReceiver displayReceiver = new DisplayReceiver();
            DisplayReceiver displayReceiver = new DisplayReceiver(new SynthReceiver());
            trans.setReceiver(displayReceiver); // or just "receiver"
//            trans.setReceiver(receiver); // or just "receiver"

            // You should be able to play on your musical keyboard (transmitter)
            // and hear sounds through your PC synthesizer (receiver)
            System.out.println("Play on your musical keyboard...");
        } catch (MidiUnavailableException e) {
            System.err.println("Error getting receiver from synthesizer");
            e.printStackTrace();
        }
    }

    /**
     * @return a specific synthesizer object by setting the system property, otherwise the default
     */
    private Synthesizer getSynthesizer() {
        if (!SYNTH_DEV_NAME.isEmpty() || !"default".equalsIgnoreCase(SYNTH_DEV_NAME)) {
            System.setProperty(SYNTH_PROP_KEY, SYNTH_DEV_NAME);
        }

        try {
            return MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException e) {
            System.err.println("Error getting synthesizer");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return a specific transmitter object by setting the system property, otherwise the default
     */
    private Transmitter getTransmitter() {
        if (!TRANS_DEV_NAME.isEmpty() && !"default".equalsIgnoreCase(TRANS_DEV_NAME)) {
            System.setProperty(TRANS_PROP_KEY, TRANS_DEV_NAME);
        }

        try {
            return MidiSystem.getTransmitter();
        } catch (MidiUnavailableException e) {
            System.err.println("Error getting transmitter");
            e.printStackTrace();
            return null;
        }
    }

}
