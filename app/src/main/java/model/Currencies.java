package model;

/**
 * Created by dkotov on 20-Sep-17.
 */

public enum Currencies {

    USD("US Dollar"),
    SEK("Swedish Krona");

    private String name;

    private Currencies(String stringVal) {
        name = stringVal;
    }

    public String toString() {
        return name;
    }

    public static String getEnumByString(String code) {
        for (Currencies e : Currencies.values()) {
            if (code.equalsIgnoreCase(e.name))
                return e.name();
        }

        return null;
    }

}
