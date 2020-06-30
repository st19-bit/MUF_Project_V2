package com.example.munf_project_v2;

import android.os.Bundle;
import android.os.DropBoxManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MonitoringFragment extends Fragment {

    private SensorViewModel sensorViewModel;


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

        final Button start = view.findViewById(R.id.button_start);
        final Button change_fragment = view.findViewById(R.id.button_to_feedback);
        final Button change_to_database = view.findViewById(R.id.button_to_database);


        // ++++++++++++ BARCHART ++++++++++++
        final BarChart barChart = view.findViewById(R.id.livedata_barchart);
        // Setup für Barchart


        ArrayList<BarEntry> x_values = new ArrayList<BarEntry>();
        ArrayList<BarEntry> y_values = new ArrayList<BarEntry>();;
        ArrayList<BarEntry> z_values = new ArrayList<BarEntry>();

        ArrayList<BarDataSet> dataSet = new ArrayList<>();
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

        change_fragment.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                controller.navigate(MonitoringFragmentDirections
                        .actionMonitoringFragmentToFeedbackFragment()
                        .setDisplayString("Hier ist dein Feedback"));
                // der Teil ist daweil eigentlich komplett unnötig,
                // finds aber trotzdem cool dass es geht
            }

        });
        change_to_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.navigate(MonitoringFragmentDirections
                        .actionMonitoringFragmentToDatenbankFragment());
            }
        });


        // TODO: start & stop der Messung implementieren + darstellung der Werte
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                // HIER Sensor Daten in eine DB speichern




                // observer registrieren:
                sensorViewModel.accelerationLiveData.observe(getViewLifecycleOwner(), (accelerationInformation)->{
                    sensor_xzy.setText(
                            "x:" + accelerationInformation.getX() + " y " + accelerationInformation.getY() + " z "+accelerationInformation.getZ()
                    ); // ACHTUNG: string in stringfile extrahieren!

                    entries.add(new BarEntry(0,accelerationInformation.getX()));
                    entries.add(new BarEntry(1,accelerationInformation.getY()));
                    entries.add(new BarEntry(2,accelerationInformation.getZ()));

                    BarDataSet barDataSet = new BarDataSet(entries, "values");
                    BarData barData = new BarData();
                    barData.addDataSet(barDataSet);

                    // Achsen schön darstellen:
                    // https://www.youtube.com/watch?v=0BsPW2DpQgE
                    // https://www.youtube.com/watch?v=sBqW770P3_U
                    xAxisLabel.add("x-axis");
                    xAxisLabel.add("y-axis");
                    xAxisLabel.add("z-axis");

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter());

                    barChart.setData(barData);
                    barChart.invalidate();
                });

            }
        });
    }
}
