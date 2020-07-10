package com.example.munf_project_v2;

import android.graphics.Color;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MonitoringFragment extends Fragment {

    private SensorViewModel sensorViewModel;
    private Observer<AccelerationInformation> observer;

    private UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitoring, container, false);
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
        final Button button_save_to_DB = view.findViewById(R.id.button_save_to_DB);

        final EditText et_name_db = view.findViewById(R.id.db_input_name);

        observer = null;


        // ++++++++++++ BARCHART ++++++++++++
        final BarChart barChart = view.findViewById(R.id.livedata_barchart);
        barChart.setNoDataText(getString(R.string.start_measurement));
        barChart.setBackgroundColor(Color.YELLOW);
        // Setup für Barchart

        ArrayList<BarEntry> entries = new ArrayList<>();

        final ArrayList<String> xAxisLabel = new ArrayList<>();

        // ++++++++++++++ BARCHART +++++++++++++


        sensorViewModel = new ViewModelProvider(this,
                ViewModelProvider
                        .AndroidViewModelFactory
                        .getInstance(getActivity()
                                .getApplication()))
                .get(SensorViewModel.class);

        button_save_to_DB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Userdata u = new Userdata(et_name_db.getText().toString());
                // userViewModel übergeben
                userViewModel.setUser(u).observe(getViewLifecycleOwner(),userdata -> { // this
                    // obererver registrieren:
                    Toast.makeText(getContext(),"User added: " + userdata.getName(), Toast.LENGTH_SHORT).show();

                });
            }
        });

        button_change_fragment.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                controller.navigate(MonitoringFragmentDirections
                        .actionMonitoringFragmentToFeedbackFragment()
                        .setDisplayString("Hier ist dein Feedback"));
                // der Teil ist daweil eigentlich komplett unnötig,
                // finds aber trotzdem cool dass es geht
            }

        });


        button_change_to_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.navigate(MonitoringFragmentDirections
                        .actionMonitoringFragmentToDatenbankFragment());
            }
        });


        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // HIER Sensor Daten in eine DB speichern


                // observer registrieren:
                // observer == null:
                // sonst hätte man den observer nicht mehr unregistern können, wenn man den start button mehr als 1x drückt
                if (observer == null) {
                    observer = (accelerationInformation) -> {


                        entries.clear();

                        entries.add(new BarEntry(0, accelerationInformation.getX()));
                        entries.add(new BarEntry(1, accelerationInformation.getY()));
                        entries.add(new BarEntry(2, accelerationInformation.getZ()));

                        BarDataSet barDataSet = new BarDataSet(entries, "values");
                        barDataSet.setDrawValues(true);

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
                    };

                    sensorViewModel.accelerationLiveData.observe(getViewLifecycleOwner(), observer);

                }
            }
        });

        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorViewModel.accelerationLiveData.removeObserver(observer);

                barChart.clear(); // notwendig?
                // entfernt dann die letzten Werte aus dem Graf

                observer = null;
            }
        });
    }

}
