package com.mideann.giffy.estacionsbicis;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<Estacio> llistatEstacions;
    private MyApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.editText = this.findViewById(R.id.id_EditText);
        //this.listView = this.findViewById(R.id.id_ListView);

        //this.listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        //this.listView.setAdapter(this.listAdapter);
        this.app = (MyApp) this.getApplication();      //  Add to manifest    android:name=".MyApp"

        // Carrega de dades en Asynctask
        FilCercaAsyncTask t = new FilCercaAsyncTask();
        t.execute();
    }

    // Add permissions in Manifest.xls      <uses-permission android:name="android.permission.INTERNET" />
    // Import to gradle app   --  >  implementation 'com.google.code.gson:gson:2.8.5'
    //  JsonParser to load JSON file
    //  JsonElement to work with elements
    //  JsonObject  to work with objects
    //  JsonArray   to work with array

    public void accioCerca(View view) {
        FilCercaAsyncTask t = new FilCercaAsyncTask();
        t.execute();
    }

    private ArrayList<Estacio> buscaEstacio() throws Exception {
        ArrayList<Estacio> dades = new ArrayList<>();

        URL url = new URL("http://wservice.viabicing.cat/v2/stations");
        // Alternative URL
        // URL url = new URL("https://www.bicing.cat/availability_map/getJsonObject");

        HttpURLConnection conexio = (HttpURLConnection) url.openConnection();

        if (conexio.getResponseCode() == HttpURLConnection.HTTP_OK) {
            JsonParser parser = new JsonParser();
            JsonElement jelem = parser.parse(new InputStreamReader(conexio.getInputStream()));
            JsonObject jobj = jelem.getAsJsonObject();
            JsonArray jArrayItems = jobj.getAsJsonArray("stations");
            for (int i = 0; i < jArrayItems.size(); i++) {
                JsonObject item = jArrayItems.get(i).getAsJsonObject();

                int id = item.get("id").getAsInt();
                String type = item.get("type").getAsString();
                Double latitude = item.get("latitude").getAsDouble();
                Double longitude = item.get("longitude").getAsDouble();
                String streetName = item.get("streetName").getAsString();
                String streetNumber = item.get("streetNumber").getAsString();
                int slots = item.get("slots").getAsInt();
                int bikes = item.get("bikes").getAsInt();
                String nearStations = item.get("nearbyStations").getAsString();
                String[] nearbyStations = nearStations.replace(" ", "").split(",");
                String status = item.get("status").getAsString();

                dades.add(new Estacio(id, type, latitude, longitude, streetName, streetNumber, slots, bikes, nearbyStations, status));
            }
        }
        return dades;
    }

    private class FilCercaAsyncTask extends AsyncTask<String, Void, ArrayList<Estacio>> {
        private String statusMSG = null;

        @Override
        protected ArrayList<Estacio> doInBackground(String... search) {
            ArrayList<Estacio> dades = new ArrayList<>();
            try {
                dades = MainActivity.this.buscaEstacio();
            } catch (Exception e) {
                this.statusMSG = e.getMessage();
            }
            return dades;
        }

        @Override
        protected void onPostExecute(ArrayList<Estacio> dades) {
            MainActivity.this.app.setEstacions(dades);

            if (this.statusMSG != null) {
                Toast.makeText(MainActivity.this, this.statusMSG, Toast.LENGTH_LONG).show();
                this.statusMSG = null;
            }
        }
    }

    public void irMapa(View view) {
        Intent intent = new Intent(this.getApplicationContext(), Mapa.class);
        this.startActivity(intent);
    }

    public void irLlista(View view) {
        Intent intent = new Intent(this.getApplicationContext(), Llista.class);
        this.startActivity(intent);
    }
}