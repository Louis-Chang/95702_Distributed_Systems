import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/22/2024
 */
public class AddingClientUDP {
    private static int serverPort;

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("The client is running.");
        System.out.println("Please enter server port: ");
        serverPort = scanner.nextInt();
        System.out.println();

        while (true) {
            String input = scanner.next();
            // if the input is "halt!", then break the loop
            if ("halt!".equalsIgnoreCase(input)) {
                System.out.println("Client side quitting.");
                break;
            }
            try {
                // send the message to the server
                int valueToAdd = Integer.parseInt(input);
                int sum = add(valueToAdd);
                System.out.println("The server returned " + sum + ".");
            } catch (IOException e) {
                System.out.println("IO Exception: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer or 'halt!' to exit.");
            }
        }
        scanner.close();
    }

    // send the message to the server
    public static int add(int i) throws IOException {
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            InetAddress aHost = InetAddress.getByName("localhost");
            byte[] sendData = java.nio.ByteBuffer.allocate(4).putInt(i).array();
            DatagramPacket request = new DatagramPacket(sendData, sendData.length, aHost, serverPort);
            aSocket.send(request);

            byte[] buffer = new byte[4];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            return java.nio.ByteBuffer.wrap(reply.getData()).getInt();
        } finally {
            if (aSocket != null) aSocket.close();
        }
    }
}
