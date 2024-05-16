package cmu.ds.project3;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class BlockChain {
    private ArrayList<Block> blockchain;
    private String chainHash;
    private int hashesPerSecond = 0;
    final long numberOfHashes = 2000000; // 2 million hashes

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

    public String isChainValid() {
        if (blockchain.isEmpty()) {
            return "Blockchain is empty";
        }
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            if (!currentBlock.getPreviousHash().equals(previousBlock.calculateHash())) {
                return "FALSE\nImproper hash on node " + (i-1) + " Does not begin with " + previousBlock.calculateHash();
            }
        }
        return "TRUE";
    }

    public void repairChain() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);
            if (!currentBlock.getPreviousHash().equals(previousBlock.calculateHash())) {
                currentBlock.setPreviousHash(previousBlock.calculateHash());
                currentBlock.proofOfWork();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Block block : blockchain) {
            builder.append(block.toString()).append("\n");
        }
        return builder.toString();
    }


}
