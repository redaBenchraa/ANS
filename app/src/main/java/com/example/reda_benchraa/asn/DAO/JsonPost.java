package com.example.reda_benchraa.asn.DAO;
/* Project
 * Created by reda-benchraa on 11/04/17.
 */
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class JsonPost extends AsyncTask<Request, Void, String> {
    public JsonPost(Listener listener) {
        mListener = listener;
    }
    public interface Listener {
        void onLoaded(String response);
        void onError();
    }
    private Listener mListener;
    @Override
    protected String doInBackground(Request... requests) {
        try {
            return sendRequest(requests[0].getUrl(),requests[0].getJsonObject());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String response) {
        if (response != null) {
            mListener.onLoaded(response);
        } else {
            mListener.onError();
        }
    }
    private String sendRequest(String jsonURL,JSONObject jsonObject) throws IOException, JSONException {
        URL url = new URL(jsonURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Log.v("API POST",jsonURL);
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("USER-AGENT", "Mozilla/5.0");
        conn.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.connect();
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(jsonObject.toString());
        wr.close();
        wr.flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = in.readLine()) != null)
            response.append(line);
        in.close();
        conn.disconnect();
        return response.toString();
    }
}