package ch.teko.benj.notezillaapp.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ch.teko.benj.notezillaapp.R;
import ch.teko.benj.notezillaapp.objects.Note;

import static ch.teko.benj.notezillaapp.server.Connection.GET_NOTE;
import static ch.teko.benj.notezillaapp.server.Connection.createNoteServerRequest;
import static ch.teko.benj.notezillaapp.server.Connection.deleteNoteServerRequest;
import static ch.teko.benj.notezillaapp.server.Connection.editNoteServerRequest;
import static ch.teko.benj.notezillaapp.server.Connection.getServerRequest;

public class NoteDetail extends AppCompatActivity {
    private EditText title;
    private EditText contend;
    private Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);

        makeLogoutButton();
        makeSaveButton();
        makeDeleteButton();

        title = (EditText) findViewById(R.id.title);
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
        final ImageButton login = (ImageButton) findViewById(R.id.logout);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent logout = new Intent(NoteDetail.this, LoginActivity.class);
                NoteDetail.this.startActivity(logout);
            }
        });
    }

    private void makeDeleteButton() {
        final ImageButton delete = (ImageButton) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteNoteInServer();
                toast(title.getText().toString() + " successfully deleted");
            }
        });
    }

    private void makeSaveButton() {
        final ImageButton login = (ImageButton) findViewById(R.id.save);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveNoteInServer();
            }
        });
    }

    private void saveNoteInServer() {
        if(title.getText().toString().length() > 0) {
            if (note != null) {
                note.setTitel(title.getText().toString());
                note.setContend(contend.getText().toString());
                Log.d("Note", "Changed: " + note.getTitel());
                editNote();
            } else {
                note = new Note(title.getText().toString(), contend.getText().toString());
                Log.d("Note", "Created: " + note.getTitel());
                createNote();
            }
        }else{
            toast("A title is needed");
        }
    }

    private void createNote(){
        new PutTask().execute(note.getTitel(), note.getContend());
    }

    private void editNote(){
        new UpdateTask().execute(String.valueOf(note.getId()), note.getTitel(), note.getContend());
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
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    private void getNote(String id) {
        new GetTask().execute(GET_NOTE + id);
    }

    private class GetTask extends AsyncTask<String, String, String> {
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
            title.setText(note.getTitel());
            contend.setText(note.getContend());
        }
    }

    private class DeleteTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            deleteNoteServerRequest(params[0]);
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("Note", "Deleted: " + title.getText().toString());
            finish();
        }
    }

    private class PutTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            createNoteServerRequest(params[0],replaceReturn(params[1]));
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("Note", "Created: " + title.getText().toString());
            toast(note.getTitel() + " successfully saved");
            finish();
        }
    }

    private class UpdateTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            editNoteServerRequest(params[0],params[1], replaceReturn(params[2]));
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d("Note", "Updated: " + title.getText().toString());
            toast(note.getTitel() + " successfully updated");
            finish();
        }
    }

    private String replaceReturn(String text){
        return text.replace("\n", "%0A");
        //Backend only handle %0A as Return
    }
}
