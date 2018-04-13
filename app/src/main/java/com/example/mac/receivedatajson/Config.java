package com.example.mac.receivedatajson;

public class Config {
    public static final int nodeQuantity = 2;
    public static final int sensorPerNode = 3;
    public static final float threshold0_Bui = 4;
    public static final float threshold1_Bui = 8;

    public static final int refreshRate = 10;            // 1 time / 5 secs
    public static final String dataUrl = "http://192.168.4.1:5000/data";
}
