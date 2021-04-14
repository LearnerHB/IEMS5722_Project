package hk.edu.cuhk.ie.iems5722.group20.asynctask;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetTask extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... strings){

        HttpURLConnection conn = null;
        BufferedReader bufferedReader = null;
        StringBuffer resultData = new StringBuffer();

        try{
            URL url = new URL(strings[0]);
            conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");

            //start Query
            conn.connect();
            InputStream in = conn.getInputStream();
            InputStreamReader input = new InputStreamReader(in);
            BufferedReader buffer = new BufferedReader(input);

            if(conn.getResponseCode()==200){
                String inputLine;
                while((inputLine = buffer.readLine())!=null){
                    resultData.append(inputLine);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultData.toString();
    }
}
