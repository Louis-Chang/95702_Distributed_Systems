import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/22/2024
 */
public class RemoteVariableClientUDP {
    private static int serverPort;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("The client is running.");
        System.out.println("Please enter server port: ");
        serverPort = scanner.nextInt();
        System.out.println();

        while (true) {
            System.out.println("1. Add a value to your sum.");
            System.out.println("2. Subtract a value from your sum.");
            System.out.println("3. Get your sum.");
            System.out.println("4. Exit client");
            int operation = scanner.nextInt();
            int value = 0;

            // 1. Add a value to your sum.
            // 2. Subtract a value from your sum.
            if (operation == 1 || operation == 2) {
                System.out.println("Enter the value to " + (operation == 1 ? "add" : "subtract") + ": ");
                value = scanner.nextInt();
            } else if (operation == 3) {    // 3. Get your sum.
                value = 0;
            } else if (operation == 4) {    // 4. Exit client
                System.out.println("Client side quitting.");
                break;
            } else {    // Invalid option
                System.out.println("Invalid option.");
                continue;
            }

            System.out.println("Enter your ID: ");
            int id = scanner.nextInt();

            try {
                // send the message to the server
                int result = calculate(id, operation, value);
                System.out.println("The result is " + result + ".\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        scanner.close();
    }

    // send the message to the server
    public static int calculate(int id, int operation, int value) throws IOException {
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            InetAddress aHost = InetAddress.getByName("localhost");
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.putInt(id).putInt(operation).putInt(value);
            byte[] sendData = byteBuffer.array();
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
