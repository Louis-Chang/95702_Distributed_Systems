import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/22/2024
 */
public class EchoServerUDP {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        DatagramSocket aSocket = null;
        byte[] buffer = new byte[1000];
        try {
            System.out.println("The UDP server is running.");
            System.out.println("Please enter the port number:");
            int port = scanner.nextInt();
            aSocket = new DatagramSocket(port);
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            while(true) {
                aSocket.receive(request);
                // copy the request data into a new byte array with correct length
                byte[] requestData = new byte[request.getLength()];
                System.arraycopy(request.getData(), 0, requestData, 0, request.getLength());
                String requestString = new String(request.getData(), 0, request.getLength());
                System.out.println("Echoing: "+requestString);
                // reply to client
                DatagramPacket reply = new DatagramPacket(requestData, requestData.length, request.getAddress(), request.getPort());
                aSocket.send(reply);
                // if the request message is "halt!", then break the loop
                if ("halt!".equals(requestString)) {
                    break;
                }
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
}
