import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your ID (must be unique): ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter your name: "); // [Ai assistance] name from the user for identification
        String name = scanner.nextLine();

        try {
            Socket socket = new Socket("127.0.0.1", 5000);  // Connect to the server using IP and port
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send ID and Name to server after connecting
            out.println(id);
            out.println(name);
            // Read welcome or error message from server
            String response = in.readLine();
            if (response.startsWith("[ERROR]")) {
                System.out.println(response);
                socket.close();
                return;
            } else {
                System.out.println(response);
            }

            // [AI-assisted]: Receive and display instructions on how to use the chat
            String serverLine;
            while ((serverLine = in.readLine()) != null && !serverLine.isEmpty()) {
                System.out.println(serverLine);
                if (serverLine.equals("")) break;
            }
            // Create a thread to listen for messages from the server
            Thread listener = new Thread(() -> {
                String line;
                try {
                    while ((line = in.readLine()) != null) {
                        System.out.println(line); // Print incoming message
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });
            listener.start();
            // Read and send user input to the server
            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("quit")) {
                    socket.close(); // Quit command exits the client
                    System.exit(0);
                }
                out.println(input); // Send message to server
            }
        } catch (IOException e) {
            System.out.println("[CLIENT] Cannot connect to server.");
        }
    }
}
