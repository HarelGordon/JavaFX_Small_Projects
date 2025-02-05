package Q1;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The `Server` class is the main entry point for the chat server. 
 * It listens for client connections, handles incoming messages, and manages the list of connected participants.
 */
public class Server {

    private static final int SERVER_PORT = 3333; // Port for the server to listen on
    private static final String CLIENT_JOINED = " has joined!\n"; // Message format for client joining
    private static final String CLIENT_LEAVE = " has left!\n";   // Message format for client leaving

    private static Map<String, String> participants = new HashMap<>(); // Stores client addresses and their names
    private static ArrayList<ObjectOutputStream> clientOutputs = new ArrayList<>(); // Stores all active client streams

    /**
     * Main method to start the server. Listens for client connections indefinitely.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) { 
            System.out.println("Server started, waiting for clients...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientAddress = clientSocket.getRemoteSocketAddress().toString();
                System.out.println("New client connected: " + clientAddress);
                new ServerThread(clientSocket, clientAddress).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    /**
     * Adds a new participant to the chat.
     * @param clientAddress The unique address of the client.
     * @param name The name of the participant.
     */
    public static synchronized void addParticipant(String clientAddress, String name) {
        participants.put(clientAddress, name);
        broadcastParticipants(); // Update the participants list for all clients
        broadcastMessage(name + " [" + clientAddress + "]" + CLIENT_JOINED); 
    }

    /**
     * Removes a participant from the chat.
     * @param clientAddress The unique address of the client to remove.
     */
    public static synchronized void removeParticipant(String clientAddress) {
        String name = participants.get(clientAddress);
        if (name != null) {
            String leaveMessage = name + " [" + clientAddress + "]" + CLIENT_LEAVE;

            // Notify the leaving client before broadcasting to others
            ObjectOutputStream leavingClientOutput = null;
            for (ObjectOutputStream out : clientOutputs) {
                try {
                    if (out != null && out.toString().contains(clientAddress)) {
                        leavingClientOutput = out;
                        break;
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            if (leavingClientOutput != null) {
                broadcastMessageToClient(leaveMessage, leavingClientOutput);
            }

            broadcastMessage(leaveMessage); // Notify other clients
            participants.remove(clientAddress); // Remove participant from the list
            broadcastParticipants(); // Update the participants list for all clients
        }
    }

    /**
     * Broadcasts a message to all connected clients.
     * @param message The message to broadcast.
     */
    public static synchronized void broadcastMessage(String message) {
        broadcastMessage(message, null);
    }

    /**
     * Broadcasts a message to a specific client or all clients.
     * @param message The message to broadcast.
     * @param specificClient If specified, only this client will receive the message.
     */
    public static synchronized void broadcastMessage(String message, ObjectOutputStream specificClient) {
        Iterator<ObjectOutputStream> iterator = clientOutputs.iterator();
        while (iterator.hasNext()) {
            ObjectOutputStream out = iterator.next();
            try {
                if (specificClient == null || specificClient == out) {
                    out.writeObject(message);
                }
            } catch (IOException e) {
                iterator.remove(); // Remove disconnected clients
            }
        }
    }

    /**
     * Sends a message to a specific client.
     * @param message The message to send.
     * @param specificClient The client to send the message to.
     */
    public static synchronized void broadcastMessageToClient(String message, ObjectOutputStream specificClient) {
        try {
            specificClient.writeObject(message);
        } catch (IOException e) {
            clientOutputs.remove(specificClient);
        }
    }

    /**
     * Sends the updated list of participants to all clients.
     */
    public static synchronized void broadcastParticipants() {
        ArrayList<String> participantList = new ArrayList<>(participants.values());
        Iterator<ObjectOutputStream> iterator = clientOutputs.iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().writeObject(participantList);
            } catch (IOException e) {
                iterator.remove(); // Remove disconnected clients
            }
        }
    }

    /**
     * Adds a client output stream to the list of active client streams.
     * @param out The output stream of the client to add.
     */
    public static synchronized void addClientOutput(ObjectOutputStream out) {
        if (!clientOutputs.contains(out)) {
            clientOutputs.add(out);
        }
    }
}
