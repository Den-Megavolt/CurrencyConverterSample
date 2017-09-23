package model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dkotov on 19-Sep-17.
 */

public class RateResponse {

    @SerializedName("base")
    private String base;

    @SerializedName("date")
    private String date;

    @SerializedName("rates")
    private HashMap<String, Double> rates;

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public HashMap<String, Double> getRates() {
        return rates;
    }
}
