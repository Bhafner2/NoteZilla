package ch.teko.benj.notezillaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TodoOverview extends AppCompatActivity {
    private ListView list;
    public static final String TITEL = "TITEL";

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
        //ArrayList<String> contend = new ArrayList<>();
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
            try {
                URL url = new URL("http://www.android.com/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    return readStream(in);
                } finally {
                    urlConnection.disconnect();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        return "No connection";
    }

    private String readStream(InputStream in) {
       // JsonReader json = new JsonReader();
        java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
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
