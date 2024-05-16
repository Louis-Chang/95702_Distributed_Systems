package cmu.ds.project3;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import com.google.gson.Gson;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 03/17/2024
 */
public class SigningClientTCP {
    private static final Gson gson = new Gson();
    private static final int PORT = 7778;
    private static final String HOST = "localhost";
    private static BigInteger e, d, n;
    private static BigInteger clientID;

    public static void main(String[] args) {
        try {
            generateRSAKeys();
            Socket socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            String userInput;
            while (true) {
                System.out.println("\n0. View basic blockchain status.");
                System.out.println("1. Add a transaction to the blockchain.");
                System.out.println("2. Verify the blockchain.");
                System.out.println("3. View the blockchain.");
                System.out.println("4. Corrupt the chain.");
                System.out.println("5. Hide the corruption by repairing the chain.");
                System.out.println("6. Exit");

                System.out.print("Enter your choice: ");
                userInput = scanner.nextLine();

                if ("6".equals(userInput)) {
                    System.out.println("Exiting...");
                    break;
                }

                // Create request message based on user input
                clientID = getClientID();
                RequestMessage request = createRequestMessage(userInput, scanner);
                if (request != null) {
                    SignedRequest signedRequest = new SignedRequest(e, n, clientID, request, sign(request));
                    out.println(gson.toJson(signedRequest));

                    String jsonResponse = in.readLine();
                    ResponseMessage response = gson.fromJson(jsonResponse, ResponseMessage.class);
                    printResponse(response);

                } else {
                    System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void generateRSAKeys() throws NoSuchAlgorithmException, InvalidKeySpecException {
        Random rnd = new Random();

        // Step 1: Generate two large random primes.
        // We use 400 bits here, but best practice for security is 2048 bits.
        // Change 400 to 2048, recompile, and run the program again and you will
        // notice it takes much longer to do the math with that many bits.
        BigInteger p = new BigInteger(400, 100, rnd);
        BigInteger q = new BigInteger(400, 100, rnd);

        // Step 2: Compute n by the equation n = p * q.
        n = p.multiply(q);

        // Step 3: Compute phi(n) = (p-1) * (q-1)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // Step 4: Select a small odd integer e that is relatively prime to phi(n).
        // By convention the prime 65537 is used as the public exponent.
        e = new BigInteger("65537");

        // Step 5: Compute d as the multiplicative inverse of e modulo phi(n).
        d = e.modInverse(phi);
        System.out.println("Public Key: (e=" + e + ", n=" + n + ")");
    }

    // This method takes a string and signs it using an RSA private key.
    private static BigInteger getClientID() throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update((e.toString() + n.toString()).getBytes());
        byte[] digest = sha.digest();
        byte[] last20 = Arrays.copyOfRange(digest, digest.length - 20, digest.length);
        return new BigInteger(1, last20); // Ensure positive
    }

    /**
     * Sign the request message
     * @param request
     * @return
     * @throws Exception
     */
    private static String sign(RequestMessage request) throws Exception {
        // compute the digest with SHA-256
        String message = request.concatenateValues();
        byte[] bytesOfMessage = message.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bigDigest = md.digest(bytesOfMessage);

        // we only want two bytes of the hash for ShortMessageSign
        // we add a 0 byte as the most significant byte to keep
        // the value to be signed non-negative.
        byte[] messageDigest = new byte[3];
        messageDigest[0] = 0;   // most significant set to 0
        messageDigest[1] = bigDigest[0]; // take a byte from SHA-256
        messageDigest[2] = bigDigest[1]; // take a byte from SHA-256

        // The message digest now has three bytes. Two from SHA-256
        // and one is 0.

        // From the digest, create a BigInteger
        BigInteger m = new BigInteger(messageDigest);

        // encrypt the digest with the private key
        BigInteger c = m.modPow(d, n);

        // return this as a big integer string
        return c.toString();
    }

    /**
     * Create a request message based on user input
     * @param userInput
     * @param scanner
     * @return
     */
    private static RequestMessage createRequestMessage(String userInput, Scanner scanner) {
        switch (userInput) {
            case "0":
                return new RequestMessage(RequestMessage.VIEW_BLOCKCHAIN_STATUS);
            case "1":
                System.out.print("Enter difficulty > ");
                int difficulty = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter transaction: ");
                String transaction = scanner.nextLine();
                return new RequestMessage(RequestMessage.ADD_TRANSACTION, transaction, difficulty);
            case "2":
                return new RequestMessage(RequestMessage.VERIFY_BLOCKCHAIN);
            case "3":
                return new RequestMessage(RequestMessage.VIEW_BLOCKCHAIN);
            case "4":
                System.out.print("Enter block ID of block to corrupt: ");
                int id = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter new data for block " + id + ": ");
                String newData = scanner.nextLine();
                return new RequestMessage(RequestMessage.CORRUPT_BLOCKCHAIN, id, newData);
            case "5":
                return new RequestMessage(RequestMessage.REPAIR_CHAIN);
            default:
                System.out.println("Invalid choice.");
                return null;
        }
    }

    private static void printResponse(ResponseMessage response) {
        System.out.println("Server response: " + response.getMessage());
        if (response.getData() != null) {
            response.getData().forEach((key, value) -> System.out.println(key + ": " + value));
        }
    }
}
