package main;

/**
 * Created by alasdair on 07.07.18.
 */
public class KeyValueTuple {
    private String key;
    private String value;

    public KeyValueTuple(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
