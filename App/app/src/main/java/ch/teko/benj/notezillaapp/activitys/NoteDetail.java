package ch.teko.benj.notezillaapp.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ch.teko.benj.notezillaapp.R;
import ch.teko.benj.notezillaapp.objects.Note;

import static ch.teko.benj.notezillaapp.server.Connection.GET_NOTE;
import static ch.teko.benj.notezillaapp.server.Connection.deleteNoteServerRequest;
import static ch.teko.benj.notezillaapp.server.Connection.getServerRequest;

public class NoteDetail extends AppCompatActivity {
    private EditText titel;
    private EditText contend;
    private Note note;
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
        String note = intent.getStringExtra(NoteOverview.TITEL);

        if(note != null) {
            getNote(note);
        };
    }

    private void makeLogoutButton() {
        final Button login = (Button) findViewById(R.id.logout);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent logout = new Intent(NoteDetail.this, LoginActivity.class);
                NoteDetail.this.startActivity(logout);
            }
        });
    }

    private void makeDeleteButton() {
        final Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteNoteInServer();
                toast(titel.getText().toString() + " successfully deleted");
            }
        });
    }

    private void makeSaveButton() {
        final Button login = (Button) findViewById(R.id.save);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveNoteInServer();
            }
        });
    }

    private void saveNoteInServer() {
        if(note != null){
            note.setTitel(titel.getText().toString());
            note.setContend(contend.getText().toString());
            Log.d("Note", "Changed: " + note.getTitel());
            editNote();
        }else{
            note = new Note(titel.getText().toString(), contend.getText().toString());
            Log.d("Note", "Created: " + note.getTitel());
            createNote();
        }
        toast(note.getTitel() + " successfully saved");
    }

    private void createNote(){
        //TODO create
        finish();
    }

    private void editNote(){
        deleteNoteInServer();
        createNote();
    }

    private void deleteNoteInServer() {
        if(note != null){
            new DeleteTask().execute(String.valueOf(note.getId()));
        }else{
            finish();
        }
    }

    private void toast(String message){
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void getNote(String id) {
        new JSONTask().execute(GET_NOTE + id);
    }

    public class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String jsonFile = getServerRequest(params[0]);

            try {
                JSONObject object = new JSONObject(jsonFile);
                note = new Note(object.getInt("idNotes"), object.getString("notetitle"), object.getString("notecontend"));
                Log.d("Note found ", note.getTitel());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            titel.setText(note.getTitel());
            contend.setText(note.getContend());
        }
    }

    public class DeleteTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            deleteNoteServerRequest(params[0]);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("Note", "Deleted: " + titel.getText().toString());
            finish();
        }
    }
}
