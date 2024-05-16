package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;


class MainTest {
    private BlockChain blockchain;

    @BeforeEach
    void setUp() {
        blockchain = new BlockChain();
        // Initialize with a genesis block to simulate starting scenario
        Block genesisBlock = new Block(0, new Timestamp(System.currentTimeMillis()), "Genesis", 0);
        blockchain.addBlock(genesisBlock);
    }
    @Test
    void testAddingTransactionsAndVerifyingBlockchain() {
        Block transaction1 = new Block(1, new Timestamp(System.currentTimeMillis()), "Alice pays Bob 100 DSCoin", 0);
        blockchain.addBlock(transaction1);
        assertEquals(2, blockchain.getChainSize(), "Blockchain should contain 2 blocks including the genesis block.");

        Block transaction2 = new Block(2, new Timestamp(System.currentTimeMillis()), "Bob pays Carol 20 DSCoin", 0);
        blockchain.addBlock(transaction2);
        assertEquals(3, blockchain.getChainSize(), "Blockchain should contain 3 blocks after second transaction.");

        String isValid = blockchain.isChainValid();
        assertEquals("TRUE", isValid, "Blockchain should be valid after adding transactions.");
    }

    @Test
    void testCorruptingAndRepairingBlockchain() {
        testAddingTransactionsAndVerifyingBlockchain(); // Use the method to add transactions.

        // Corrupt the chain by changing the data of a block.
        Block corruptBlock = blockchain.getBlock(1); // Get the first transaction block.
        assertNotNull(corruptBlock, "Block should not be null for corruption test.");
        corruptBlock.setData("Corrupted data");

        assertNotEquals("TRUE", blockchain.isChainValid(), "Blockchain should be invalid after corruption.");

        // Simulate repair by resetting the data and recalculating the hash
        // This is where you'd implement or call blockchain.repairChain() if it were implemented
        // For demonstration, manually correct and recalculate hash of the corrupted block
        corruptBlock.setData("Alice pays Bob 100 DSCoin"); // Resetting data to original
        corruptBlock.proofOfWork(); // Recompute proof of work for this block (or manually set the nonce if proofOfWork is too costly for testing)

        assertEquals("TRUE", blockchain.isChainValid(), "Blockchain should be valid after manual repair.");
    }

}