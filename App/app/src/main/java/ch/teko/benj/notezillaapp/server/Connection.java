package ch.teko.benj.notezillaapp.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by b7hafnb on 07.09.2017.
 */

public class Connection{
    public static final String SERVER_LOCATION = "http://192.168.1.105:9000";
    public static final String DELETE_NOTE = "/notes/delete/";
    public static final String GET_NOTE = "/notes/all/";
    public static final String SAVE_NOTE = "/notes/all/";



    public static String getServerRequest(String place) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(SERVER_LOCATION + place);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if(connection.getResponseCode() == 200){
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                Log.d("Server answer", buffer.toString());

                return buffer.toString();
            }else {
                return "No connection, Error: "+ connection.getResponseCode() +" "+ connection.getErrorStream();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            connection.disconnect();
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "No connection";
    }

    public static void deleteNoteServerRequest(String id) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(SERVER_LOCATION + DELETE_NOTE + id);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.connect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }
}
