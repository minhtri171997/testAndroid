package com.example.mac.receivedatajson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FetchData extends AsyncTask<Void,Void,ArrayList<Float>> {
    private String dataParsed = "";
    private FetchDataListener fetchDataListener;

    public FetchData(FetchDataListener fetchDataListener) {
        this.fetchDataListener = fetchDataListener;
    }

    @Override
    protected ArrayList<Float> doInBackground(Void... voids){
        ArrayList<Float> dsensor = new ArrayList<>();
        try {
            URL url= new URL(Config.dataUrl);
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            InputStream inputstream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader (new InputStreamReader(inputstream));
            String line = "";
            String data = "";
            while (line != null)
            {
                line = bufferedReader.readLine();
                data = data + line;
            }
            JSONArray JA= new JSONArray(data);

            for (int i=0; i<JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                String singleParsed = "Name :" + JO.get("name")+"\n"+
                        "Sensor 0 value :" + JO.get("sensor0_val")+ "\n"+
                        "Sensor 1 value :" + JO.get("sensor1_val")+ "\n"+
                        "Sensor 2 value :" + JO.get("sensor2_val") + "\n";

                String data_sensor0 = JO.get("sensor0_val").toString();
                String data_sensor1 = JO.get("sensor1_val").toString();
                String data_sensor2 = JO.get("sensor2_val").toString();

                dsensor.add(Float.valueOf(data_sensor0));
                dsensor.add(Float.valueOf(data_sensor1));
                dsensor.add(Float.valueOf(data_sensor2));

                dataParsed = dataParsed + singleParsed +"\n";
            }
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }

        return dsensor;
    }
    @Override
    protected void onPostExecute(ArrayList<Float> dsensor){
        super.onPostExecute(dsensor);

        fetchDataListener.onFetchDataCompleted(dsensor);
    }
}
