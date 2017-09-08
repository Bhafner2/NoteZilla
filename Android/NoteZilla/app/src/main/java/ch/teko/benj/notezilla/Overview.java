package ch.teko.benj.notezilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Overview extends AppCompatActivity {
    private ListView list;
    public static final String TITEL = "TITEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
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
                Intent detail = new Intent(Overview.this, DetailView.class);
                detail.putExtra(TITEL, selectedItem);
                Overview.this.startActivity(detail);
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

        message.add(Connection.getServerMessage("/contents"));

        return message;
    }

    private void makeCreateButton() {
        final Button login = (Button) findViewById(R.id.create);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detail = new Intent(Overview.this, DetailView.class);
                Overview.this.startActivity(detail);
            }
        });
    }

    private void makeLogoutButton() {
        final Button login = (Button) findViewById(R.id.logout);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent logout = new Intent(Overview.this, LoginActivity.class);
                Overview.this.startActivity(logout);
            }
        });
    }
}
