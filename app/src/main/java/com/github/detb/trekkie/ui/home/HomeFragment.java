package com.github.detb.trekkie.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class HomeFragment extends Fragment implements HikeAdapter.OnListItemClickListener{

    private HomeViewModel homeViewModel;
    RecyclerView hikeList;
    HikeAdapter hikeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        hikeList = root.findViewById(R.id.rv);
        hikeList.hasFixedSize();
        hikeList.setLayoutManager(new LinearLayoutManager(getActivity()));

        homeViewModel.getHikeLiveData().observe(getViewLifecycleOwner(), hikes -> {
            hikeAdapter = new HikeAdapter((ArrayList)hikes, HomeFragment.this);
            hikeList.setAdapter(hikeAdapter);
            hikeList.setItemAnimator(new DefaultItemAnimator());
        });
        return root;
    }


    @Override
    public void onItemClick(Hike item) {
                FragmentTransaction fragmentTransaction = getActivity()
                .getSupportFragmentManager().beginTransaction();
        SpecificRouteFragment fragment = new SpecificRouteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("HikeId", item.getId());
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.addToBackStack( "tag" );
        fragmentTransaction.commit();
    }
    }