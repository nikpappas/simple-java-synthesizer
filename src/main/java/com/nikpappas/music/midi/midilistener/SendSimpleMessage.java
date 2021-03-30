package com.nikpappas.music.midi.midilistener;

import javax.sound.midi.*;

public class SendSimpleMessage {
    public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException {
        ShortMessage myMsg = new ShortMessage();
        // Start playing the note Middle C (60),
        // moderately loud (velocity = 93).
        myMsg.setMessage(ShortMessage.NOTE_ON, 0, 60, 93);
        long timeStamp = -1;
        Receiver rcvr = MidiSystem.getReceiver();
        rcvr.send(myMsg, timeStamp);
//        MidiSystem.getTransmitter().
    }
}
