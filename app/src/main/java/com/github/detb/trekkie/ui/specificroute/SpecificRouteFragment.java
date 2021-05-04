package com.github.detb.trekkie.ui.specificroute;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.detb.trekkie.data.model.Hike;
import com.github.detb.trekkie.data.model.HikePoint;
import com.github.detb.trekkie.R;
import com.github.detb.trekkie.data.remote.Root;
import com.github.detb.trekkie.data.remote.Summary;
import com.github.detb.trekkie.data.remote.Feature;
import com.github.detb.trekkie.db.OpenRouteServiceApi;
import com.github.detb.trekkie.db.ServiceGenerator;
import com.github.detb.trekkie.ui.home.HomeFragment;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;



public class SpecificRouteFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {
    // ViewModel
    private SpecificRouteViewModel specificRouteViewModel;

    // Variables handling layout
        // MAP
    private MapView hikeMapView;
    private MapboxMap mapboxMap;
    private NestedScrollView specificScrollView; // To prevent scroll while interacting with map

        // SPECIFIC HIKE
    private MutableLiveData<Hike> specificHike;
    private TextView specificHikeTime;
    private MutableLiveData<Integer> timeValue;

        // PIE CHART
    private PieChart pieChart;
    private View[] children;
    private MutableLiveData<List<Summary>> waytypeSummaryList;
    private LinearLayout piechartParentLinearLayout;


    // Variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;

    // Directions
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;



    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));
        specificRouteViewModel = new ViewModelProvider(this).get(SpecificRouteViewModel.class);

        View root = inflater.inflate(R.layout.fragment_specificroute, container, false);

        // Creation of LiveData
        specificHike = new MutableLiveData<>();
        timeValue = new MutableLiveData<>();
        List<Summary> summaries = new ArrayList<>();
        waytypeSummaryList = new MutableLiveData<>(summaries);
        timeValue.setValue(1000);

        // Setting layout variables
        TextView hikeNameTextView = root.findViewById(R.id.specificHikeName);
        TextView hikeDescriptionTextView = root.findViewById(R.id.specificHikeDescription);
        ImageView hikeImageImageView = root.findViewById(R.id.specificHikePicture);
        ImageView deleteHike = root.findViewById(R.id.delete_hike);
        specificScrollView = root.findViewById(R.id.specificHikeScrollView);
        pieChart = root.findViewById(R.id.piechart);
        specificHikeTime = root.findViewById(R.id.specificHikeTime);
        hikeMapView = root.findViewById(R.id.specificHikeMapView);


        hikeMapView.onCreate(savedInstanceState);


        // Making pie chart explanations disappear
        root.findViewById(R.id.RoadLL).setVisibility(View.GONE);
        root.findViewById(R.id.StreetLL).setVisibility(View.GONE);
        root.findViewById(R.id.PathLL).setVisibility(View.GONE);
        root.findViewById(R.id.TrackLL).setVisibility(View.GONE);
        root.findViewById(R.id.FootwayLL).setVisibility(View.GONE);
        root.findViewById(R.id.OMLL).setVisibility(View.GONE);

        // Linear layout sorting
        piechartParentLinearLayout = root.findViewById(R.id.listParent);
        int childcount = piechartParentLinearLayout.getChildCount();
        children = new View[childcount];

        // get children of linearlayout. For sorting
        for (int i=0; i < childcount; i++){
            System.out.println("test i: " + i);
            children[i] = piechartParentLinearLayout.getChildAt(i);
        }
        // removing linearlayout views, for sorting
        piechartParentLinearLayout.removeAllViews();


        // Disabling scrollview while moving on the map
        hikeMapView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    specificScrollView.requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    specificScrollView.requestDisallowInterceptTouchEvent(false);
                    break;
            }
            return hikeMapView.onTouchEvent(event);
        });

        hikeMapView.getMapAsync(this);

        // Getting hikeId from arguments sent via bundle in fragment change
        int hikeId = this.getArguments().getInt("HikeId");

        // Setting specific hike value
        specificRouteViewModel.getHike(hikeId).observe(getViewLifecycleOwner(), hike -> {
            hikeNameTextView.setText(hike.getTitle());
            hikeDescriptionTextView.setText(hike.getDescription());
            hikeImageImageView.setImageResource(hike.getPictureId());
            specificHike.setValue(hike);
        });

        // onclicklistener for deleting the hike
        deleteHike.setOnClickListener(v -> {
            specificRouteViewModel.deleteHike(specificHike.getValue());
            Toast.makeText(getContext(), "Hike " + specificHike.getValue().getTitle() + " Deleted", Toast.LENGTH_LONG).show();
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            HomeFragment fragment = new HomeFragment();
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
            fragmentTransaction.addToBackStack( "tag" );
            fragmentTransaction.commit();
        });

        // api call to get statistics about the route (waytypes, height etc.)
        getRouteData();

        return root;
}

    @Override
    public void onStart() {
        super.onStart();
        hikeMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        hikeMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        hikeMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        hikeMapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        hikeMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        hikeMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hikeMapView.onDestroy();
    }


    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mesterb/cknu7vyt90oel17o5cp6azm1q"), style -> {
            enableLocationComponent(style);

            addDestinationIconSymbolLayer(style);
            getRoute(specificHike.getValue().hikePointList);

            LatLng latlng = new LatLng();
            latlng.setLatitude(specificHike.getValue().hikePointList.get(0).position.latitude());
            latlng.setLongitude(specificHike.getValue().hikePointList.get(0).position.longitude());
            timeValue.observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    mapboxMap.setCameraPosition(new CameraPosition.Builder().target(latlng).zoom(timeValue.getValue() / 9.5).build()); // need better calculation
                }
            });
        });

    }

        private void getRoute(List<HikePoint> hikePoints) {
        NavigationRoute.Builder navRouteBuilder = NavigationRoute.builder(getContext()).accessToken(Mapbox.getAccessToken())
            .origin(hikePoints.get(0).position)
            .destination(hikePoints.get(hikePoints.size() - 1).position);

            navRouteBuilder.profile(DirectionsCriteria.PROFILE_WALKING);
           for (HikePoint hikePoint : hikePoints
                ) {
               navRouteBuilder.addWaypoint(hikePoint.position);
           }

            navRouteBuilder.build().getRoute(new Callback<DirectionsResponse>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }
                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, hikeMapView, mapboxMap, R.style.NavigationLocationLayerStyle);

                            timeValue.setValue((int) TimeUnit.SECONDS.toMinutes(currentRoute.duration().longValue()));
                            specificHikeTime.setText(timeValue.getValue() + " minutes");
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();

            // Setting up custom location compartment
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(getContext())
                    .elevation(5)
                    .accuracyAlpha(.6f)
                    .accuracyColor(Color.RED)
                    .foregroundDrawable(R.drawable.ic_baseline_circle_24)
                    .build();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(getContext(), loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();

            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), "need explanation", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_LONG).show();
            //finish();
        }
    }

    private void createPieChart()
    {
        waytypeSummaryList.observe(getViewLifecycleOwner(), summaries -> {
            for (Summary summary : summaries) {

                TextView toEdit;

                switch (summary.getWayTypeAsString()) {
                    case "Unknown":
                    case "State Road":
                    case "Cycleway":
                    case "Steps":
                    case "Ferry":
                    case "Construction":
                        // TOO MANY TIMES IT GOES HERE - CANNOT ADDVIEW MULTIPLE TIMES
                        piechartParentLinearLayout.addView(children[5]);
                        toEdit = getView().findViewById(R.id.OM);
                        getView().findViewById(R.id.OMLL).setVisibility(View.VISIBLE); break;
                    case "Road":
                        piechartParentLinearLayout.addView(children[0]);
                        toEdit = getView().findViewById(R.id.Road);
                        getView().findViewById(R.id.RoadLL).setVisibility(View.VISIBLE); break;
                    case "Street":
                        piechartParentLinearLayout.addView(children[1]);
                        toEdit = getView().findViewById(R.id.Street);
                        getView().findViewById(R.id.StreetLL).setVisibility(View.VISIBLE); break;
                    case "Path":
                        piechartParentLinearLayout.addView(children[2]);
                        toEdit = getView().findViewById(R.id.Path);
                        getView().findViewById(R.id.PathLL).setVisibility(View.VISIBLE); break;
                    case "Track":
                        piechartParentLinearLayout.addView(children[3]);
                        toEdit = getView().findViewById(R.id.Track);
                        getView().findViewById(R.id.TrackLL).setVisibility(View.VISIBLE); break;
                    case "Footway":
                        piechartParentLinearLayout.addView(children[4]);
                        toEdit = getView().findViewById(R.id.Footway);
                        getView().findViewById(R.id.FootwayLL).setVisibility(View.VISIBLE); break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + summary.getWayTypeAsString());
                }

                String toSet = summary.getWayTypeAsString() + " " + summary.getWayTypeAmountPercentage();
                toEdit.setText(toSet);

                pieChart.addPieSlice(
                        new PieModel(
                                summary.getWayTypeAsString(),
                                (float)summary.getWayTypeAmount(),
                                Color.parseColor(summary.getColorString())));
            }
            pieChart.startAnimation();
        });
    }


    public void getRouteData()
    {
        //TESTING
        OpenRouteServiceApi api = ServiceGenerator.getOpenRouteServiceApi();
        specificHike.observe(getViewLifecycleOwner(), hike -> {
            String coordinates = specificHike.getValue().getCoordinatesAsString();

            String text = "{\"coordinates\":[" + coordinates + "],\"elevation\":\"true\",\"extra_info\":[\"steepness\",\"waytype\",\"surface\"],\"instructions\":\"false\",\"units\":\"m\"}";
            RequestBody body =
                    RequestBody.create(MediaType.parse("text/plain"), text);
            Call<Root> call = api.getHikeData(body);

            call.enqueue(new Callback<Root>() {
                @Override
                public void onResponse(Call<Root> call, Response<Root> response) {

                    if (response.code() == 200)
                    {
                        for (Feature feature : response.body().features
                        ) {
                            System.out.println("surface: ");
                            for (Summary summary:feature.properties.extras.surface.summary
                            ) {

                            }
                            System.out.println("waytype: ");
                            waytypeSummaryList.postValue(new ArrayList<>(feature.properties.extras.waytypes.summary));
                        }
                    }
                }

                @Override
                public void onFailure(Call<Root> call, Throwable t) {

                }
            });

        });
        createPieChart();
    }
}
