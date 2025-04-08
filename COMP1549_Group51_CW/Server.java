import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    // Stores all connected clients
    private static Map<Integer, ClientHandler> clients = new ConcurrentHashMap<>();
    // Tracks used client IDs to prevent duplicates
    private static Set<Integer> usedIds = ConcurrentHashMap.newKeySet();
    // ID of the current coordinator client
    private static volatile int coordinatorId = -1;

    public static void main(String[] args) throws IOException {
        int port = 5000;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("[SERVER] Running on port " + port);

        // Accept new clients continuously
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ClientHandler(socket)).start(); // Handle each client in its own thread
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private int clientId;
        private String clientName;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // [AI-assisted]: Get name from client
                clientId = Integer.parseInt(in.readLine());
                clientName = in.readLine();

                // Check for duplicate ID
                if (usedIds.contains(clientId)) {
                    out.println("[ERROR] This ID is already taken. Please restart with a different ID.");
                    socket.close();
                    return;
                }

                // Register client
                usedIds.add(clientId);
                clients.put(clientId, this);

                // Log join event using Singleton logger
                Singleton.getInstance().log("Client " + clientId + " (" + clientName + ") joined the group.");

                // Elect coordinator if needed
                if (coordinatorId == -1) {
                    coordinatorId = clientId;
                    out.println("You are the COORDINATOR");
                } else {
                    out.println("Current coordinator: Client " + coordinatorId);
                }

                // [AI-assisted]: Send usage instructions to client
                out.println("\n[INSTRUCTIONS] Available commands:");
                out.println("- broadcast [message] : Send a message to everyone");
                out.println("- private [ID] [message] : Send a private message to a specific client");
                out.println("- quit : Leave the group\n");

                // Notify all clients that a new client joined
                broadcast("Client " + clientId + " (" + clientName + ") has joined the group.");

                // Message handling loop
                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.startsWith("broadcast ")) {
                        broadcast("Client " + clientId + " (" + clientName + "): " + msg.substring(10));
                    } else if (msg.startsWith("private ")) {
                        String[] parts = msg.split(" ", 3);
                        int targetId = Integer.parseInt(parts[1]);
                        String message = parts[2];
                        sendPrivate(targetId, "[Private from " + clientId + " (" + clientName + ")]: " + message);
                    }
                }
            } catch (IOException e) {
                System.out.println("[SERVER] Client " + clientId + " disconnected.");
            } finally {
                // Cleanup on disconnect
                clients.remove(clientId);
                usedIds.remove(clientId);
                Singleton.getInstance().log("Client " + clientId + " (" + clientName + ") left the group.");
                broadcast("Client " + clientId + " (" + clientName + ") has left the group.");

                // Reassign coordinator if needed
                if (clientId == coordinatorId) {
                    if (!clients.isEmpty()) {
                        coordinatorId = clients.keySet().iterator().next();
                        broadcast("Client " + coordinatorId + " is the new coordinator.");
                    } else {
                        coordinatorId = -1;
                    }
                }
            }
        }

        // Send a private message to a specific client
        private void sendPrivate(int id, String message) {
            ClientHandler client = clients.get(id);
            if (client != null) {
                client.out.println(message);
            } else {
                out.println("Client " + id + " not found.");
            }
        }

        // Send a message to all connected clients
        private void broadcast(String message) {
            for (ClientHandler client : clients.values()) {
                client.out.println(message);
            }
        }
    }
}
