import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/22/2024
 */
public class RemoteVariableServerUDP {
    private static final Map<Integer, Integer> map = new TreeMap<>();

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
                ByteBuffer byteBuffer = ByteBuffer.wrap(request.getData(), 0, request.getLength());

                int id = byteBuffer.getInt();
                int operation = byteBuffer.getInt();
                int value = byteBuffer.getInt();

                // process the request
                int result = 0;
                switch (operation) {    // 1. Add a value to your sum. 2. Subtract a value from your sum. 3. Get your sum.
                    case 1:
                        result = add(id, value);
                        break;
                    case 2:
                        result = subtract(id, value);
                        break;
                    case 3:
                        result = get(id);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operation: " + operation);
                }

                System.out.println("Operation: " + operation + " on " + id + " with value " + value + " to " + result);
                System.out.println("Returning sum of " + result + " to client");

                // reply to client
                byte[] replyData = java.nio.ByteBuffer.allocate(4).putInt(result).array();
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

    // add a value to the sum
    private static int add(int id, int value) {
        int sum = map.getOrDefault(id, 0);
        sum += value;
        map.put(id, sum);
        return sum;
    }

    // subtract a value from the sum
    private static int subtract(int id, int value) {
        int sum = map.getOrDefault(id, 0);
        sum -= value;
        map.put(id, sum);
        return sum;
    }

    // get the sum
    private static int get(int id) {
        int sum = map.getOrDefault(id, 0);
        return sum;
    }
}
