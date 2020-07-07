package com.example.munf_project_v2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.sql.Array;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class FeedbackFragment extends Fragment {

    private SensorViewModel sensorViewModel;

    private MediaServiceConnection mediaServiceConnection = null;
    private MediaService.MediaBinder mediaBinder;

    int x_abschnitt = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feedback,container,false);

        // mitgeschickten Daten vom ersten Fragment auslesen:
        Bundle args = getArguments();
        FeedbackFragmentArgs feedbackFragmentArgs = null;

        if(args != null)
            feedbackFragmentArgs = FeedbackFragmentArgs.fromBundle(args);
        if(feedbackFragmentArgs != null){
            TextView tv = v.findViewById(R.id.tv_feedback_fragment);
            tv.setText(feedbackFragmentArgs.getDisplayString());
        };
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final TextView sensor_xyz = view.findViewById(R.id.tv_feedbacksensor_xyz);
        final TextView feedback_x = view.findViewById(R.id.tv_feedback_x);
        final TextView feedback_y = view.findViewById(R.id.tv_feedback_y);
        final TextView feedback_z = view.findViewById(R.id.tv_feedback_z);


        LineChart lineChart_x = view.findViewById(R.id.liveChart_x);
        Description desc_x = new Description();
        desc_x.setText("Feedback Beschleunigung an der X-Achse");
        lineChart_x.setDescription(desc_x);
        lineChart_x.setDrawGridBackground(false);


        LineChart lineChart_y = view.findViewById(R.id.liveChart_y);
        Description desc_y = new Description();
        desc_y.setText("Feedback Beschleunigung an der Y-Achse");
        lineChart_y.setDescription(desc_y);
        lineChart_y.setDrawGridBackground(false);

        LineChart lineChart_z = view.findViewById(R.id.liveChart_z);
        Description desc_z = new Description();
        desc_z.setText("Feedback Beschleunigung an der Z-Achse");
        lineChart_z.setDescription(desc_z);
        lineChart_z.setDrawGridBackground(false);

        ArrayList<Entry> x_values = new ArrayList<Entry>();
        ArrayList<Entry> y_values = new ArrayList<Entry>();
        ArrayList<Entry> z_values = new ArrayList<Entry>();


        sensorViewModel = new ViewModelProvider(this,
                ViewModelProvider
                        .AndroidViewModelFactory
                        .getInstance(getActivity()
                                .getApplication()))
                .get(SensorViewModel.class);

        sensorViewModel.accelerationLiveData.observe(getViewLifecycleOwner(),(accelerationInformation)->{
            sensor_xyz.setText(
                    "x:" + accelerationInformation.getX() + " y " + accelerationInformation.getY() + " z "+accelerationInformation.getZ()
                    // UNNÖTIG - weg?
            ); // ACHTUNG: string in stringfile extrahieren!

            x_values.add(new Entry(x_abschnitt,accelerationInformation.getX()));
            y_values.add(new Entry(x_abschnitt, accelerationInformation.getY()));
            z_values.add(new Entry(x_abschnitt, accelerationInformation.getZ()));
            x_abschnitt = x_abschnitt + 1;


            LineDataSet lineDataSet_x = new LineDataSet(x_values, "a");
            lineDataSet_x.setColor(Color.BLUE);
            lineDataSet_x.setDrawCircles(false);
            lineDataSet_x.setDrawCircleHole(false);
            lineDataSet_x.setDrawValues(false);


            if(accelerationInformation.getX() > 2){
                lineDataSet_x.setColor(Color.RED);
                lineDataSet_x.setDrawValues(true);

                feedback_x.setText("Deine Beschleunigungswerte sind zu hoch!");
                feedback_x.setBackgroundColor(Color.RED);

                // mediaBinder.play(R.raw.ding);


                // AUDIO FEEDBACK einbinden?
            }
            else{
                feedback_x.setText("Du hast eine gute Beschleunigung. Weiter so!");
                feedback_x.setBackgroundColor(Color.WHITE);
            }


            LineDataSet lineDataSet_y = new LineDataSet(y_values, "a");
            lineDataSet_y.setColor(Color.BLUE);
            lineDataSet_y.setDrawCircles(false);
            lineDataSet_y.setDrawCircleHole(false);
            lineDataSet_y.setDrawValues(false);

            if(accelerationInformation.getY() > 2){
                lineDataSet_y.setColor(Color.RED);
                lineDataSet_y.setDrawValues(true);

                feedback_y.setText("Deine Beschleunigungswerte sind zu hoch!");
                feedback_y.setBackgroundColor(Color.RED);

                if(mediaBinder == null) return;
                mediaBinder.play(R.raw.ding);

                // AUDIO FEEDBACK einbinden?
            }
            else{
                feedback_y.setText("Du hast eine gute Beschleunigung. Weiter so!");
                feedback_y.setBackgroundColor(Color.WHITE);
            }

            LineDataSet lineDataSet_z = new LineDataSet(z_values, "a");
            lineDataSet_z.setColor(Color.BLUE);
            lineDataSet_z.setDrawCircles(false);
            lineDataSet_z.setDrawCircleHole(false);
            lineDataSet_z.setDrawValues(false);

            if(accelerationInformation.getZ() > 2){
                lineDataSet_z.setColor(Color.RED);
                lineDataSet_z.setDrawValues(true);

                feedback_z.setText("Deine Beschleunigungswerte sind zu hoch!");
                feedback_z.setBackgroundColor(Color.RED);

                // mediaBinder.play(R.raw.ding);

                // AUDIO FEEDBACK einbinden?
            }
            else{
                feedback_z.setText("Du hast eine gute Beschleunigung. Weiter so!");
                feedback_z.setBackgroundColor(Color.WHITE);
            }

            LineData data_x = new LineData(lineDataSet_x);
            LineData data_y = new LineData(lineDataSet_y);
            LineData data_z = new LineData(lineDataSet_z);

            lineChart_x.setData(data_x);
            lineChart_x.invalidate();

            lineChart_y.setData(data_y);
            lineChart_y.invalidate();

            lineChart_z.setData(data_z);
            lineChart_z.invalidate();


        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if(mediaServiceConnection == null) {
            getActivity().bindService(new Intent(getContext(), MediaService.class),
                    mediaServiceConnection = new MediaServiceConnection(),
                    Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mediaServiceConnection != null){
            getActivity().unbindService(mediaServiceConnection);
            mediaServiceConnection = null;
        }
    }

    private final class MediaServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mediaBinder = (MediaService.MediaBinder) iBinder;



        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mediaBinder = null;

        }
    }


}
