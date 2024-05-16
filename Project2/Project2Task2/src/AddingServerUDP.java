import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/22/2024
 */
public class AddingServerUDP {
    private static int sum = 0;
    public static void main(String[] args){
        DatagramSocket aSocket = null;
        byte[] buffer = new byte[1000];
        try {
            System.out.println("Server started");
            aSocket = new DatagramSocket(6789);
            while (true) {
                // receive the request from the client
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                // copy the request data into a new byte array with correct length
                int receivedValue = java.nio.ByteBuffer.wrap(request.getData()).getInt();

                // add the received value to the sum
                int newSum = add(receivedValue);
                System.out.println("Adding: " + receivedValue + " to " + (newSum - receivedValue));
                System.out.println("Returning sum of " + newSum + " to client");

                // reply to client
                byte[] replyData = java.nio.ByteBuffer.allocate(4).putInt(newSum).array();
                DatagramPacket reply = new DatagramPacket(replyData, replyData.length, request.getAddress(), request.getPort());
                aSocket.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if(aSocket != null) {
                System.out.println("UDP Server side quitting");
                aSocket.close();
            }
        }
    }
    private static int add(int value) {
        sum += value;
        return sum;
    }
}
