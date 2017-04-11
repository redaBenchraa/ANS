package com.example.reda_benchraa.asn.DAO;
/* Project
 * Created by reda-benchraa on 11/04/17.
 */
import android.os.AsyncTask;
import android.util.Log;

import com.example.reda_benchraa.asn.Model.Account;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class JsonFetcher extends AsyncTask<String, Void, String> {
    public JsonFetcher(Listener listener) {
        mListener = listener;
    }
    public interface Listener {
        void onLoaded(String response);

        void onError();
    }
    private Listener mListener;
    @Override
    protected String doInBackground(String... strings) {
        try {
            return loadJSON(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            mListener.onLoaded(response);
        } else {
            mListener.onError();
        }
    }
    private String loadJSON(String jsonURL) throws IOException {
        URL url = new URL(jsonURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = in.readLine()) != null)
            response.append(line);
        in.close();
        return response.toString();
    }
}