package main;

import org.json.simple.JSONObject;

/**
 * Created by alasdair on 08.07.18.
 */
public class RequestData {
    private String requestWallet;
    private boolean personalInformation;
    private boolean creditInformation;
    private JSONObject publicKey;
    private JSONObject referencedTransaction;

    public RequestData(String requestWallet, boolean personalInformation, boolean creditInformation, JSONObject publicKey, JSONObject referencedTransaction) {
        this.requestWallet = requestWallet;
        this.personalInformation = personalInformation;
        this.creditInformation = creditInformation;
        this.publicKey = publicKey;
        this.referencedTransaction = referencedTransaction;
    }

    public String getRequestWallet() {
        return requestWallet;
    }

    public boolean isPersonalInformation() {
        return personalInformation;
    }

    public boolean isCreditInformation() {
        return creditInformation;
    }

    public JSONObject getPublicKey() {
        return publicKey;
    }

    public JSONObject getReferencedTransaction() {
        return referencedTransaction;
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
        return "Wallet: " + requestWallet + " ;" +
                temp + " Infos";
    }
}
