import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.midi.*;

public class MusicApp {
    private JFrame frame;
    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;

    private JCheckBox[][] checkBoxes;
    private JButton startButton;
    private JButton stopButton;
    private JButton tempoUpButton;
    private JButton tempoDownButton;

    private String[] instrumentNames = {
        "Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare",
        "Crash Cymbal", "Hand Clap", "High Tom", "Hi Bongo",
        "Maracas", "Whistle", "Low Conga", "Cowbell",
        "Vibraslap", "Low-mid Tom", "High Agogo", "Open Hi Conga"
    };

    private int[] instruments = {
        35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63
    };

    public static void main(String[] args) {
        new MusicApp().buildGUI();
    }

    public void buildGUI() {
        frame = new JFrame("Music App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        checkBoxes = new JCheckBox[16][16];
        JPanel checkBoxPanel = new JPanel(new GridLayout(17, 17));

        for (int i = 0; i < 16; i++) {
            checkBoxPanel.add(new JLabel(instrumentNames[i]));
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                checkBoxes[i][j] = new JCheckBox();
                checkBoxPanel.add(checkBoxes[i][j]);
            }
        }

        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        tempoUpButton = new JButton("Tempo Up");
        tempoDownButton = new JButton("Tempo Down");

        startButton.addActionListener(new StartButtonListener());
        stopButton.addActionListener(new StopButtonListener());
        tempoUpButton.addActionListener(new TempoUpButtonListener());
        tempoDownButton.addActionListener(new TempoDownButtonListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(tempoUpButton);
        buttonPanel.add(tempoDownButton);

        frame.add(checkBoxPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        setupMIDI();
        frame.setSize(800, 400);
        frame.setVisible(true);
    }

    public void setupMIDI() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            sequencer.setTempoInBPM(120);
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    public void buildTrackAndStart() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                int instrument = instruments[i];
                int velocity = 100;
                int note = 44 + i;

                if (checkBoxes[j][i].isSelected()) {
                    makeEvent(144, instrument, note, velocity, j);
                    makeEvent(128, instrument, note, velocity, j + 1);
                }
            }
        }
        sequencer.start();
    }

    public void makeEvent(int command, int instrument, int note, int velocity, int tick) {
        try {
            ShortMessage msg = new ShortMessage();
            msg.setMessage(command, 9, instrument, velocity);
            MidiEvent event = new MidiEvent(msg, tick);
            track.add(event);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            buildTrackAndStart();
        }
    }

    class StopButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            sequencer.stop();
        }
    }

    class TempoUpButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor(tempoFactor * 1.03f);
        }
    }

    class TempoDownButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor(tempoFactor * 0.97f);
        }
    }
}
