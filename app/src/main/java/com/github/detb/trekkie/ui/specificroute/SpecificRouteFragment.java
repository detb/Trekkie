package com.github.detb.trekkie.ui.specificroute;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.detb.trekkie.Hike;
import com.github.detb.trekkie.R;

public class SpecificRouteFragment extends Fragment {
    private SpecificRouteViewModel specificRouteViewModel;
    private TextView hikeNameTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                ViewGroup container, Bundle savedInstanceState) {
            specificRouteViewModel = new ViewModelProvider(this).get(SpecificRouteViewModel.class);

            View root = inflater.inflate(R.layout.fragment_specificroute, container, false);
        hikeNameTextView = root.findViewById(R.id.specificHikeName);

            int hikeId = this.getArguments().getInt("HikeId");

            specificRouteViewModel.getHike(hikeId).observe(getViewLifecycleOwner(), hike -> {
                hikeNameTextView.setText(hike.getTitle());
            });

        return root;
}


}
