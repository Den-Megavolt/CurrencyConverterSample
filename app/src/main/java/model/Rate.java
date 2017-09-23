package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dkotov on 20-Sep-17.
 */

public class Rate {

    @SerializedName("name")
    String name;

    @SerializedName("value")
    double value;

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
}
