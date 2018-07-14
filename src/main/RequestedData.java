package main;

import org.json.simple.JSONObject;

import java.math.BigInteger;

/**
 * Created by alasdair on 08.07.18.
 */
public class RequestedData {
    private String requestedWallet;
    private boolean personalInformation;
    private boolean creditInformation;
    private JSONObject privateKey;
    private JSONObject reqestedInTransaction;
    private int encryptedPersonal = 0;
    private int encryptedCredit = 0;
    private int decryptedPersonal;
    private int decryptedCredit;

    public boolean isHasPrivateKey() {
        return hasPrivateKey;
    }

    public int getDecryptedPersonal() {
        return decryptedPersonal;
    }

    public int getDecryptedCredit() {
        return decryptedCredit;
    }

    private boolean hasPrivateKey = false;

    public RequestedData(String requestedWallet, boolean personalInformation, boolean creditInformation, JSONObject reqestedInTransaction) {
        this.requestedWallet = requestedWallet;
        this.personalInformation = personalInformation;
        this.creditInformation = creditInformation;
        this.reqestedInTransaction = reqestedInTransaction;
    }

    public String getRequestedWallet() {
        return requestedWallet;
    }

    public boolean isPersonalInformation() {
        return personalInformation;
    }

    public boolean isCreditInformation() {
        return creditInformation;
    }

    public JSONObject getPrivateKey() {
        return privateKey;
    }

    public JSONObject getReqestedInTransaction() {
        return reqestedInTransaction;
    }

    public int getEncryptedPersonal() {
        return encryptedPersonal;
    }

    public int getEncryptedCredit() {
        return encryptedCredit;
    }

    public void setEncryptedPersonal(int encryptedPersonal) {
        this.encryptedPersonal = encryptedPersonal;
        if (hasPrivateKey && encryptedPersonal > 0){
            Long e = (Long) privateKey.get("e");
            Long n = (Long) privateKey.get("n");
            BigInteger b = BigInteger.valueOf(encryptedPersonal).pow(e.intValue());
            BigInteger b2 = BigInteger.valueOf(n.intValue());
            decryptedPersonal = b.mod(b2).intValue();
        }
    }

    public void setEncryptedCredit(int encryptedCredit) {
        this.encryptedCredit = encryptedCredit;
        if (hasPrivateKey && encryptedCredit > 0){
            Long e = (Long) privateKey.get("e");
            Long n = (Long) privateKey.get("n");
            BigInteger b = BigInteger.valueOf(encryptedCredit).pow(e.intValue());
            BigInteger b2 = BigInteger.valueOf(n.intValue());
            decryptedCredit = b.mod(b2).intValue();
        }
    }

    public void setPrivateKey(JSONObject privateKey) {
        this.privateKey = privateKey;
        hasPrivateKey = true;
    }

    @Override
    public String toString() {
        String temp="";
        if (personalInformation && ! creditInformation)
            temp = "   Personliche";
        else if (creditInformation && !personalInformation)
            temp = "   Kredit";
        else if (creditInformation && personalInformation)
            temp = "   Personliche & Kredit";
        return "Wallet: " + requestedWallet + " ;" +
                temp + " Infos";
    }
}
