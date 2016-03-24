package com.zeowls.gifts;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by root on 3/23/16.
 */
public class Core {

    private String Domain = "http://bubble-zeowls.herokuapp.com";

    private String getRequest(String url) throws IOException {
        String data;
        BufferedReader reader;
        URL url1 = new URL(url);
        Log.d("url", url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(2000);
        httpURLConnection.connect();

        InputStream inputStream = httpURLConnection.getInputStream();
        StringBuilder stringBuffer = new StringBuilder();
        assert inputStream != null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine())!=null){
            stringBuffer.append(line);
        }
        data = stringBuffer.toString();
        return data;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void postRequest(String name) throws IOException {
//        String data;
//        BufferedReader reader;
//        URL url1 = new URL(url);
//        Log.d("url", url);
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
//        httpURLConnection.setRequestMethod("GET");
//        httpURLConnection.setConnectTimeout(2000);
//        httpURLConnection.connect();

        URL url = new URL(Domain + "/newShopItem/3/" );
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        ArrayList<AbstractMap.SimpleEntry> params = new ArrayList<>();
        params.add(new AbstractMap.SimpleEntry<>("name", name));

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();

        conn.connect();

//        String urlParameters  = "name=" + name;
//        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
//        int    postDataLength = postData.length;
//        URL url = new URL( Domain + "/newShopItem/5/" );
//        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
//        conn.setDoOutput( true );
//        conn.setInstanceFollowRedirects( false );
//        conn.setRequestMethod( "POST" );
//        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
//        conn.setRequestProperty( "charset", "utf-8");
//        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
//        conn.setUseCaches( false );
//        conn.connect();

//        InputStream inputStream = httpURLConnection.getInputStream();
//        StringBuilder stringBuffer = new StringBuilder();
//        assert inputStream != null;
//        reader = new BufferedReader(new InputStreamReader(inputStream));
//        String line;
//        while ((line = reader.readLine())!=null){
//            stringBuffer.append(line);
//        }
//        data = stringBuffer.toString();
//        return data;
    }


    private String getQuery(List<AbstractMap.SimpleEntry> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (AbstractMap.SimpleEntry pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode((String) pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    public JSONObject getShopItems(int id) throws JSONException {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetShopItems/"+id+"/JSON");
            if (!response.equals("0")){
                json = new JSONObject(response);
            }else {
                Log.d("getShopItems", response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }

    public JSONObject getShop(int id) throws JSONException {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetShop/"+id+"/JSON");
            if (!response.equals("0")){
                json = new JSONObject(response);
            }else {
                Log.d("getShopItems", response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }

    public JSONObject getAllShops(){
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetAllShops/JSON");
            if (!response.equals("0")){
                json = new JSONObject(response);
            }else {
                Log.d("getAllShops", response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }


    public JSONObject newItem(String name) throws JSONException {
        JSONObject json = null;
        try {
            postRequest(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }
}
