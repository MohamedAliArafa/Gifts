package com.zeowls.gifts.BackEndOwl;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Core {

    Context context;

    public Core(Context context){
        this.context = context;
    }

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
    private void postRequest(final String name, final int Quantity) throws IOException {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Domain + "/newShopItem/3/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("name",name);
                params.put("quantity", String.valueOf(Quantity));
//                params.put(KEY_EMAIL, email);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
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

    public JSONObject getItem(int id) throws JSONException {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetItem/"+id+"/JSON");
            if (!response.equals("0")){
                json = new JSONObject(response);
            }else {
                Log.d("get Items", response);
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

    public JSONObject getSubCategoriesByCatID(int id){
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetSubCategoriesById/"+id+"/JSON");
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




    public JSONObject getAllCategories(){
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetCategories/JSON");
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

    public JSONObject getItemsByCategoryId(int id){
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/GetItemByCategory/"+id+"/JSON");
            if (!response.equals("0")){
                json = new JSONObject(response);
            }else {
                Log.d("get Items By Cat id", response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }





    public JSONObject newItem(String name,int Quantity) throws JSONException {
        JSONObject json = null;
        try {
            postRequest(name,Quantity);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }
}
