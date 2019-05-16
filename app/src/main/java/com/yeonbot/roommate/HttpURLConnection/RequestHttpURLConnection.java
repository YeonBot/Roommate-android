package com.yeonbot.roommate.HttpURLConnection;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class RequestHttpURLConnection {

    public String request(String _url, ContentValues _params) throws JSONException {

        //HttpURLConnection 참조변수
        HttpURLConnection urlConn = null;

        JSONObject jsonObject = new JSONObject();

        //StringBuffer 에 파라미터 연결::
        if(_params == null){
            //noting
        }else{
            String key;
            String value ;

            for(Map.Entry<String,Object> parameter : _params.valueSet() ){
                key = parameter.getKey();
                value = parameter.getValue().toString();

                jsonObject.put(key,value);

            }
        }

        //2. HttpURLConnection을 통해 web의 데이터를 가져온다.
        try{
            URL url = new URL(_url);
            urlConn = (HttpURLConnection)url.openConnection();

            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Accept", "application/json");
            urlConn.setRequestProperty("Content-type", "application/json");
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);

            String json = jsonObject.toString();
            OutputStream os = urlConn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.flush();
            os.close();

            Log.d("TAG",""+urlConn.getResponseCode());

            if(urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"UTF-8"));

            String line;
            String page = "";

            while((line = reader.readLine()) != null){
                page += line;
            }

            return page;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(urlConn != null)
                urlConn.disconnect();
        }

        return null;
    }
}
