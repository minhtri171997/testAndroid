package com.example.mac.receivedatajson;

import java.util.ArrayList;

public interface FetchDataListener {
    void onFetchDataCompleted(ArrayList<Float> dsensor);
}
