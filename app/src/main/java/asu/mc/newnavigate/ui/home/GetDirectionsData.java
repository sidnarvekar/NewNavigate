package asu.mc.newnavigate.ui.home;


import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * @auth Priyanka
 */

public class GetDirectionsData extends AsyncTask<Object,String,String> {

    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    String duration, distance;
    LatLng latLng;
    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        latLng = (LatLng)objects[2];
        Log.d("URL URL *******", url);


        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {

        String[] directionsList;
        DataParser parser = new DataParser();
        Log.d("////onPostExecute", s);

        try {
            JSONObject jsonObject = new JSONObject(s);

            final JSONArray geodata = jsonObject.getJSONArray("routes");
            Log.d("Length of routes ^^^^", Integer.toString(geodata.length()));
            for(int i = 0; i < geodata.length(); ++i){
                JSONObject route = geodata.getJSONObject(i);
                JSONArray legs = route.getJSONArray("legs");
                JSONArray leg = legs.getJSONObject(0).getJSONArray("steps");
                Log.d("Legs for route: ", Integer.toString(leg.length()));
            }

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }


        //directionsList = parser.parseDirections(s);
        displayDirection(s);

    }

    public void displayDirection(String s)
    {

        try {
            JSONObject jsonObject = new JSONObject(s);

            final JSONArray geodata = jsonObject.getJSONArray("routes");
            Log.d("Length of routes ^^^^", Integer.toString(geodata.length()));
            for(int i = 0; i < geodata.length(); ++i){
                JSONObject route = geodata.getJSONObject(i);
                JSONArray legs = route.getJSONArray("legs");
                JSONArray leg = legs.getJSONObject(0).getJSONArray("steps");
                Log.d("Legs for route: ", Integer.toString(leg.length()));

            }

            DataParser parser = new DataParser();

            String[] directionsList = null;

            for (int x = 0; x < Math.min(4, geodata.length()); x++)
            {
                //Log.d(directionsList[x], "alternate $$$$$ ");
                directionsList = parser.parseDirections(s, x);

                int count = directionsList.length;
                Log.d("Total Routes: ", Integer.toString(count));
                //PolylineOptions options = new PolylineOptions();
                for(int i = 0;i<count;i++)
                {
                    PolylineOptions options = new PolylineOptions();

                    //In order to filter paths, get list of latLng for current PolylineOption, i.e. current path
                    List<LatLng> currrntPathPoints = options.getPoints();
//            for (int ind = 0; ind < currrntPathPoints.size(); ind++)
//            {
//                Log.d("Current Directions: ", "Hello");
//            }

                    if (x == 0){
                        options.color(Color.GREEN);
                    }
                    else{
                        options.color(Color.RED);
                    }

                    options.width(10);
                    options.addAll(PolyUtil.decode(directionsList[i]));

                    mMap.addPolyline(options);
                }


            }

        }catch (JSONException err){
            Log.d("Error", err.toString());
        }



        //mMap.addPolyline(options);
    }






}





