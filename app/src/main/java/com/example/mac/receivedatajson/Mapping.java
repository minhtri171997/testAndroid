package com.example.mac.receivedatajson;

import android.app.ProgressDialog;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class Mapping extends AppCompatActivity implements FetchDataListener {
    private ProgressDialog progressDialog;
    private Handler fetchHandler;
    private Timer timer;
    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);

        init();
    }

    private void init() {
        fetchHandler = new Handler();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                fetchHandler.post(new Runnable() {
                    public void run() {
                        progressDialog = ProgressDialog.show(Mapping.this, "Please wait", "Loading...", true);
                        new FetchData(Mapping.this).execute();
                    }
                });
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.schedule(task, 0, Config.refreshRate * 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    private int getCondition(float value, float thres0, float thres1) {
        if (value > thres1) {
            return 2;
        } else if ((thres0 <= value) && (value <= thres1)) {
            return 1;
        } else {
            return 0;
        }
    }

    private int getColorCode(int sensor0_color, int sensor1_color, int sensor2_color) {
        int color = Math.max(sensor0_color, Math.max(sensor1_color, sensor2_color));
        switch (color) {
            case 0:
                return Color.GREEN;
            case 1:
                return Color.YELLOW;
            case 2:
                return Color.RED;
            default:
                break;
        }
        return Color.TRANSPARENT;
    }

    @Override
    public void onFetchDataCompleted(ArrayList<Float> dsensor) {
        progressDialog.dismiss();

        if (dsensor.size() == 0) {
            Toast.makeText(this, "Failed to load data...", Toast.LENGTH_SHORT).show();
            finish();
        }

        BitmapFactory.Options myOptions = new BitmapFactory.Options();
        myOptions.inDither = true;
        myOptions.inScaled = false;
        myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
        myOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cde,myOptions);

        ArrayList<Paint> paintList = new ArrayList<Paint>();


        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

        ArrayList<Integer> colorVal = new ArrayList();

        //TextView testvalue= (TextView)findViewById(R.id.textView);
        //testvalue.setText(String.valueOf((new Random()).nextInt(100) + 1));

        for (int i = 0; i < Config.nodeQuantity * Config.sensorPerNode; i = i + Config.sensorPerNode) {
            int sensor0 = getCondition(dsensor.get(i + 0), Config.threshold0_Bui, Config.threshold1_Bui);
            int sensor1 = getCondition(dsensor.get(i + 1), Config.threshold0_Bui, Config.threshold1_Bui);
            int sensor2 = getCondition(dsensor.get(i + 2), Config.threshold0_Bui, Config.threshold1_Bui);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(getColorCode(sensor0, sensor1, sensor2));
            paintList.add(paint);
        }

        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawCircle(428,340, 18, paintList.get(0));

        Canvas canvas1= new Canvas(mutableBitmap);
        canvas1.drawCircle(800,706,18, paintList.get(1));

        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(mutableBitmap);
    }
}