package model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dkotov on 20-Sep-17.
 */

public class MessageEvent {

    private final int status;
    private final HashMap<String, Double> rates;

    public MessageEvent(int status, HashMap<String, Double> rates) {
        this.status = status;
        this.rates = rates;
    }

    public int getStatus() {
        return status;
    }

    public HashMap<String, Double> getRates() {
        return rates;
    }
}
