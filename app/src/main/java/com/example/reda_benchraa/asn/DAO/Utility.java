package com.example.reda_benchraa.asn.DAO;
/* Project
 * Created by reda-benchraa on 11/04/17.
 */

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utility {
    public static String getProperty(String key,Context context){
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("api.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR/";
        }
        return properties.getProperty(key);
    }
}
