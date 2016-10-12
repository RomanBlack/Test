package ru.romanblack.test.ui.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import ru.romanblack.test.BuildConfig;
import ru.romanblack.test.R;
import ru.romanblack.test.util.Consts;

public class MapFragment extends MainActivityFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient googleApiClient;

    private TextView latitudeView;
    private TextView longitudeView;

    private Location location;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initApiClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeUi(view);
    }

    @Override
    public void onStart() {
        super.onStart();

        connectApiClient();
    }

    @Override
    public void onStop() {
        disconnectApiClient();

        super.onStop();
    }

    private void initializeUi(View view) {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        latitudeView = (TextView) view.findViewById(R.id.latitude);
        longitudeView = (TextView) view.findViewById(R.id.longitude);
    }

    private void setupMap() {
        map.setMyLocationEnabled(true);
    }

    private void initApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void connectApiClient() {
        googleApiClient.connect();
    }

    private void disconnectApiClient() {
        googleApiClient.disconnect();
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                LocationRequest
                        .create()
                        .setInterval(60000) // 60 seconds
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY),
                this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
    }

    private void setupLocationText() {
        String latitudeText = location == null ? "-" : location.getLatitude() + "";
        String longitudeText = location == null ? "-" : location.getLongitude() + "";

        latitudeView.setText(latitudeText);
        longitudeView.setText(longitudeText);
    }

    private void moveMap() {
        if (location == null) {
            return;
        }

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()),
                15
        ));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        setupMap();
    }


    @Override
    public void onConnected(Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        moveMap();

        setupLocationText();

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (BuildConfig.DEBUG) {
            Log.e(
                    Consts.DEBUG_TAG,
                    "location changed " + location.getLatitude() + " " + location.getLongitude()
            );
        }

        Location oldLocation = this.location;

        this.location = location;

        if (oldLocation == null && location != null) {
            moveMap();
        }

        setupLocationText();
    }
}
