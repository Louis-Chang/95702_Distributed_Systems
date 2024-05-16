package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 03/17/2024
 */
public class BlockChain {
    private ArrayList<Block> blockchain;
    private String chainHash;
    private int hashesPerSecond = 0;
    public final long numberOfHashes = 2000000; // 2 million hashes

    public BlockChain() {
        this.blockchain = new ArrayList<>();
        this.chainHash = "";
    }

    /**
     * A new Block is being added to the BlockChain.
     * @param newBlock
     */
    public void addBlock(Block newBlock) {
        if (!blockchain.isEmpty()) {
            newBlock.setPreviousHash(blockchain.get(blockchain.size() - 1).calculateHash());
        }
        newBlock.proofOfWork(); // Assuming proofOfWork sets the hash inside the Block
        blockchain.add(newBlock);
        this.chainHash = newBlock.calculateHash(); // Update the chain hash to the latest block's hash
    }

    /**
     * This method computes exactly 2 million hashes and times how long that process takes.
     */
    public void computeHashesPerSecond() {
        final String textToHash = "00000000";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            long startTime = System.nanoTime();
            for (int i = 0; i < numberOfHashes; i++) {
                byte[] hash = digest.digest(textToHash.getBytes());
            }
            long endTime = System.nanoTime();
            double durationInSeconds = (endTime - startTime) / 1_000_000_000.0; // Convert nanoseconds to seconds
            this.hashesPerSecond = (int) (numberOfHashes / durationInSeconds);
            System.out.println("Hashes per second: " + this.hashesPerSecond);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // Getter for hashesPerSecond
    public int getHashesPerSecond() {
        return hashesPerSecond;
    }

    /**
     * return block at position i
     * @param i
     * @return
     */
    public Block getBlock(int i) {
        return i >= 0 && i < blockchain.size() ? blockchain.get(i) : null;
    }

    public String getChainHash() {
        return this.chainHash;
    }

    public int getChainSize() {
        return blockchain.size();
    }

    public Block getLatestBlock() {
        return blockchain.isEmpty() ? null : blockchain.get(blockchain.size() - 1);
    }

    /**
     * Compute and return the expected number of hashes required for the entire chain.
     * @return
     */
    public double getTotalExpectedHashes() {
        double totalExpectedHashes = 0;
        for (Block block : blockchain) {
            int difficulty = block.getDifficulty();
            totalExpectedHashes += Math.pow(16, difficulty);
        }
        return totalExpectedHashes;
    }

    public int getTotalDifficulty() {
        int totalDifficulty = 0;
        for (Block block : blockchain) {
            totalDifficulty += block.getDifficulty();
        }
        return totalDifficulty;
    }

    // This method verifies the entire blockchain
    public String isChainValid() {
        if (blockchain.isEmpty()) {
            return "Blockchain is empty";
        }
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            if (!currentBlock.getPreviousHash().equals(previousBlock.calculateHash())) {
                return "FALSE\nImproper hash on node " + (i-1) + " Does not begin with 0000";
            }
        }
        return "TRUE";
    }

    // Additional methods like computeHashesPerSecond and repairChain would go here

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Block block : blockchain) {
            builder.append(block.toString()).append("\n");
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        BlockChain blockchain = new BlockChain();
        // Adding genesis block to the blockchain with a simple transaction
        blockchain.addBlock(new Block(0, new Timestamp(System.currentTimeMillis()), "Genesis", 2));

        while (true) {
            System.out.println("\n0. View basic blockchain status.");
            System.out.println("1. Add a transaction to the blockchain.");
            System.out.println("2. Verify the blockchain.");
            System.out.println("3. View the blockchain.");
            System.out.println("4. Corrupt the chain.");
            System.out.println("5. Hide the corruption by repairing the chain.");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 0:
                    printBlockchainStatus(blockchain);
                    break;
                case 1:
                    addTransaction(blockchain);
                    break;
                case 2:
                    verifyBlockchain(blockchain);
                    break;
                case 3:
                    System.out.println(blockchain);
                    break;
                case 4:
                    corruptBlockchain(blockchain);
                    break;
                case 5:
                    System.out.println("Repairing the entire chain");
                    repairChain(blockchain);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void printBlockchainStatus(BlockChain blockchain) {
        System.out.println("Current size of chain: " + blockchain.getChainSize());
        System.out.println("Difficulty of most recent block: " + blockchain.getLatestBlock().getDifficulty());
        System.out.println("Total difficulty for all blocks: " + blockchain.getTotalDifficulty());
        System.out.println("Experimented with: " + blockchain.numberOfHashes + " hashes");
        System.out.println("Approximate hashes per second on this machine: " + blockchain.getHashesPerSecond());
        System.out.println("Expected total hashes required for the whole chain: " + blockchain.getTotalExpectedHashes());
        System.out.println("Nonce for most recent block: " + blockchain.getLatestBlock().getNonce());
        System.out.println("Chain hash: " + blockchain.getChainHash());
    }

    // This method adds a new block to the blockchain
    private static void addTransaction(BlockChain blockchain) {
        System.out.print("Enter difficulty > ");
        Scanner scanner = new Scanner(System.in);
        int difficulty = scanner.nextInt();
        scanner.nextLine(); // Clean up newline
        System.out.print("Enter transaction: ");
        String transaction = scanner.nextLine();

        long startTime = System.currentTimeMillis();
        Block newBlock = new Block(blockchain.getChainSize(), new Timestamp(System.currentTimeMillis()), transaction, difficulty);
        blockchain.addBlock(newBlock);
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time to add this block was " + (endTime - startTime) + " milliseconds.");
    }

    // This method verifies the entire blockchain
    private static void verifyBlockchain(BlockChain blockchain) {
        String result = blockchain.isChainValid();
        System.out.println("Verifying entire chain");
        System.out.println("Chain verification: " + result);
    }

    // This method corrupts the blockchain
    private static void corruptBlockchain(BlockChain blockchain) {
        System.out.println("Currupt the Blockchain");
        System.out.print("Enter block ID of block to corrupt: ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        scanner.nextLine(); // Clean up newline
        if (id >= 0 && id < blockchain.getChainSize()) {
            System.out.print("Enter new data for block " + id + ": ");
            String newData = scanner.nextLine();
            Block blockToCorrupt = blockchain.getBlock(id);
            blockToCorrupt.setData(newData);
            System.out.println("Block " + id + " now holds: " + newData);
        } else {
            System.out.println("Invalid block ID.");
        }
    }

    // This method repairs the entire blockchain
    public static void repairChain(BlockChain blockchain) {
        for (int i = 1; i < blockchain.blockchain.size(); i++) {
            Block currentBlock = blockchain.blockchain.get(i);
            Block previousBlock = blockchain.blockchain.get(i - 1);
            if (!currentBlock.getPreviousHash().equals(previousBlock.calculateHash())) {
                currentBlock.setPreviousHash(previousBlock.calculateHash());
                currentBlock.proofOfWork();
            }
        }
    }
}
