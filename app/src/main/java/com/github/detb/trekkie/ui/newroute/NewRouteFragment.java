package com.github.detb.trekkie.ui.newroute;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.detb.trekkie.Hike;
import com.github.detb.trekkie.HikePoint;
import com.github.detb.trekkie.R;
import com.github.detb.trekkie.data.Root;
import com.github.detb.trekkie.data.Summary;
import com.github.detb.trekkie.db.OpenRouteServiceApi;
import com.github.detb.trekkie.db.ServiceGenerator;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;


// classes needed to add the location component
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;


// classes needed to add a marker
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// classes needed to launch navigation UI
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class NewRouteFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, MapboxMap.OnMapClickListener, PermissionsListener {


    private MapView mapView;
    private MapboxMap mapboxMap;

    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;

    // variables needed to initialize navigation
    private Button addPoint;
    private Button addHike;
    private EditText pointDescriptionEditText;

    private final List<HikePoint> hikePoints = new ArrayList<>();

    private NewRouteViewModel newRouteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Mapbox.getInstance(getContext(), getString(R.string.mapbox_access_token));

        newRouteViewModel =
                new ViewModelProvider(this).get(NewRouteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_newroute, container, false);

        pointDescriptionEditText = root.findViewById(R.id.point_description);
        pointDescriptionEditText.setHint("Description of specific pointDescription of specific point");
        pointDescriptionEditText.setOnClickListener(this);

        addPoint = root.findViewById(R.id.addPoint);
        addPoint.setEnabled(false);

        addHike = root.findViewById(R.id.addHike);

        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        addHike.setOnClickListener(v -> {
            Hike hike;

            String nameOfHike = ((EditText)root.findViewById(R.id.hike_name_string)).getText().toString();
            String descriptionOfHike = ((EditText)root.findViewById(R.id.hike_description_string)).getText().toString();

            if(TextUtils.isEmpty(nameOfHike)){
                Toast.makeText(getContext(),"Please give your hike a name",Toast.LENGTH_LONG).show();
            }
            if(TextUtils.isEmpty(descriptionOfHike)){
                Toast.makeText(getContext(),"Please give your hike a description",Toast.LENGTH_LONG).show();
            }
            else {
                hike = new Hike(nameOfHike, descriptionOfHike, R.drawable.defaulthike, hikePoints);
                newRouteViewModel.pushHikeToDb(hike);

                Toast.makeText(getContext(), "Hike Added", Toast.LENGTH_SHORT).show();
                mapboxMap.clear();
                ((EditText) root.findViewById(R.id.hike_name_string)).getText().clear();
                ((EditText) root.findViewById(R.id.hike_description_string)).getText().clear();
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mesterb/cknu7vyt90oel17o5cp6azm1q"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);

                addDestinationIconSymbolLayer(style);
                mapboxMap.addOnMapClickListener(NewRouteFragment.this);
            }
        });
        mapboxMap.setCameraPosition(new CameraPosition.Builder().zoom(10).build());
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

    @SuppressWarnings( {"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

        addPoint.setEnabled(true);
        addPoint.setBackgroundResource(R.color.mapbox_blue);
        addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point pointToAdd = Point.fromLngLat(point.getLongitude(), point.getLatitude());
                mapboxMap.addMarker(new MarkerOptions().position(point));

                hikePoints.add(new HikePoint(pointToAdd, pointDescriptionEditText.getText().toString()));
                pointDescriptionEditText.getText().clear();
                Toast.makeText(getContext(), "Point added", Toast.LENGTH_SHORT).show();
                addHike.setEnabled(true);
                closeKeyboard();
            }
        });

        return true;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
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


    @Override
    public void onClick(View v) {
        pointDescriptionEditText.setText("");
    }

    private void closeKeyboard()
    {
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }
}