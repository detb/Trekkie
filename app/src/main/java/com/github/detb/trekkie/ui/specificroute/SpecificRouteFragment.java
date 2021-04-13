package com.github.detb.trekkie.ui.specificroute;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.detb.trekkie.R;

public class SpecificRouteFragment extends Fragment {
    private SpecificRouteViewModel specificRouteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        specificRouteViewModel = new ViewModelProvider(this).get(SpecificRouteViewModel.class);

        View root = inflater.inflate(R.layout.fragment_specificroute, container, false);

        return root;
}


}
