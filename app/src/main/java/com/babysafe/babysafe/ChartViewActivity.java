package com.babysafe.babysafe;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.babysafe.babysafe.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Connect.UbidotsClient;

/**
 * Created by Menginar on 23.3.2017.
 */

public class ChartViewActivity extends AppCompatActivity {


    private String APIKEY = "3kPo3rpXvaolxlLnsGBlBMTxxo2Btz";
    private String VARIABLEID = "";
    private String VARIABLENAME = "";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    // UI reference
    private BarChart barChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        setContentView(R.layout.fragment_chart);
        barChart = (BarChart) findViewById(R.id.chart);
        setupToolbar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //Geri tuşu eklendi

        try {
            Bundle extras = getIntent().getExtras();

            String sound = extras.getString("sound");
            String temp = extras.getString("temp");
            String motion = extras.getString("motion");
            String gas = extras.getString("gas");

            if (sound != null) {
                VARIABLEID = sound;
                VARIABLENAME = "Sound";
            } else if (temp != null) {
                VARIABLEID = temp;
                VARIABLENAME = "Temp";
            } else if (motion != null) {
                VARIABLEID = motion;
                VARIABLENAME = "Motion";
            } else if (gas != null) {
                VARIABLEID = gas;
                VARIABLENAME = "Gas";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        initChartBar(barChart);

        // Pressure
        ( new UbidotsClient() ).handleUbidots(VARIABLEID, APIKEY, new UbidotsClient.UbiListener() {
            @Override
            public void onDataReady(List<UbidotsClient.Value> result) {
                Log.d("Chart", "======== On data Ready ===========");
                List<BarEntry> entries = new ArrayList();
                List<String> labels = new ArrayList<String>();
                for (int i=0; i < result.size(); i++) {

                    BarEntry be = new BarEntry(result.get(i).value, i);
                    entries.add(be);
                    // Convert timestamp to date
                    Date d = new Date(result.get(i).timestamp);
                    // Create Labels
                    labels.add(sdf.format(d));
                }

                BarDataSet lse = new BarDataSet(entries, VARIABLENAME);

                lse.setDrawValues(false);
                if (VARIABLENAME.equals("Sound")) lse.setColor(Color.BLUE);
                if (VARIABLENAME.equals("Temp")) lse.setColor(Color.RED);
                if (VARIABLENAME.equals("Motion")) lse.setColor(Color.GREEN);
                if (VARIABLENAME.equals("Gas")) lse.setColor(Color.MAGENTA);


                BarData bd = new BarData(labels, lse);

                barChart.setData(bd);
                Handler handler = new Handler(getApplication().getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        barChart.invalidate();
                    }
                });
            }
        });
    }

    private void setupToolbar(){
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initChartBar(BarChart chart) {
        YAxis leftAxis = new YAxis();
        chart.setTouchEnabled(true);
        chart.setDrawGridBackground(true);
        chart.getAxisRight().setEnabled(false);
        chart.setDrawGridBackground(true);

        if(VARIABLENAME == "Sound" || VARIABLENAME == "Motion"){
            leftAxis.setAxisMaxValue(1F);
            leftAxis.setAxisMinValue(0F);

        } else if(VARIABLENAME == "Gas"){
            leftAxis.setAxisMaxValue(500F);
            leftAxis.setAxisMinValue(1F);

        } else {
            leftAxis.setAxisMaxValue(100F);
            leftAxis.setAxisMinValue(1F);

        }
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisLineWidth(2);
        leftAxis.setDrawGridLines(true);

        // X-Axis
        XAxis xAxis = chart.getXAxis();
        xAxis.resetLabelsToSkip();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
    }
}
