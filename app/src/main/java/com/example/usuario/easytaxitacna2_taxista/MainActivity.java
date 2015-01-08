package com.example.usuario.easytaxitacna2_taxista;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;


public class MainActivity extends ActionBarActivity {

    GoogleMap map;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getGoogleMap();
    }


    public void getGoogleMap(){
        try{
            if(map==null)
                map =((MapFragment) getFragmentManager().findFragmentById(R.id.mapView)).getMap(); //Mostrar mapa nuevo
            map.setMyLocationEnabled(true); //Muestra la localizacion actual
            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() { //Detecta cambios de posicion y lo muestra
                @Override
                public void onMyLocationChange(Location _location) {
                    location=_location;
                    Double latitude = _location.getLatitude();
                    Double longitude = _location.getLongitude();

                    //Enviar localizacion del usuario como string compuesto de altitud y latitud
                    String message = Double.toString(latitude) + "\n"  +Double.toString(longitude) ;
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    //SendMessage(message);
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"No se pudo obtener mapa",Toast.LENGTH_SHORT).show();
        }
    }
}
