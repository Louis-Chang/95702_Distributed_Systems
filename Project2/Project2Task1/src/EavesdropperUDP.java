import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/22/2024
 */
public class EavesdropperUDP {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DatagramSocket serverSocket = null;
        DatagramSocket clientSocket = null;
        try {
            // Eavesdropper listens on port 6798 and forwards to server port 6789
            System.out.println("EavesdropperUDP is running.");
            System.out.println("Please enter the server port number:");
            int serverPort = scanner.nextInt(); // Server port
            System.out.println("Please enter the eavesdropper port number:");
            int eavesdropperPort = scanner.nextInt(); // Eavesdropper port

            System.out.println("Listening on port: " + eavesdropperPort + ", forwarding to server port: " + serverPort);

            serverSocket = new DatagramSocket(eavesdropperPort);
            clientSocket = new DatagramSocket();

            byte[] buffer = new byte[1000];
            while (true) {
                DatagramPacket clientPacket = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(clientPacket);

                // message from client
                String message = new String(clientPacket.getData(), 0, clientPacket.getLength());
                System.out.println("Received from client: " + message);

                // modify message if it contains "like" but not "dislike"
                if (message.contains("like") && !message.contains("dislike")) {
                    message = message.replaceFirst("like", "dislike");
                }

                // send modified message to server
                byte[] modifiedMessage = message.getBytes();
                InetAddress serverAddress = InetAddress.getByName("localhost");
                DatagramPacket serverPacket = new DatagramPacket(modifiedMessage, modifiedMessage.length, serverAddress, serverPort);
                clientSocket.send(serverPacket);

                // receive response from server
                byte[] responseBuffer = new byte[1000];
                DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
                clientSocket.receive(responsePacket);
                String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                System.out.println("Received from server: " + response);

                // send response to client
                InetAddress clientAddress = clientPacket.getAddress();
                int clientPort = clientPacket.getPort();
                DatagramPacket replyPacket = new DatagramPacket(responseBuffer, responsePacket.getLength(), clientAddress, clientPort);
                serverSocket.send(replyPacket);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (serverSocket != null) serverSocket.close();
            if (clientSocket != null) clientSocket.close();
        }
    }
}
