package cmu.ds.project3;

import java.util.Map;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 03/17/2024
 */
public class ResponseMessage {
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    private String status;
    private String message;
    private Map<String, String> data;

    public ResponseMessage(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseMessage(String status, String message, Map<String, String> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getData() {
        return data;
    }
}
