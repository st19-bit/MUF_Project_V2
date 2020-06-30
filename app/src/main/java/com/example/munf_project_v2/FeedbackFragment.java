package com.example.munf_project_v2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class FeedbackFragment extends Fragment {
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
        }



        return v;
    }


}
