package ch.teko.benj.notezillaapp.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by b7hafnb on 07.09.2017.
 */

public class Connection{
    public static final String SERVER_LOCATION = "https://192.168.1.105:8443";
    public static final String DELETE_NOTE = "/notes/delete/";
    public static final String GET_NOTE = "/notes/all/";
    public static final String CREATE_NOTE = "/notes/new/";
    public static final String VERIFY_USER = "/Users/valid/";

    private static final String STORE = "Notezilla.p12";
    private static final String PASSWORD = "Ad.1234";


    public static String getServerRequest(String place) {
        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(SERVER_LOCATION + place);
            connection = (HttpsURLConnection) url.openConnection();
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

    public static String putServerRequest(String place, String email, String password) {
        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        try {
            try
            {
                // Create an SSLContext that uses our TrustManager
                SSLContext context = SSLContext.getInstance("TLS");
                TrustManager[] tmlist = {new MyTrustManager()};
                context.init(null, tmlist, null);
                connection.setSSLSocketFactory(context.getSocketFactory());
            }
            catch (NoSuchAlgorithmException e)
            {
                throw new IOException(e);
            } catch (KeyManagementException e)
            {
                throw new IOException(e);
            }

            URL url = new URL(SERVER_LOCATION + place + "?email="+email+"&password="+password);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");

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
            if(connection != null) {
                connection.disconnect();
            }
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
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(SERVER_LOCATION + DELETE_NOTE + id);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.connect();

            Log.d("Server answer", String.valueOf(connection.getResponseCode()));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    public static void createNoteServerRequest(String titel, String contend) {
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(SERVER_LOCATION + CREATE_NOTE + "?title=" + titel + "&content=" + contend);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.connect();

            Log.d("Server answer", String.valueOf(connection.getResponseCode()));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    private static class MyTrustManager implements X509TrustManager
    {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException
        {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException
        {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers()
        {
            return null;
        }

    }
}
