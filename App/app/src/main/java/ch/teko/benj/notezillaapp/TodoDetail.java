package ch.teko.benj.notezillaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TodoDetail extends AppCompatActivity {
    private EditText titel;
    private EditText contend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);

        makeLogoutButton();
        makeSaveButton();
        makeDeleteButton();

        titel = (EditText) findViewById(R.id.titel);
        contend = (EditText) findViewById(R.id.contend);

        reviceData();
    }

    private void reviceData() {
        Intent intent = getIntent();
        String message = intent.getStringExtra(TodoOverview.TITEL);

        if(message != null) {
            titel.setText(message);
            contend.setText(getContendFromServer());
        };
    }

    private String getContendFromServer() {
        //TODO
        return null;
    }

    private void makeLogoutButton() {
        final Button login = (Button) findViewById(R.id.logout);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent logout = new Intent(TodoDetail.this, LoginActivity.class);
                TodoDetail.this.startActivity(logout);
            }
        });
    }

    private void makeDeleteButton() {
        final Button login = (Button) findViewById(R.id.delete);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent overview = new Intent(TodoDetail.this, TodoOverview.class);
                TodoDetail.this.startActivity(overview);
                deleteNoteInServer();
            }
        });
    }

    private void makeSaveButton() {
        final Button login = (Button) findViewById(R.id.save);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent overview = new Intent(TodoDetail.this, TodoOverview.class);
                TodoDetail.this.startActivity(overview);
                saveNoteInServer();
            }
        });
    }

    private void saveNoteInServer() {
        //TODO
    }

    private void deleteNoteInServer() {
        //TODO
    }
}
