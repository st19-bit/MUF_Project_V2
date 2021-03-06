package com.example.munf_project_v2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;


public class MonitoringFragment extends Fragment {

    private SensorViewModel sensorViewModel;
    private SenViewModel senViewModel;
    private Observer<AccelerationInformation> observer;

    private MediaServiceConnection mediaServiceConnection = null;
    private MediaService.MediaBinder mediaBinder;

    private TextToSpeech textToSpeech;

    private ArrayList<Depot> datalist;
    private int count = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        senViewModel = new ViewModelProvider(
                getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
        ).get(SenViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitoring,container,false);
        return view;
    }

    // Sensordaten anhand der LiveData auslesen:

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final NavController controller = Navigation.findNavController(view);

        final Button button_start = view.findViewById(R.id.button_start);
        final Button button_stop = view.findViewById(R.id.button_stop);
        final Button button_change_fragment = view.findViewById(R.id.button_to_feedback);
        final Button button_change_to_database = view.findViewById(R.id.button_to_database);
        final ImageButton backtostart = view.findViewById(R.id.backtostartbutton);


        observer = null;
        datalist = new ArrayList<>();


        // ++++++++++++ BARCHART ++++++++++++
        final BarChart barChart = view.findViewById(R.id.livedata_barchart);
        barChart.setNoDataText(getString(R.string.start_measurement));
        barChart.setBackgroundColor(Color.TRANSPARENT);
        // Setup für Barchart

        ArrayList<BarEntry> entries = new ArrayList<>();

        final ArrayList<String> xAxisLabel = new ArrayList<>();

        // ++++++++++++++ BARCHART +++++++++++++

        final TextView sensor_xzy = view.findViewById(R.id.tv_acceleration_xyz);

        sensorViewModel = new ViewModelProvider(this,
                ViewModelProvider
                        .AndroidViewModelFactory
                        .getInstance(getActivity()
                                .getApplication()))
                .get(SensorViewModel.class);


        button_change_fragment.setOnClickListener(v -> {
            controller.navigate(MonitoringFragmentDirections
                    .actionMonitoringFragmentToFeedbackFragment()
                    .setDisplayString("Feedback View"));sensorViewModel.accelerationLiveData.removeObserver(observer);
            // der Teil ist daweil eigentlich komplett unnötig,
            // finds aber trotzdem cool dass es geht
            barChart.clear(); 
            // entfernt dann die letzten Werte aus dem Graf
            observer = null;
            count = 0;
            datalist.clear();

        });


        button_change_to_database.setOnClickListener(view12 -> controller.navigate(MonitoringFragmentDirections
                .actionMonitoringFragmentToDatenbankFragment()));


        // TODO: start & stop der Messung implementieren + darstellung der Werte
        button_start.setOnClickListener(v -> {

            // HIER Sensor Daten in eine DB speichern

            sensor_xzy.setText(R.string.recording );

            if(mediaBinder == null) return;
            mediaBinder.play(R.raw.start);

            String toSay = sensor_xzy.getText().toString();
            textToSpeech.speak(toSay,
                    TextToSpeech.QUEUE_ADD,
                    null,
                    UUID.randomUUID().toString());




            // observer registrieren:
            if(observer == null) {
                observer = (accelerationInformation) -> {
//
                    entries.clear();

                    entries.add(new BarEntry(0, accelerationInformation.getX()));
                    entries.add(new BarEntry(1, accelerationInformation.getY()));
                    entries.add(new BarEntry(2, accelerationInformation.getZ()));

                    BarDataSet barDataSet = new BarDataSet(entries, "values");
                    BarData barData = new BarData();
                    barData.setDrawValues(false); // ?
                    barData.addDataSet(barDataSet);

                    xAxisLabel.add("x-axis");
                    xAxisLabel.add("y-axis");
                    xAxisLabel.add("z-axis");

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter());

                    barChart.setData(barData);
                    barChart.invalidate();
                    barChart.setDrawValueAboveBar(false); // ?

                    Depot tempdepot = new Depot(count, accelerationInformation.getX(),accelerationInformation.getY(),accelerationInformation.getZ(), System.currentTimeMillis());
                    datalist.add(tempdepot);
                    count=count+1;
                    // eingabe in die Datenbank
                    senViewModel.setSensor(tempdepot);


                };

            sensorViewModel.accelerationLiveData.observe(getViewLifecycleOwner(),observer);

            }
        });

        button_stop.setOnClickListener(view1 -> {

            if(mediaBinder == null) return;
            mediaBinder.play(R.raw.stop);

            sensorViewModel.accelerationLiveData.removeObserver(observer);

            Toast.makeText(getContext(), "Saving to database ...", Toast.LENGTH_SHORT).show();

            // barChart.clear(); // notwendig?
            // entfernt dann die letzten Werte aus dem Graf

            observer = null;
            sensor_xzy.setText(R.string.recording_stoped );

            String toSay = sensor_xzy.getText().toString();
            textToSpeech.speak(toSay,
                    TextToSpeech.QUEUE_ADD,
                    null,
                    UUID.randomUUID().toString());

        });

        backtostart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.navigate(R.id.action_monitoringFragment_to_startFragment);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        textToSpeech = new TextToSpeech(getContext(), status -> {

        });
    }

    @Override
    public void onStop() {
        super.onStop();
        textToSpeech.shutdown();
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
