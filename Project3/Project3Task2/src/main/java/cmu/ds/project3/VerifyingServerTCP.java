package cmu.ds.project3;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 03/17/2024
 */
public class VerifyingServerTCP {
    private static final Gson gson = new Gson();
    private static final int PORT = 7778;
    private static final BlockChain blockchain = new BlockChain();

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Blockchain server running on port " + PORT);

            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Adding genesis block to the blockchain with a simple transaction
            blockchain.addBlock(new Block(0, new Timestamp(System.currentTimeMillis()), "Genesis", 2));

            // Read input from client
            String inputLine;
            while ((inputLine = in.readLine()) != null) { // Keep reading until client disconnects
                System.out.println("Received: " + inputLine);
                SignedRequest signedRequest = gson.fromJson(inputLine, SignedRequest.class);

                // Verify the signature and process the request
                if (verify(signedRequest)) {
                    ResponseMessage response = processRequest(signedRequest.getRequest());
                    out.println(gson.toJson(response));
                    System.out.println("Sent: " + gson.toJson(response));
                } else {
                    out.println(gson.toJson(new ResponseMessage("Error", "Invalid signature or request.")));
                }
            }

            System.out.println("Client disconnected.");
            // Close resources for this client
            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception e) {
            System.out.println("Server failed to start: " + e.getMessage());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.out.println("Could not close server socket: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Verify the signature of the request
     * @param signedRequest
     * @return
     * @throws Exception
     */
    private static boolean verify(SignedRequest signedRequest) throws Exception {
        BigInteger e = signedRequest.getE();
        BigInteger n = signedRequest.getN();
        BigInteger clientID = signedRequest.getClientID();
        String signature = signedRequest.getSignature();

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        String publicKeyConcat = e.toString() + n.toString(); // 串接e和n
        byte[] publicKeyHash = sha256.digest(publicKeyConcat.getBytes("UTF-8"));


        byte[] derivedClientIDBytes = Arrays.copyOfRange(publicKeyHash, publicKeyHash.length - 20, publicKeyHash.length);
        BigInteger derivedClientID = new BigInteger(1, derivedClientIDBytes); // 將字節數組轉換為BigInteger

        if (!clientID.equals(derivedClientID)) {
            return false;
        }

        String concatenatedValues = signedRequest.getRequest().concatenateValues();

        byte[] hashOfConcatenatedValues = sha256.digest(concatenatedValues.getBytes("UTF-8"));

        byte[] hashForVerification = new byte[3];
        hashForVerification[0] = 0;
        hashForVerification[1] = hashOfConcatenatedValues[0];
        hashForVerification[2] = hashOfConcatenatedValues[1];
        BigInteger hashBigInteger = new BigInteger(hashForVerification);

        BigInteger encryptedHash = new BigInteger(signature);
        BigInteger decryptedHash = encryptedHash.modPow(e, n);

        return hashBigInteger.compareTo(decryptedHash) == 0;
    }

    /**
     * Process the request and return a response
     * @param request
     * @return
     */
    private static ResponseMessage processRequest(RequestMessage request) {
        Map<String, String> data = new HashMap<>();
        switch (request.getAction()) {
            case RequestMessage.VIEW_BLOCKCHAIN_STATUS:
                getBlockchainStatus(data);
                return new ResponseMessage(ResponseMessage.SUCCESS, "Blockchain status printed.", data);
            case RequestMessage.ADD_TRANSACTION:
                addTransaction(request.getDifficulty(), request.getData(), data);
                return new ResponseMessage(ResponseMessage.SUCCESS, "Block added.", data);
            case RequestMessage.VERIFY_BLOCKCHAIN:
                verifyBlockchain(data);
                return new ResponseMessage(ResponseMessage.SUCCESS, "Chain verification", data);
            case RequestMessage.VIEW_BLOCKCHAIN:
                data.put("blockchain", blockchain.toString());
                return new ResponseMessage(ResponseMessage.SUCCESS, "Blockchain data.", data);
            case RequestMessage.CORRUPT_BLOCKCHAIN:
                try {
                    corruptBlockchain(request.getId(), request.getNewData(), data);
                    return new ResponseMessage(ResponseMessage.SUCCESS, "Currupt the Blockchain", data);
                } catch (IllegalArgumentException e) {
                    return new ResponseMessage(ResponseMessage.ERROR, e.getMessage(), null);
                }
            case RequestMessage.REPAIR_CHAIN:
                repairChain(data);
                return new ResponseMessage(ResponseMessage.SUCCESS, "Repairing the entire chain", data);
            default:
                return new ResponseMessage(ResponseMessage.ERROR, "Unsupported action.", null);
        }
    }

    /**
     * Get the status of the blockchain
     * @param data
     */
    private static void getBlockchainStatus(Map<String, String> data) {
        data.put("Current size of chain", String.valueOf(blockchain.getChainSize()));
        data.put("Difficulty of most recent block", String.valueOf(blockchain.getLatestBlock().getDifficulty()));
        data.put("Experimented with hashes", String.valueOf(blockchain.numberOfHashes));
        data.put("Total difficulty for all blocks", String.valueOf(blockchain.getTotalDifficulty()));
        data.put("Approximate hashes per second on this machine", String.valueOf(blockchain.getHashesPerSecond()));
        data.put("Expected total hashes required for the whole chain", String.valueOf(blockchain.getTotalExpectedHashes()));
        data.put("Nonce for most recent block", String.valueOf(blockchain.getLatestBlock().getNonce()));
        data.put("Chain hash", blockchain.getChainHash());
    }

    /**
     * Add a new block to the blockchain
     * @param difficulty
     * @param transaction
     * @param data
     */
    private static void addTransaction(int difficulty, String transaction, Map<String, String> data) {
        long startTime = System.currentTimeMillis();
        Block newBlock = new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), transaction, difficulty);
        blockchain.addBlock(newBlock);
        long endTime = System.currentTimeMillis();
        data.put("Transaction", transaction);
        data.put("Total execution time to add this block", String.valueOf(endTime - startTime));
    }

    /**
     * Verify the entire blockchain
     * @param data
     */
    private static void verifyBlockchain(Map<String, String> data) {
        String result = blockchain.isChainValid();
        data.put("Verification", result);
    }

    /**
     * Corrupt the blockchain
     * @param id
     * @param newData
     * @param data
     */
    private static void corruptBlockchain(int id, String newData, Map<String, String> data) {
        if (id >= 0 && id < blockchain.getChainSize()) {
            System.out.print("Corrupting block " + id + " with new data: " + newData + "\n");
            Block blockToCorrupt = blockchain.getBlock(id);
            blockToCorrupt.setData(newData);
            data.put("CorruptedBlockID", String.valueOf(id));
            data.put("CorruptedBlockData", newData);
        } else {
            System.out.println("Invalid block ID.");
            throw new IllegalArgumentException("Invalid block ID.");
        }
    }

    /**
     * Repair the blockchain
     * @param data
     */
    private static void repairChain(Map<String, String> data) {
        long startTime = System.currentTimeMillis();
        blockchain.repairChain();
        long endTime = System.currentTimeMillis();
        data.put("Total execution time to add this block", String.valueOf(endTime - startTime));
    }
}
