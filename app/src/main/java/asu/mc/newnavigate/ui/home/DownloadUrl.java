package asu.mc.newnavigate.ui.home;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Priyanka on 7/11/17.
 */

public class DownloadUrl {

    public String readUrl(String myUrl) throws IOException
    {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(myUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while((line = br.readLine()) != null)
            {
                sb.append(line);

            }


            data = sb.toString();
            Log.d("downloadUrl", data.toString());

            try {
                JSONObject jsonObject = new JSONObject(data);

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

//            PrintWriter pw = new PrintWriter("outFile.txt", "UTF-8");
//            pw.println(data);
//            pw.close();

            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(inputStream != null)
                inputStream.close();
            urlConnection.disconnect();
        }

        Log.d("data downlaod",data);
        return data;

    }
}

