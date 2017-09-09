package ch.teko.benj.notezillaapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TodoOverview extends AppCompatActivity {
    private ListView list;
    public static final String TITEL = "TITEL";
    private List<Note> notes = new ArrayList<Note>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_overview);
        list = (ListView) findViewById(R.id.list_view);

        makeCreateButton();
        makeLogoutButton();

        makeListClikable();
        updateList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateList();
    }

    private void makeListClikable(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Intent detail = new Intent(TodoOverview.this, TodoDetail.class);
                detail.putExtra(TITEL, selectedItem);
                TodoOverview.this.startActivity(detail);
            }
        });
    }

    private void updateList(){
        ArrayList<String> contend = getTitlesFromServer();
        contend.add("Shopping");
        contend.add("Buissnes");
        contend.add("Bucktedlist");
        contend.add("Blacklist");
        contend.add("Passwords");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, contend);

        list.setAdapter(arrayAdapter);
    }

    private ArrayList<String> getTitlesFromServer(){
        ArrayList<String> message = new ArrayList<>();

        message.add(getServerMessage("/notes/all"));

        return message;
    }

    public  String getServerMessage(String place) {
        new JSONTask().execute("http://localhost:9000/Users/all"); //https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesData.txt");
        return "No connection";
    }
    public class JSONTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
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

                    String jsonFile = buffer.toString();

                    JSONArray parentArray = new JSONArray(jsonFile);
                    notes.clear();
                    for(int i = 0; i < parentArray.length(); i++){
                        JSONObject object = parentArray.getJSONObject(i);
                        Note note = new Note(object.getInt("idNotes"), object.getString("idNotes"), object.getString("notetitle"));
                        notes.add(note);
                    }
                }else {
                    return "No connection Error: "+ connection.getResponseCode() +" "+ connection.getErrorStream();
                }

            } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
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
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(notes.isEmpty()){
            Log.d("Server answer", "No Item found");
        }else{
            for (int i = 0; i < notes.size(); i++)
                Log.d("Server answer", notes.get(i).getTitel());
        }
        //TODO set data in UI result
    }
}


    private void makeCreateButton() {
        final Button login = (Button) findViewById(R.id.create);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detail = new Intent(TodoOverview.this, TodoDetail.class);
                TodoOverview.this.startActivity(detail);
            }
        });
    }

    private void makeLogoutButton() {
        final Button login = (Button) findViewById(R.id.logout);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent logout = new Intent(TodoOverview.this, LoginActivity.class);
                TodoOverview.this.startActivity(logout);
            }
        });
    }
}
