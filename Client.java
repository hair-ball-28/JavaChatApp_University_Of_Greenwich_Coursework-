// Client.java
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            Socket socket = new Socket("127.0.0.1", 5000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(id); // Send ID to server

            Thread listener = new Thread(() -> {
                String line;
                try {
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });
            listener.start();

            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("quit")) {
                    socket.close();
                    System.exit(0);
                }
                out.println(input);
            }
        } catch (IOException e) {
            System.out.println("[CLIENT] Cannot connect to server.");
        }
    }
}
