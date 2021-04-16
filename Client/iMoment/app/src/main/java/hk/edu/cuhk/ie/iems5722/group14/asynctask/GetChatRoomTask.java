package hk.edu.cuhk.ie.iems5722.group14.asynctask;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetChatRoomTask extends AsyncTask<URL, Void, String> {


    @Override
    protected String doInBackground(URL... urls) {

        HttpURLConnection conn = null;
        BufferedReader bufferedReader = null;
        StringBuffer resultData = new StringBuffer();

        try{
            URL url = new URL("http://18.191.232.230/api/project/rooms");

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
