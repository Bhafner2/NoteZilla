package ch.teko.benj.notezillaapp.server;

import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import static android.content.ContentValues.TAG;

/**
 * Created by b7hafnb on 07.09.2017.
 */

public class Connection{
    public static final String SERVER_IP = "172.20.6.86"; //for local tests
 //   public static final String SERVER_LOCATION = "https://"+SERVER_IP+":8443";  //for local tests
    public static final String SERVER_LOCATION = "https://notezilla.wtf:8443";
    public static final String DELETE_NOTE = "/notes/delete/";
    public static final String GET_NOTE = "/notes/all/";
    public static final String CREATE_NOTE = "/notes/new/";
    public static final String EDIT_NOTE = "/notes/edit/";
    public static final String VERIFY_USER = "/Users/valid/";

    public static String getServerRequest(String place) {
        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        try {
            trustEveryone();
            URL url = new URL(SERVER_LOCATION + place);
            connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            if(connection.getResponseCode() == 200){
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                Log.d("Server answer", buffer.toString());

                return buffer.toString();
            }else {
                return "No connection, Error: "+ connection.getResponseCode() +" "+ connection.getErrorStream();
            }

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

    public static String putServerRequest(String place, String email, String password) {
        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        try {
            trustEveryone();
            URL url = new URL(SERVER_LOCATION + place + "?email="+email+"&password="+password);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            if(connection.getResponseCode() == 200){
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                Log.d("Server answer", buffer.toString());

                return buffer.toString();
            }else {
                return "No connection";
            }
        } catch (ConnectTimeoutException e) { //needed for loliypop
            Log.e(TAG, "Timeout", e);
            return "No connection";
        } catch (SocketTimeoutException e) {
            Log.e(TAG, " Socket timeout", e);
            return "No connection";
        } catch (IOException e) {
            e.printStackTrace();
            return "No connection";
        } finally {
            if (connection != null) {
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
    }

    public static void deleteNoteServerRequest(String id) {
        HttpsURLConnection connection = null;
        try {
            trustEveryone();
            URL url = new URL(SERVER_LOCATION + DELETE_NOTE + id);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.connect();

            Log.d("Server answer", String.valueOf(connection.getResponseCode()));

        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void createNoteServerRequest(String titel, String contend) {
        HttpsURLConnection connection = null;
        try {
            trustEveryone();
            URL url = new URL(SERVER_LOCATION + CREATE_NOTE + "?title=" + titel + "&content=" + contend);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.connect();

            Log.d("Server answer", String.valueOf(connection.getResponseCode()));

        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void editNoteServerRequest(String id, String titel, String contend) {
        HttpsURLConnection connection = null;
        try {
            trustEveryone();
            URL url = new URL(SERVER_LOCATION + EDIT_NOTE + id + "?id=" + id + "&title=" + titel + "&content=" + contend);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.connect();

            Log.d("Server answer", String.valueOf(connection.getResponseCode()));

        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean
                verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }
}
