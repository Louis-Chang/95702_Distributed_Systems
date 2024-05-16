package cmu.ds.project3;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 03/17/2024
 */
public class ServerTCP {
    private static final BlockChain blockchain = new BlockChain();
    private static final Gson gson = new Gson();
    private static final int port = 7778;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Blockchain server running on port " + port);

            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Adding genesis block to the blockchain with a simple transaction
            blockchain.addBlock(new Block(0, new Timestamp(System.currentTimeMillis()), "Genesis", 2));

            // Read input from client
            String inputLine;
            while ((inputLine = in.readLine()) != null) { // Keep reading until client disconnects
                System.out.println("Received: " + inputLine);
                RequestMessage request = gson.fromJson(inputLine, RequestMessage.class);
                ResponseMessage response = processRequest(request);
                out.println(gson.toJson(response));
                System.out.println("Sent: " + gson.toJson(response));
            }

            System.out.println("Client disconnected.");
            // Close resources for this client
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
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

    private static ResponseMessage processRequest(RequestMessage request) {
        // Process the request and return a response
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

    // Helper methods to process the request
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

    // This method adds a new block to the blockchain
    private static void addTransaction(int difficulty, String transaction, Map<String, String> data) {
        long startTime = System.currentTimeMillis();
        Block newBlock = new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), transaction, difficulty);
        blockchain.addBlock(newBlock);
        long endTime = System.currentTimeMillis();
        data.put("Transaction", transaction);
        data.put("Total execution time to add this block", String.valueOf(endTime - startTime));
    }

    // This method verifies the entire blockchain
    private static void verifyBlockchain(Map<String, String> data) {
        String result = blockchain.isChainValid();
        data.put("Verification", result);
    }

    // This method corrupts the blockchain
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

    // This method repairs the blockchain
    private static void repairChain(Map<String, String> data) {
        long startTime = System.currentTimeMillis();
        blockchain.repairChain();
        long endTime = System.currentTimeMillis();
        data.put("Total execution time to add this block", String.valueOf(endTime - startTime));
    }
}
