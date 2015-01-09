package com.example.usuario.easytaxitacna2_taxista;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MapActivity extends ActionBarActivity {

    AsyncTask<Void, Void, String> shareRegidTask;
    //Añadiendo nuevo mapa de Google Maps
    GoogleMap map;
    Location location;
    String SERVER_URL = "https://aqueous-escarpment-1930.herokuapp.com/SEND";
    private IntentFilter myFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getGoogleMap();
    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String myParam = intent.getExtras().getString("parameter");
            if (myParam != null) {
                //Aquí ejecutais el método que necesiteis, por ejemplo actualizar //el número de notificaciones recibidas

            }
        }
    };
    public void SendMessage(final String message){


        shareRegidTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = SendServerMessage(message);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                shareRegidTask = null;
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }

        };
        shareRegidTask.execute(null, null, null);
    }


    public String SendServerMessage(String message){

        JSONArray values = new JSONArray();
        values.put("user1");
        //JSON PRINCIPAL (MENSAJE)
        JSONObject JsonMessage = new JSONObject();

        //Partes del JSON
        JSONObject androidjs = new JSONObject();
        JSONObject datajs = new JSONObject();
        JSONObject iosjs = new JSONObject();

        try {
            datajs.put("message", message);

            androidjs.put("collapseKey", "optional");
            androidjs.put("data", datajs);

            iosjs.put("badge", 0);
            iosjs.put("alert", "Your message here");
            iosjs.put("sound", "soundName");


            JsonMessage.put("users", values);
            JsonMessage.put("android", androidjs);
            JsonMessage.put("ios", iosjs);
        }
        catch (Exception e){

            Log.e("JSONMessage", "Error al crear mensaje JSON");
        }

        String result = "";

        try {
            URL serverUrl = null;
            try {
                serverUrl = new URL(SERVER_URL);
            } catch (MalformedURLException e) {
                Log.e("AppUtil", "URL Connection Error: "
                        + SERVER_URL, e);
                result = "Invalid URL: " + SERVER_URL;
            }


            byte[] bytes =  JsonMessage.toString().getBytes();

            HttpURLConnection httpCon = null;
            try {
                httpCon = (HttpURLConnection) serverUrl.openConnection();
                httpCon.setDoOutput(true);
                httpCon.setUseCaches(false);
                httpCon.setFixedLengthStreamingMode(bytes.length);
                httpCon.setRequestMethod("POST");
                httpCon.setRequestProperty("Content-Type","application/json");
                httpCon.connect();

                OutputStream out = httpCon.getOutputStream();
                out.write(bytes);
                out.close();

                int status = httpCon.getResponseCode();
                if (status == httpCon.HTTP_OK){
                    result = "RegId shared with Application Server. RegId: "
                    ;
                } else {
                    result = "Post Failure." + " Status: " + status;
                }
            } finally {
                if (httpCon != null) {
                    httpCon.disconnect();
                }
            }

        } catch (IOException e) {
            result = "Post Failure. Error in sharing with App Server.";
            Log.e("AppUtil", "Error in sharing with App Server: " + e);
        }
        return result;
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
                    SendMessage(message);
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"No se pudo obtener mapa",Toast.LENGTH_SHORT).show();
        }
    }
}
