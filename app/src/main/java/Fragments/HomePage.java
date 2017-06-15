package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.babysafe.babysafe.ChartViewActivity;
import com.babysafe.babysafe.R;

import static com.babysafe.babysafe.MyService.variables;

/**
 * Created by ss on 6.3.2017.
 */

public class HomePage extends Fragment {

    public static TextView soundTitle, soundValue, motionTitle, motionValue, gasTitle, gasValue, tempTitle, tempValue, tempDegree;
    ImageView soundImage, motionImage, gasImage, tempImage;
    Button soundButton, tempButton, motionButton, gasButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_sound_sensor, container, false);

        soundValue = (TextView) view.findViewById(R.id.sound_value);
        soundTitle = (TextView) view.findViewById(R.id.sound_title);
        soundImage = (ImageView) view.findViewById(R.id.sound_image);
        //soundDegree = (TextView) view.findViewById(R.id.sound_degree);

        motionValue = (TextView) view.findViewById(R.id.motion_value);
        motionTitle = (TextView) view.findViewById(R.id.motion_title);
        motionImage = (ImageView) view.findViewById(R.id.motion_image);
        //motionDegree = (TextView) view.findViewById(R.id.motion_degree);

        gasValue = (TextView) view.findViewById(R.id.gas_value);
        gasTitle = (TextView) view.findViewById(R.id.gas_title);
        gasImage = (ImageView) view.findViewById(R.id.gas_image);
        //gasDegree = (TextView) view.findViewById(R.id.gas_degree);

        tempValue = (TextView) view.findViewById(R.id.temp_value);
        tempTitle = (TextView) view.findViewById(R.id.temp_title);
        tempImage = (ImageView) view.findViewById(R.id.temp_image);
        tempDegree = (TextView) view.findViewById(R.id.temp_degree);

        soundButton = (Button) view.findViewById(R.id.sound_button);
        tempButton = (Button) view.findViewById(R.id.temp_button);
        motionButton = (Button) view.findViewById(R.id.motion_button);
        gasButton = (Button) view.findViewById(R.id.gas_button);

        soundTitle.setText("SES");
        motionTitle.setText("HAREKET");
        gasTitle.setText("YANICI GAZ DEĞERİ");
        tempTitle.setText("SICAKLIK DEĞERİ");

        //soundDegree.setText("dB");
        tempDegree.setText("°C");



        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chartIntent = new Intent(getContext(), ChartViewActivity.class);
                chartIntent.putExtra("sound", variables[0].getId().toString());
                startActivity(chartIntent);

            }
        });

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chartIntent = new Intent(getContext(), ChartViewActivity.class);
                chartIntent.putExtra("temp", variables[3].getId().toString());
                startActivity(chartIntent);
            }
        });

        motionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chartIntent = new Intent(getContext(), ChartViewActivity.class);
                chartIntent.putExtra("motion", variables[1].getId().toString());
                startActivity(chartIntent);
            }
        });

        gasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chartIntent = new Intent(getContext(), ChartViewActivity.class);
                chartIntent.putExtra("gas", variables[2].getId().toString());
                startActivity(chartIntent);
            }
        });

        return view;
    }
}
