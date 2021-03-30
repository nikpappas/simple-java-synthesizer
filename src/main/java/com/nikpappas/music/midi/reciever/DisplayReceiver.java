package com.nikpappas.music.midi.reciever;


import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

/**
 * Implementation of {@link Receiver} that will display the MIDI message
 * before sending it.
 *
 * @author Knute Snortum
 * @version 2017/06/16
 */
public class DisplayReceiver implements Receiver {
    private Receiver receiver;
    boolean isSystemExclusiveData = false;

    public DisplayReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public DisplayReceiver() {
        System.out.println("Wrapping no receiver -- Display notes only");
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        displayMessage(message, timeStamp);
        if (receiver != null) {
            receiver.send(message, timeStamp);
        }
    }

    @Override
    public void close() {
        if(receiver!=null){
            receiver.close();
        }
    }

    // Display MIDI message
    private void displayMessage(MidiMessage message, long timeStamp) {

        // Check: Are we printing system exclusive data?
        if (isSystemExclusiveData) {
            displayRawData(message);
            return;
        }

        int status = message.getStatus();

        // These statuses clutter the display
        if (status == 0xf8) {
            return;
        } // ignore timing messages
        if (status == 0xfe) {
            return;
        } // ignore status active

        System.out.printf("%d - Status: 0x%s",
                timeStamp, Integer.toHexString(status));

        // Strip channel number out of status
        int leftNibble = status & 0xf0;

        // These statuses have MIDI channel numbers and data (except
        // 0xf0 thru 0xff)
        switch (leftNibble) {
            case 0x80:
                displayNoteOff(message);
                break;
            case 0x90:
                displayNoteOn(message);
                break;
            case 0xa0:
                displayKeyPressure(message);
                break;
            case 0xb0:
                displayControllerChange(message);
                break;
            case 0xc0:
                displayProgramChange(message);
                break;
            case 0xd0:
                displayChannelPressure(message);
                break;
            case 0xe0:
                displayPitchBend(message);
                break;
            case 0xf0:
                displaySystemMessage(message);
                break;
            default:
                System.out.println(" Unknown status");
                displayRawData(message);
        }
    }

    // Displays raw data as integers, if any
    private void displayRawData(MidiMessage message) {
        byte[] bytes = message.getMessage();

        if (message.getLength() > 1) {
            System.out.print("\tRaw data: ");

            for (int i = 1; i < bytes.length; i++) {
                System.out.print(byteToInt(bytes[i]) + " ");
            }

            System.out.println();
        }
    }

    // Display status and data of a NoteOn message.  Data may come
    // in pairs after the status byte.
    //
    // Note that a NoteOn with a velocity of 0 is synonymous with
    // a NoteOff message.
    private void displayNoteOn(MidiMessage message) {
        if (message.getLength() < 3 || message.getLength() % 2 == 0) {
            System.out.println(" Bad MIDI message");
            return;
        }

        byte[] bytes = message.getMessage();

        // Zero velocity
        if (bytes[2] == 0) {
            System.out.print(" = Note off");
        } else {
            System.out.print(" = Note on");
        }

        System.out.print(", Channel " + midiChannelToInt(message));

        if (bytes[2] == 0) {
            System.out.println(", Note " + byteToInt(bytes[1]));
            return;
        }

        System.out.print("\n\t");

        for (int i = 1; i < message.getLength(); i += 2) {
            if (i > 1) {
                System.out.print("; ");
            }
            System.out.printf("Number %d, Velocity %d",
                    byteToInt(bytes[i]), byteToInt(bytes[i + 1]));
        }

        System.out.println();
    }

    // Display status and data of a NoteOff message.
    private void displayNoteOff(MidiMessage message) {
        if (message.getLength() < 3 || message.getLength() % 2 == 0) {
            System.out.println(" Bad MIDI message");
        } else {
            byte[] bytes = message.getMessage();
            System.out.printf(" = Note off, Channel %d, Note %d%n",
                    midiChannelToInt(message), byteToInt(bytes[1]));
            System.out.println();
        }
    }

    // Display status and data of a ControllerChange message.  Data may come
    // in pairs after the status byte.
    private void displayControllerChange(MidiMessage message) {
        if (message.getLength() < 3 || message.getLength() % 2 == 0) {
            System.out.println(" Bad MIDI message");
            return;
        }

        System.out.print(" = Controller Change, Channel "
                + midiChannelToInt(message) + "\n\t");

        byte[] bytes = message.getMessage();
        for (int i = 1; i < message.getLength(); i += 2) {
            if (i > 1) {
                System.out.print("; ");
            }
            System.out.printf("Controller %d, Value %d",
                    byteToInt(bytes[i]), byteToInt(bytes[i + 1]));
        }

        System.out.println();
    }

    // Display status and data of a KeyPressure message.  Data may come
    // in pairs after the status byte.
    private void displayKeyPressure(MidiMessage message) {
        if (message.getLength() < 3 || message.getLength() % 2 == 0) {
            System.out.println(" Bad MIDI message");
            return;
        }

        System.out.print(" = Key Pressure, Channel "
                + midiChannelToInt(message) + "\n\t");

        byte[] bytes = message.getMessage();
        for (int i = 1; i < message.getLength(); i += 2) {
            if (i > 1) {
                System.out.print("; ");
            }
            System.out.printf("Note Number %d, Pressure %d",
                    byteToInt(bytes[i]), byteToInt(bytes[i + 1]));
        }

        System.out.println();
    }

    // Display status and data of a PitchBend message.  Data may come
    // in pairs after the status byte.
    private void displayPitchBend(MidiMessage message) {
        if (message.getLength() < 3 || message.getLength() % 2 == 0) {
            System.out.println(" Bad MIDI message");
            return;
        }

        System.out.print(" = Pitch Bend, Channel "
                + midiChannelToInt(message) + "\n\t");

        byte[] bytes = message.getMessage();
        for (int i = 1; i < message.getLength(); i += 2) {
            if (i > 1) {
                System.out.print("; ");
            }
            System.out.printf("Value %d",
                    bytesToInt(bytes[i], bytes[i + 1]));
        }

        System.out.println();
    }

    // Display status and data of a ProgramChange message
    private void displayProgramChange(MidiMessage message) {
        if (message.getLength() < 2) {
            System.out.println(" Bad MIDI message");
            return;
        }

        System.out.print(" = Program Change, Channel "
                + midiChannelToInt(message) + "\n\t");

        byte[] bytes = message.getMessage();
        for (int i = 1; i < message.getLength(); i++) {
            if (i > 1) {
                System.out.print(", ");
            }
            System.out.println("Program Number " + byteToInt(bytes[i]));
        }
    }

    // Display status and data of a ChannelPressure message
    private void displayChannelPressure(MidiMessage message) {
        if (message.getLength() < 2) {
            System.out.println(" Bad MIDI message");
            return;
        }

        System.out.print(" = Channel Pressure, Channel "
                + midiChannelToInt(message) + "\n\t");

        byte[] bytes = message.getMessage();
        for (int i = 1; i < message.getLength(); i++) {
            if (i > 1) {
                System.out.print(", ");
            }
            System.out.println("Pressure " + byteToInt(bytes[i]));
        }
    }

    // Display system messages.  Some may have data.
    //
    // "Begin System Exclusive" stops data interpretation, "End of
    // System Exclusive" starts it again
    private void displaySystemMessage(MidiMessage message) {
        byte[] bytes = message.getMessage();

        switch (message.getStatus()) {
            case 0xf0:
                System.out.println(" = Begin System Exclusive");
                isSystemExclusiveData = true;
                break;
            case 0xf1:
                if (bytes.length < 2) {
                    System.out.println(" Bad Data");
                } else {
                    System.out.println(" = MIDI Time Code 1/4 Frame, Time Code "
                            + byteToInt(bytes[1]));
                }
                break;
            case 0xf2:
                if (bytes.length < 3) {
                    System.out.println(" Bad Data");
                } else {
                    System.out.println(" = Song Position, Pointer "
                            + bytesToInt(bytes[1], bytes[2]));
                }
            case 0xf3:
                if (bytes.length < 2) {
                    System.out.println(" Bad Data");
                } else {
                    System.out.println(" = Song Select, Song "
                            + byteToInt(bytes[1]));
                }
                break;
            case 0xf6:
                System.out.println(" = Tune Request");
                break;
            case 0xf7:
                System.out.println(" = End of System Exclusive");
                isSystemExclusiveData = false;
                break;
            case 0xf8:
                System.out.println(" = Timing Clock"); // ignored
                break;
            case 0xfa:
                System.out.println(" = Start");
                break;
            case 0xfb:
                System.out.println(" = Continue");
                break;
            case 0xfc:
                System.out.println(" = Stop");
                break;
            case 0xfe:
                System.out.println(" = Active Sensing"); // ignored
                break;
            case 0xff:
                System.out.println(" = System Reset");
                break;
            default:
                System.out.println(" Unknow System Message");
                displayRawData(message);
        }
    }

    private int byteToInt(byte b) {
        return b & 0xff;
    }

    // Two 7-bit bytes
    private int bytesToInt(byte msb, byte lsb) {
        return byteToInt(msb) * 128 + byteToInt(lsb);
    }

    private int midiChannelToInt(MidiMessage message) {
        return (message.getStatus() & 0x0f) + 1;
    }
}
