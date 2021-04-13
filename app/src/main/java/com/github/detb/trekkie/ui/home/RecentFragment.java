package com.github.detb.trekkie.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.detb.trekkie.Hike;
import com.github.detb.trekkie.HikeAdapter;
import com.github.detb.trekkie.R;
import com.github.detb.trekkie.ui.specificroute.SpecificRouteFragment;

import java.util.ArrayList;

public class RecentFragment extends Fragment implements HikeAdapter.OnListItemClickListener{

    private RecentViewModel recentViewModel;
    RecyclerView hikeList;
    HikeAdapter hikeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        recentViewModel = new ViewModelProvider(this).get(RecentViewModel.class);

        View root = inflater.inflate(R.layout.fragment_myrecenthikes, container, false);

        hikeList = root.findViewById(R.id.rv);
        hikeList.hasFixedSize();
        hikeList.setLayoutManager(new LinearLayoutManager(getActivity()));



        recentViewModel.getHikeLiveData().observe(getViewLifecycleOwner(), hikes -> {
            hikeAdapter = new HikeAdapter((ArrayList)hikes, RecentFragment.this);
            hikeList.setAdapter(hikeAdapter);
            hikeList.setItemAnimator(new DefaultItemAnimator());
        });
        return root;
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
            int hikeNumber = clickedItemIndex + 1;

            //Toast.makeText(getContext(), "Hike Number: " + hikeNumber, Toast.LENGTH_SHORT).show();
        }

    @Override
    public void onItemClick(Hike item) {

        Toast.makeText(getContext(), "Hike " + item.getId(), Toast.LENGTH_SHORT).show();
    }
    }