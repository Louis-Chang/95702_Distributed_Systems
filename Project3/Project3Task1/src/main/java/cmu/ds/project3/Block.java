package cmu.ds.project3;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 03/17/2024
 */
public class Block {
    private int index;
    private Timestamp timestamp;
    private String data;
    private String previousHash;
    private BigInteger nonce;
    private int difficulty;
    private String hash;

    public Block(int index, Timestamp timestamp, String data, int difficulty) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;
        this.previousHash = "0"; // Genesis block
        this.nonce = new BigInteger("0");
        this.hash = calculateHash();
    }

    public String calculateHash() {
        /*
         * The calculateHash method is used to calculate the hash of the block. It takes no parameters and returns a string.
         */
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Create a new SHA-256 digest
            String input = index + timestamp.toString() + data + previousHash + nonce + difficulty; // Create a string to hash
            byte[] hash = digest.digest(input.getBytes()); // Hash the input string
            StringBuilder hexString = new StringBuilder(); // Create a new string builder

            // Convert the hash to a hex string
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b); // Convert the byte to a hex string
                if (hex.length() == 1) hexString.append('0'); // Add a leading 0 if the hex string is only one character
                hexString.append(hex); // Add the hex string to the string builder
            }
            return hexString.toString(); // Return the hex string

            // Catch the NoSuchAlgorithmException
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e); // Throw a runtime exception
        }
    }

    // The proof of work methods finds a good hash.
    public String proofOfWork() {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while(!hash.substring(0, difficulty).equals(target)) {
            nonce = nonce.add(BigInteger.ONE);
            hash = calculateHash();
        }
        return hash;
    }

    // Getters and Setters
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getPreviousHash() { return previousHash; }
    public void setPreviousHash(String previousHash) { this.previousHash = previousHash; }
    public BigInteger getNonce() { return nonce; }
    public int getDifficulty() { return difficulty; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

    @Override
    public String toString() {
        // Simplified JSON-like representation
        return String.format("{\"index\":%d,\"timestamp\":\"%s\",\"data\":\"%s\",\"previousHash\":\"%s\",\"nonce\":\"%s\",\"difficulty\":%d,\"hash\":\"%s\"}",
                index, timestamp.toString(), data, previousHash, nonce.toString(), difficulty, hash);
    }

}
