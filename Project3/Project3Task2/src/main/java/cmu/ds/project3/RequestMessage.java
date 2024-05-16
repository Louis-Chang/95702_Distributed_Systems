package cmu.ds.project3;

public class RequestMessage {
    public static final String VIEW_BLOCKCHAIN_STATUS = "viewBlockchainStatus";
    public static final String ADD_TRANSACTION = "addTransaction";
    public static final String VERIFY_BLOCKCHAIN = "verifyBlockchain";
    public static final String VIEW_BLOCKCHAIN = "viewBlockchain";
    public static final String CORRUPT_BLOCKCHAIN = "corruptBlockchain";
    public static final String REPAIR_CHAIN = "repairChain";

    private String action;
    private String data;
    private int difficulty;
    private int id; // For corrupting a block
    private String newData; // For corrupting a block

    public RequestMessage(String action) {
        this.action = action;
    }

    public RequestMessage(String action, String data, int difficulty) {
        this.action = action;
        this.data = data;
        this.difficulty = difficulty;
    }

    public RequestMessage(String action, int id, String newData) {
        this.action = action;
        this.id = id;
        this.newData = newData;
    }

    /**
     * Concatenate the values of the fields
     * @return
     */
    public String concatenateValues() {
        StringBuilder sb = new StringBuilder();
        if (data != null) {
            sb.append(data);
        }
        if (difficulty != 0) { // suppose difficulty is 0 means not set or not applicable
            sb.append(difficulty);
        }
        if (id != 0) { // suppose id is 0 means not set or not applicable
            sb.append(id);
        }
        if (newData != null) {
            sb.append(newData);
        }
        return sb.toString();
    }

    // Additional getters for the new fields
    public int getId() {
        return id;
    }

    public String getNewData() {
        return newData;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }
}
