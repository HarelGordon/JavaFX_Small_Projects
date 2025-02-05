package Q1;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * A thread class for handling communication with the server.
 * Responsible for sending messages to the server and receiving responses.
 */
public class ClientThread extends Thread {
    private static final int SERVER_PORT = 3333; // Port used by the server.

    private ClientController control; // Reference to the controller for updating the GUI.
    private String serverIp; // The server's IP address.
    private String name; // The client's name.
    private boolean listening; // Whether the thread is actively listening for server messages.
    private Socket clientSocket = null; // The client's socket connection.
    private ObjectOutputStream out; // Output stream for sending data to the server.
    private String clientAddress; // The client's own address.

    /**
     * Constructor to initialize the client thread with necessary details.
     * 
     * @param control The controller for the client GUI.
     * @param ip The server's IP address.
     * @param name The client's name.
     */
    public ClientThread(ClientController control, String ip, String name) {
        this.control = control;
        this.serverIp = ip;
        this.name = name;
        listening = true;
    }

    /**
     * Main execution method for the thread. Handles server communication.
     */
    public void run() {
        try {
            handleReadAndWrite(); // Start handling server communication.
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Handles reading from and writing to the server.
     * 
     * @throws Exception If an error occurs during communication.
     */
    @SuppressWarnings("unchecked")
    public void handleReadAndWrite() throws Exception {
        clientSocket = new Socket(serverIp, SERVER_PORT); // Establish a connection to the server.
        out = new ObjectOutputStream(clientSocket.getOutputStream()); // Initialize the output stream.
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream()); // Initialize the input stream.
        clientAddress = clientSocket.getRemoteSocketAddress().toString(); // Retrieve the client's address.
        out.writeObject("NAME:" + name); // Send the client's name to the server.
        out.flush();

        while (listening) {
            Object serverMessage = in.readObject(); // Read messages from the server.

            if (serverMessage instanceof String) {
                control.addToMessagesArea((String) serverMessage); // Update the message area.
            } else if (serverMessage instanceof ArrayList<?>) {
                ArrayList<?> list = (ArrayList<?>) serverMessage;
                if (list.isEmpty() || list.get(0) instanceof String) {
                    control.setParticipantsArea((ArrayList<String>) list); // Update the participants area.
                }
            } else {
                System.err.println("ERROR: Received unexpected data type.");
            }
        }
    }

    /**
     * Sends a leave notification to the server and stops listening.
     */
    public void leave() {
        try {
            out.writeObject("LEAVE:" + clientAddress); // Notify the server of the leave.
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            Object serverMessage = in.readObject();
            if (serverMessage instanceof String) {
                control.addToMessagesArea((String) serverMessage);
            }
            listening = false; // Stop listening for server messages.
            clientSocket.close(); // Close the socket connection.
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error during leave process: " + e.getMessage());
        }
    }

    /**
     * Sends a message to the server.
     * 
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        try {
            out.writeObject(message); // Write the message to the output stream.
            out.flush();
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}
