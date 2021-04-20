package com.github.detb.trekkie.ui.specificroute;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.detb.trekkie.Hike;
import com.github.detb.trekkie.HikePoint;
import com.github.detb.trekkie.R;
import com.github.detb.trekkie.ui.newroute.NewRouteFragment;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class SpecificRouteFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {
    private SpecificRouteViewModel specificRouteViewModel;
    private MapView hikeMapView;
    private MapboxMap mapboxMap;
    private Hike specificHike;
    private TextView specificHikeTime;

    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;


    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;

    public View onCreateView(@NonNull LayoutInflater inflater,
                ViewGroup container, Bundle savedInstanceState) {
        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));
        specificRouteViewModel = new ViewModelProvider(this).get(SpecificRouteViewModel.class);

        View root = inflater.inflate(R.layout.fragment_specificroute, container, false);
        TextView hikeNameTextView = root.findViewById(R.id.specificHikeName);
        TextView hikeDescriptionTextView = root.findViewById(R.id.specificHikeDescription);
        ImageView hikeImageImageView = root.findViewById(R.id.specificHikePicture);

        specificHikeTime = root.findViewById(R.id.specificHikeTime);
        hikeMapView = root.findViewById(R.id.specificHikeMapView);
        hikeMapView.onCreate(savedInstanceState);
        hikeMapView.getMapAsync(this);

            int hikeId = this.getArguments().getInt("HikeId");

            specificRouteViewModel.getHike(hikeId).observe(getViewLifecycleOwner(), hike -> {
                hikeNameTextView.setText(hike.getTitle());
                hikeDescriptionTextView.setText(hike.getDescription());
                hikeImageImageView.setImageResource(hike.getPictureId());
                specificHike = hike;

            });

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
        mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);

                addDestinationIconSymbolLayer(style);
                getRoute(specificHike.hikePointList);
                // mapboxMap.addOnMapClickListener(NewRouteFragment.this);

//                boolean simulateRoute = true;
//                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
//                        .directionsRoute(currentRoute)
//                        .shouldSimulateRoute(simulateRoute)
//                        .build();
//// Call this method with Context from within an Activity
//                NavigationLauncher.startNavigation(getActivity(), options);
            }
        });
    }

        private void getRoute(List<HikePoint> hikePoints) {
        NavigationRoute.Builder navRouteBuilder = NavigationRoute.builder(getContext()).accessToken(Mapbox.getAccessToken())
            .origin(hikePoints.get(0).position)
            .destination(hikePoints.get(hikePoints.size() - 1).position);
        //navRouteBuilder.accessToken(Mapbox.getAccessToken());
        //navRouteBuilder.origin(hikePoints.get(0).position);
        //navRouteBuilder.destination(hikePoints.get(hikePoints.size() - 1).position);
            navRouteBuilder.profile(DirectionsCriteria.PROFILE_WALKING);
           for (HikePoint hikePoint : hikePoints
                ) {
               navRouteBuilder.addWaypoint(hikePoint.position);
           }
            navRouteBuilder.build().getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
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
                            specificHikeTime.setText(String.valueOf(TimeUnit.SECONDS.toMinutes(currentRoute.duration().longValue())) + " minutes");
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
            locationComponent.activateLocationComponent(getContext(), loadedMapStyle);
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
}
