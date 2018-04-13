package com.example.mac.receivedatajson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FetchDataListener {
    Button click;
    //public static TextView data;
    public TextView data;
    public Button button2;
    public void init(){
        button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            Intent abc = new Intent(MainActivity.this,Mapping.class);
            startActivity(abc);

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        click=(Button) findViewById(R.id.button);
        data=(TextView) findViewById(R.id.fetcheddata);

        click.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FetchData process = new FetchData(MainActivity.this);
                process.execute();

            }
        });
    }

    @Override
    public void onFetchDataCompleted(ArrayList<Float> dsensor) {
        data.setText(String.valueOf(dsensor.size()));
    }

}
