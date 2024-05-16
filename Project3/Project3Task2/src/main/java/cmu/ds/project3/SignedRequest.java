package cmu.ds.project3;

import java.math.BigInteger;

/**
 * Author: Louis Chang (hungyic)
 * Last Modified: 03/17/2024
 */
public class SignedRequest {
    private BigInteger e;
    private BigInteger n;
    private BigInteger clientID;
    private RequestMessage request;
    private String signature;

    public SignedRequest(BigInteger e, BigInteger n, BigInteger clientID, RequestMessage request, String signature) {
        this.e = e;
        this.n = n;
        this.clientID = clientID;
        this.request = request;
        this.signature = signature;
    }

    // Getters
    public BigInteger getE() {
        return e;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getClientID() {
        return clientID;
    }

    public RequestMessage getRequest() {
        return request;
    }

    public String getSignature() {
        return signature;
    }
}
