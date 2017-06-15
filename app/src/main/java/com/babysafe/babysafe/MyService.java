package com.babysafe.babysafe;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.babysafe.babysafe.R;
import com.ubidots.ApiClient;
import com.ubidots.DataSource;
import com.ubidots.Value;
import com.ubidots.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Fragments.HomePage;

/**
 * Created by ss on 10.3.2017.
 */

public class MyService extends Service {
    Timer timer;
    Value[] values;
    public double firstValue, secondValue, previousSound, previousMotion, previousGas, previousTemp;
    public int TIME = 1000;
    private MyDBHandler dbHandler;
    public static String VARIABLE_ID;
    public static Variable[] variables;
    public static int numMessages = 0;
    int index = 0, i;
    int notifyID = 0, notiSound = 0, notiMotion = 1, notiGas = 2,notiTemp = 3;
    public static ArrayList<String> noti = new ArrayList<>();
    public static HashMap<String, String> notificationMap = new HashMap<>();
    NotificationManager manager = null;
    NotificationCompat.Builder mBuilder = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        timer = new Timer();
        //Toast.makeText(this, "Servis Çalıştı.Bu Mesaj Servis Class'dan", Toast.LENGTH_LONG).show();
        dbHandler = new MyDBHandler(this, null, null, 1);

        timer.schedule(new TimerTask() {  //her 1 sn de bir kontrol et
            @Override
            public void run() {

                if (isNetworkAvailable()) {
                    new ApiUbidots().execute();
                }
            }
        }, 0, TIME);


    }

    @Override
    public void onDestroy() {       //Servis stopService(); metoduyla durdurulduğunda çalışır
        timer.cancel();
        Toast.makeText(this, "Servis Durduruldu.Bu Mesaj Servis Class'dan", Toast.LENGTH_LONG).show();
    }

    private class ApiUbidots extends AsyncTask<String, Void, String> {
        private ArrayList<String> dgr = new ArrayList<>();


        @Override
        protected String doInBackground(String... params) {

            try {
                ApiClient apiClient = new ApiClient(LoginActivity.API_KEY);
                DataSource[] dataSources= apiClient.getDataSources();      //Device "id" leri burada alınıyor.
                String token = dbHandler.getTokenFind();

                /*for(String key : LoginActivity.ubiDataSources.keySet()){
                    if ( key.equals(token) ){
                        variables = LoginActivity.ubiDataSources.get(key).getVariables();
                        break;
                    }
                }*/

                for (DataSource dataSource : dataSources) {
                    if (dataSource.getName().equals(token)) {
                        variables = dataSource.getVariables();
                        break;
                    }
                }

                for (i = 0; i < variables.length; i++) {
                    VARIABLE_ID = variables[i].getId().toString();  //Sensör "id" leri burada alınıyor.
                    values = apiClient.getVariable(VARIABLE_ID).getValues();
                    firstValue = values[0].getValue();
                    secondValue = values[1].getValue();

                    dgr.add(i, Double.toString(firstValue));

                    if ( i == 0 && firstValue == 1){
                        previousSound = secondValue;
                        if(previousSound != firstValue){
                            //addNotification("Odada ses algılandı.");
                            addNotification1("Odadaki ses algılandı. ", 0);
                        }
                    }
                    else if ( i == 1 && firstValue == 1){
                        previousMotion = secondValue;
                        if (previousMotion != firstValue) {
                            //addNotification("Odada hareket algılandı.");
                            addNotification1("Odada hareket algılandı.", 1);
                        }
                    }
                    else if( i == 2 && firstValue == 1){
                        previousGas = secondValue;
                        if (previousGas != firstValue) {
                            //addNotification("Odanın gaz seviyesi yükseldi.");
                            addNotification1("Odanın gaz seviyesi yükseldi.", 2);
                        }
                    }
                    else if( i == 3 && (firstValue < 20 || firstValue > 27)){
                        previousTemp = secondValue;
                        if (previousTemp != firstValue) {
                            String tempValue = String.valueOf(firstValue);
                            //addNotification("Odanın sıcaklık değeri : " + tempValue);
                            addNotification1("Odanın sıcaklık değeri : " + tempValue, 3);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "mengi";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                if (dgr.get(0).equals("1.0")) {
                    HomePage.soundValue.setText("VAR");
                } else {
                    HomePage.soundValue.setText("YOK");
                }

                if (dgr.get(1).equals("1.0")) {
                    HomePage.motionValue.setText("VAR");
                } else {
                    HomePage.motionValue.setText("YOK");
                }

                if (dgr.get(2).equals("1.0")) {
                    HomePage.gasValue.setText("YÜKSEK");
                } else {
                    HomePage.gasValue.setText("NORMAL");
                }
                //HomePage.soundValue.setText(dgr.get(0));
                //HomePage.motionValue.setText(dgr.get(1));
                //HomePage.gasValue.setText(dgr.get(2));
                HomePage.tempValue.setText(dgr.get(3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void addNotification (String message) {

        int notifyID = 1;
        manager.cancelAll();

        mBuilder.setSmallIcon(R.drawable.ic_menu_white_24dp)
                .setContentTitle("Baby Safe'den size haberler var!");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message).append(" ");
        //stringBuilder.append(state != null ? state : deg);

        notificationMap.put(message, stringBuilder.toString());

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Baby Safe'den size haberler var!");
        inboxStyle.setSummaryText("Baby Safe");

        for (String key : notificationMap.keySet()) {
            inboxStyle.addLine(notificationMap.get(key));
        }

        mBuilder.setStyle(inboxStyle);
        Intent notificationIntent;
        if (LoginActivity.user != null) {
            notificationMap.clear();
            notificationIntent = new Intent(this, MainActivity.class);
        } else {
            notificationIntent = new Intent(this, LoginActivity.class);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        manager.notify(++notifyID, mBuilder.build());
    }

    private void addNotification1 (String message, int notifyID) {


        NotificationCompat.Builder builder;

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_white_24dp)
                .setContentTitle("Baby Safe'den size haberler var!")
                .setContentText(message);

        Intent notificationIntent;

        if (LoginActivity.user != null) {

            notificationIntent = new Intent(this, MainActivity.class);

        } else {
            notificationIntent = new Intent(this, LoginActivity.class);
        }

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notifyID, builder.build());
    }
}