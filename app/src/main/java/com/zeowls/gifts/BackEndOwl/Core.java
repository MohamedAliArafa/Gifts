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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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

    private String postRequest(String url,JSONObject params) throws IOException {
        URL url1 = new URL(Domain + url);
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        OutputStream stream = connection.getOutputStream();
        DataOutputStream writer = new DataOutputStream(stream);

        Log.d("WARN", params.toString());
        // The LogCat prints out data like:
        // ID:test,Email:test@gmail.com,Pwd:test
        writer.writeBytes(params.toString());
        writer.flush();
        writer.close();
        stream.close();

        String data;
        BufferedReader reader;
        InputStream inputStream = connection.getInputStream();
        StringBuilder stringBuffer = new StringBuilder();
        assert inputStream != null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine())!=null){
            stringBuffer.append(line);
        }
        data = stringBuffer.toString();
        Log.d("data", data);
        return data;
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
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
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        putMoviesDB(json);
        return json;
    }

    public int getCredentials(String username, String password) throws JSONException {
        JSONObject json = new JSONObject();
        int result = 0;
        try {
            json.put("email", username);
            json.put("password", password);
            String response = postRequest("/login",json);
            JSONObject resJson = new JSONObject(response);
            result = resJson.getJSONArray("response").getJSONObject(0).getInt("id");
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return result;
    }

    public int signUpUser(String username, String password, String mobile) throws JSONException {
        JSONObject json = new JSONObject();
        int result = 0;
        try {
            json.put("email", username);
            json.put("password", password);
            json.put("mobile", mobile);
            String response = postRequest("/signup",json);
            JSONObject resJson = new JSONObject(response);
            result = resJson.getInt("response");
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject addToCart(int userId, int itemId) throws JSONException {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/addToShopCart/" + userId + "/" + itemId);
            if (!response.equals("0")){
                json = new JSONObject(response);
            }else {
                Log.d("addToShopCart", response);
            }
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return json;
    }

    public int cartCount(int userId) {
        JSONObject json;
        int count = 0;
        try {
            String response = getRequest(Domain + "/getUserShopCart/" + userId + "/");
            if (!response.equals("0")){
                json = new JSONObject(response);
                count = json.getJSONArray("Cart").length();
            }else {
                Log.d("addToShopCart", response);
            }
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return count;
    }

    public JSONObject getUserCart(int userId) {
        JSONObject json = null;
        try {
            String response = getRequest(Domain + "/getUserShopCart/" + userId + "/");
            if (!response.equals("0")){
                json = new JSONObject(response);
            }else {
                Log.d("addToShopCart", response);
            }
        } catch (Exception e) {
//            Toast.makeText(context,  e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return json;
    }

}
