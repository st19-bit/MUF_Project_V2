package com.example.munf_project_v2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
        final NavController controller = Navigation.findNavController(view);

        final TextView sensor_xyz = view.findViewById(R.id.tv_feedbacksensor_xyz);
        final TextView feedback_x = view.findViewById(R.id.tv_feedback_x);
        final TextView feedback_y = view.findViewById(R.id.tv_feedback_y);
        final TextView feedback_z = view.findViewById(R.id.tv_feedback_z);
        final ImageButton button_back = view.findViewById(R.id.backbutton);


        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.navigate(R.id.action_feedbackFragment_to_monitoringFragment);
            }
        });



        LineChart lineChart_x = view.findViewById(R.id.liveChart_x);
        Description desc_x = new Description();
        desc_x.setText("");
        lineChart_x.setDescription(desc_x);
        lineChart_x.setDrawGridBackground(false);


        LineChart lineChart_y = view.findViewById(R.id.liveChart_y);
        Description desc_y = new Description();
        desc_y.setText("");
        lineChart_y.setDescription(desc_y);
        lineChart_y.setDrawGridBackground(false);

        LineChart lineChart_z = view.findViewById(R.id.liveChart_z);
        Description desc_z = new Description();
        desc_z.setText("");
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

            LineDataSet lineDataSet_x = new LineDataSet(x_values, "x-axis");
            lineDataSet_x.setColor(Color.GREEN);
            lineDataSet_x.setDrawCircles(false);
            lineDataSet_x.setDrawCircleHole(false);
            lineDataSet_x.setDrawValues(false);

            if(accelerationInformation.getX() > 2) {

                if(mediaBinder == null) return;
                mediaBinder.play(R.raw.ding);

                lineDataSet_x.setColor(Color.RED);
                lineDataSet_x.setDrawValues(true);

                feedback_x.setText(R.string.Attention);
                feedback_x.setTextColor(Color.RED);
            }

            else {
                feedback_x.setText(R.string.Keepgoing);
                feedback_x.setTextColor(Color.GREEN);
            }



            LineDataSet lineDataSet_y = new LineDataSet(y_values, "y-axis");
            lineDataSet_y.setColor(Color.GREEN);
            lineDataSet_y.setDrawCircles(false);
            lineDataSet_y.setDrawCircleHole(false);
            lineDataSet_y.setDrawValues(false);

            if(accelerationInformation.getY() > 2) {

                if(mediaBinder == null) return;
                mediaBinder.play(R.raw.ding);

                lineDataSet_y.setColor(Color.RED);
                lineDataSet_y.setDrawValues(true);

                feedback_y.setText(R.string.Attention);
                feedback_y.setTextColor(Color.RED);
            }

            else {
                feedback_y.setText(R.string.Keepgoing);
                feedback_y.setTextColor(Color.GREEN);
            }



            LineDataSet lineDataSet_z = new LineDataSet(z_values, "z-axis");
            lineDataSet_z.setColor(Color.GREEN);
            lineDataSet_z.setDrawCircles(false);
            lineDataSet_z.setDrawCircleHole(false);
            lineDataSet_z.setDrawValues(false);

            if(accelerationInformation.getZ() > 2){

                if(mediaBinder == null) return;
                mediaBinder.play(R.raw.ding);

                lineDataSet_z.setColor(Color.RED);
                lineDataSet_z.setDrawValues(true);

                feedback_z.setText(R.string.Attention);
                feedback_z.setTextColor(Color.RED);

                // AUDIO FEEDBACK einbinden?
            }

            else {
                feedback_z.setText(R.string.Keepgoing);
                feedback_z.setTextColor(Color.GREEN);
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

    private final class MediaServiceConnection implements ServiceConnection {

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
