import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 02/22/2024
 */
public class EchoClientUDP{
    public static void main(String args[]){
        // args give message contents and server hostname
        DatagramSocket aSocket = null;
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("The UDP client is running.");
            InetAddress aHost = InetAddress.getByName("localhost");
            System.out.println("Enter the port number: ");
            int port = scanner.nextInt();
            aSocket = new DatagramSocket();
            String nextLine;
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            while ((nextLine = typed.readLine()) != null) {
                byte[] m = nextLine.getBytes();
                // send the message to the server
                DatagramPacket request = new DatagramPacket(m, m.length, aHost, port);
                aSocket.send(request);
                byte[] buffer = new byte[1000];

                // receive the reply from the server
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);
                // copy the reply data into a new byte array with correct length
                byte[] replyData = new byte[reply.getLength()];
                System.arraycopy(reply.getData(), 0, replyData, 0, reply.getLength());
                String replyMsg = new String(replyData);
                System.out.println("Reply from server: " + replyMsg);

                // if the reply message is "halt!", then break the loop
                if ("halt!".equals(replyMsg)) {
                    break;
                }
            }

        } catch (SocketException e) {
            System.out.println("Socket Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        } finally {
            if(aSocket != null) {
                System.out.println("UDP Client side quitting");
                aSocket.close();
            }
        }
    }
}