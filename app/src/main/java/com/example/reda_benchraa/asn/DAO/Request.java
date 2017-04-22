package com.example.reda_benchraa.asn.DAO;
/* Project
 * Created by reda-benchraa on 19/04/17.
 */

import org.json.JSONObject;

public class Request {
    JSONObject jsonObject;
    String url;
    public Request(String url, JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        this.url = url;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
