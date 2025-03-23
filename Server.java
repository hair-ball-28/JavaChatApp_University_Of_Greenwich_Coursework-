// Server.java
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static Map<Integer, ClientHandler> clients = new ConcurrentHashMap<>();
    private static volatile int coordinatorId = -1;

    public static void main(String[] args) throws IOException {
        int port = 5000;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("[SERVER] Running on port " + port);

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ClientHandler(socket)).start();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private int clientId;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // First line = client ID
                clientId = Integer.parseInt(in.readLine());
                clients.put(clientId, this);

                if (coordinatorId == -1) {
                    coordinatorId = clientId;
                    out.println("You are the COORDINATOR");
                } else {
                    out.println("Current coordinator: Client " + coordinatorId);
                }

                broadcast("Client " + clientId + " has joined the group.");

                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.startsWith("broadcast ")) {
                        broadcast("Client " + clientId + ": " + msg.substring(10));
                    } else if (msg.startsWith("private ")) {
                        String[] parts = msg.split(" ", 3);
                        int targetId = Integer.parseInt(parts[1]);
                        String message = parts[2];
                        sendPrivate(targetId, "[Private from " + clientId + "]: " + message);
                    }
                }
            } catch (IOException e) {
                System.out.println("[SERVER] Client " + clientId + " disconnected.");
            } finally {
                clients.remove(clientId);
                broadcast("Client " + clientId + " has left the group.");
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

        private void sendPrivate(int id, String message) {
            ClientHandler client = clients.get(id);
            if (client != null) {
                client.out.println(message);
            } else {
                out.println("Client " + id + " not found.");
            }
        }

        private void broadcast(String message) {
            for (ClientHandler client : clients.values()) {
                client.out.println(message);
            }
        }
    }
}
