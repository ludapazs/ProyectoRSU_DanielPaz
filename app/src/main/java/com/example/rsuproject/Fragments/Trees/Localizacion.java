package com.example.rsuproject.Fragments.Trees;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

public class Localizacion implements LocationListener {

    TreesCreateFragment localizacion;

    public void setLocalizacion(TreesCreateFragment localizacion) {
        this.localizacion = localizacion;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        String latitud = String.valueOf(location.getLatitude());
        String longitud = String.valueOf(location.getLongitude());

        this.localizacion.tree_latitude.setText(latitud);
        this.localizacion.tree_longitude.setText(longitud);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}
