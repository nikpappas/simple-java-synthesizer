package com.nikpappas.music.midi.midilistener;

import com.nikpappas.music.midi.reciever.DisplayReceiver;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import static java.lang.String.format;

/**
 * Create a connection between a musical keyboard (transmitter) and an internal
 * synthesizer.  You should first run {@link MidiDeviceDisplay} to discover the
 * device names for each of these.
 *
 * @author Knute Snortum
 * @version 2017/06/17
 */
public class KeyboardToMessage implements Runnable {
    private final Receiver receiver;
    private final String midiDevice;


    public KeyboardToMessage(Receiver receiver, String midiDevice) {
        this.midiDevice = midiDevice;
        this.receiver = receiver;
    }

    public KeyboardToMessage(Receiver receiver) {
        this(receiver, "DigitalKBD-1");
    }

    /**
     * See {@link MidiSystem} for other classes
     */
    private static final String TRANS_PROP_KEY = "javax.sound.midi.Transmitter";


    @Override
    public void run() {

        // Get a transmitter and synthesizer from their device names
        // using system properties or defaults
        Transmitter trans = getTransmitter();

        if (trans == null) {
            return;
        }


        // You get your receiver from the synthesizer, then set it in
        // your transmitter.  Optionally, you can create an implementation
        // of Receiver to display the messages before they're sent.
        DisplayReceiver displayReceiver = new DisplayReceiver(receiver);
        trans.setReceiver(displayReceiver); // or just "receiver"

        // You should be able to play on your musical keyboard (transmitter)
        // and hear sounds through your PC synthesizer (receiver)
        System.out.println("Play on your musical keyboard...");
    }


    /**
     * @return a specific transmitter object by setting the system property, otherwise the default
     */
    private Transmitter getTransmitter() {
        final String TRANS_DEV_NAME = format("%s#%s", TRANS_PROP_KEY, midiDevice);

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
