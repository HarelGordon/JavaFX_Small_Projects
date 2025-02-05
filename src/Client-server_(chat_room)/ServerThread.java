package Q1;

import java.io.*;
import java.net.*;

/**
 * A thread class that handles individual client connections to the server.
 * Each instance of this class is responsible for reading and writing data for a single client.
 */
public class ServerThread extends Thread {

    private Socket clientSocket; // The client's socket connection.
    private String clientAddress; // The client's IP address or hostname.

    /**
     * Constructor to initialize the thread with the client socket and address.
     * 
     * @param clientSocket The socket connection for the client.
     * @param clientAddress The client's IP address or hostname.
     */
    public ServerThread(Socket clientSocket, String clientAddress) {
        this.clientSocket = clientSocket;
        this.clientAddress = clientAddress;
    }

    /**
     * The main execution method for the thread, which handles client communication.
     */
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream()); // Input stream to receive data from the client.
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream()); // Output stream to send data to the client.
            Server.addClientOutput(out); // Register the client's output stream with the server.

            String name = null; // The name of the client.
            while (true) {
                String message = (String) in.readObject(); // Read messages sent by the client.

                if (message != null) {
                    if (message.startsWith("LEAVE:")) { // Client indicates they want to leave.
                        String clientAddress = message.substring(6);
                        Server.removeParticipant(clientAddress);
                        break;
                    } else if (message.startsWith("NAME:")) { // Client sends their name.
                        name = message.substring(5);
                        Server.addParticipant(clientAddress, name);
                    } else if (name != null) { // General message handling.
                        Server.broadcastMessage(name + " [" + clientAddress + "]: " + message + "\n");
                    }
                }
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected: " + clientAddress);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error with client " + clientAddress + ": " + e.getMessage());
        } finally {
            Server.removeParticipant(clientAddress); // Ensure the client is removed on disconnection.
            try {
                clientSocket.close(); // Close the socket connection.
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
