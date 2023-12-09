import java.io.*;
import java.net.*;
import java.awt.event.*;

public class ChatClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private ChatGUI gui;

    public ChatClient() {
        gui = new ChatGUI();

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        gui.getSendButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        gui.getMessageField().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        new Thread(new IncomingReader()).start();
    }

    private void sendMessage() {
        String message = gui.getMessageField().getText();
        if (!message.isEmpty()) {
            writer.println(message);
            gui.getMessageField().setText("");
        }
    }

    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    gui.getMessageArea().append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ChatClient();
    }
}
