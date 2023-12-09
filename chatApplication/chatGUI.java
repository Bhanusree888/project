import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatGUI {
    private JFrame frame;
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;

    public ChatGUI() {
        frame = new JFrame("Chat Application");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        messageField = new JTextField();
        frame.add(messageField, BorderLayout.SOUTH);

        sendButton = new JButton("Send");
        frame.add(sendButton, BorderLayout.EAST);

        frame.setVisible(true);
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

    public JTextField getMessageField() {
        return messageField;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public JFrame getFrame() {
        return frame;
    }
}
