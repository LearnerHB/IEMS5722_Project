package hk.edu.cuhk.ie.iems5722.group20.asynctask;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginRequest extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... strings){

        InputStream is = null;
        String results = "";
        HttpURLConnection conn = null;


        try {
            URL url = new URL(strings[0]);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            Uri.Builder builder = new Uri.Builder();
            // Build the parameters using ArrayList objects para_names and para_values

            builder.appendQueryParameter("username", String.valueOf(strings[1]));
            builder.appendQueryParameter("password", strings[2]);

            String query = builder.build().getEncodedQuery();
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Process the response
                System.out.println("http请求成功");
                is = conn.getInputStream();
                // Convert the InputStream into a string
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    results += line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }
}
