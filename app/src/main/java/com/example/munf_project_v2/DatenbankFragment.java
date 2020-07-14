package com.example.munf_project_v2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class DatenbankFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_database, container, false);

        // IDEE:
        // in dem Fragment kann man allte Messungen aus der Datenbank heruassuchen und darstellen lassen

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final NavController controller = Navigation.findNavController(view);
        final ImageButton button_back_DB = view.findViewById(R.id.backbutton_DB);

        LineChart lineChart_x = view.findViewById(R.id.DBChart_x);
        Description desc_x = new Description();
        desc_x.setText("");
        lineChart_x.setDescription(desc_x);
        lineChart_x.setDrawGridBackground(false);


        LineChart lineChart_y = view.findViewById(R.id.DBChart_y);
        Description desc_y = new Description();
        desc_y.setText("");
        lineChart_y.setDescription(desc_y);
        lineChart_y.setDrawGridBackground(false);

        LineChart lineChart_z = view.findViewById(R.id.DBChart_z);
        Description desc_z = new Description();
        desc_z.setText("");
        lineChart_z.setDescription(desc_z);
        lineChart_z.setDrawGridBackground(false);

        ArrayList<Entry> x_values = new ArrayList<Entry>();
        ArrayList<Entry> y_values = new ArrayList<Entry>();
        ArrayList<Entry> z_values = new ArrayList<Entry>();

        ((MUFApplication) getActivity()
                .getApplication())
                .getDatabase()
                .getSensorDao()
                .getAllMessung()
                .observe(getViewLifecycleOwner(),
                        tempDepot -> {
                     int count = 0;
                            for (Depot s : tempDepot) {
                                // TODO: render speicher//Danke Patrick
                                x_values.add(new Entry(count, s.getX()));
                                y_values.add(new Entry(count, s.getY()));
                                z_values.add(new Entry(count, s.getZ()));

                                LineDataSet lineDataSet_x = new LineDataSet(x_values, "x-axis");
                                lineDataSet_x.setColor(Color.BLUE);
                                lineDataSet_x.setDrawCircles(false);
                                lineDataSet_x.setDrawCircleHole(false);
                                lineDataSet_x.setDrawValues(false);

                                LineDataSet lineDataSet_y = new LineDataSet(y_values, "y-axis");
                                lineDataSet_y.setColor(Color.RED);
                                lineDataSet_y.setDrawCircles(false);
                                lineDataSet_y.setDrawCircleHole(false);
                                lineDataSet_y.setDrawValues(false);

                                LineDataSet lineDataSet_z = new LineDataSet(z_values, "z-axis");
                                lineDataSet_z.setColor(Color.GREEN);
                                lineDataSet_z.setDrawCircles(false);
                                lineDataSet_z.setDrawCircleHole(false);
                                lineDataSet_z.setDrawValues(false);

                                LineData data_x = new LineData(lineDataSet_x);
                                LineData data_y = new LineData(lineDataSet_y);
                                LineData data_z = new LineData(lineDataSet_z);

                                lineChart_x.setData(data_x);
                                lineChart_x.invalidate();

                                lineChart_y.setData(data_y);
                                lineChart_y.invalidate();

                                lineChart_z.setData(data_z);
                                lineChart_z.invalidate();

                                count = count + 1;

                            }
                        });


        button_back_DB.setOnClickListener(new View.OnClickListener() {
         @Override
            public void onClick(View view) {
                controller.navigate(R.id.action_datenbankFragment_to_monitoringFragment);

            }
        });
    }
}