package ch.teko.benj.notezillaapp.activitys;

import android.content.Context;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.teko.benj.notezillaapp.R;
import ch.teko.benj.notezillaapp.objects.Note;

import static ch.teko.benj.notezillaapp.server.Connection.GET_NOTE;
import static ch.teko.benj.notezillaapp.server.Connection.getServerRequest;

public class NoteOverview extends AppCompatActivity {
    private ListView list;
    public static final String TITEL = "TITEL";
    private ArrayList<Note> notes = new ArrayList<Note>();

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
    protected void onResume() {
        super.onResume();
        updateList();
    }

    private void makeListClikable(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note selectedNote = (Note) parent.getItemAtPosition(position);
                Intent detail = new Intent(NoteOverview.this, NoteDetail.class);
                detail.putExtra(TITEL, String.valueOf(selectedNote.getId()));
                NoteOverview.this.startActivity(detail);
            }
        });
    }

    private void updateList() {
        new JSONTask().execute(GET_NOTE);
    }

    private void setDataToList(ArrayList<Note> titels){
        ArrayAdapter<Note> arrayAdapter = new ArrayAdapter<Note>
                (this, android.R.layout.simple_list_item_1, titels);

        list.setAdapter(arrayAdapter);
    }

    private void toast(String message){
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    private void makeCreateButton() {
        final Button login = (Button) findViewById(R.id.create);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent detail = new Intent(NoteOverview.this, NoteDetail.class);
                NoteOverview.this.startActivity(detail);
            }
        });
    }

    private void makeLogoutButton() {
        final Button login = (Button) findViewById(R.id.logout);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent logout = new Intent(NoteOverview.this, LoginActivity.class);
                NoteOverview.this.startActivity(logout);
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            String jsonFile = getServerRequest(params[0]);

            try {
                JSONArray parentArray = new JSONArray(jsonFile);
                notes.clear();
                for(int i = 0; i < parentArray.length(); i++){
                    JSONObject object = parentArray.getJSONObject(i);
                    Note note = new Note(object.getInt("idNotes"), object.getString("notetitle"), object.getString("notecontend"));
                    Log.d("Add Note to List", note.getTitel());
                    notes.add(note);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(notes.isEmpty()){
                Log.d("Server answer", "No Item found");
                toast("No Item found");
            }else{
                setDataToList(notes);
            }
        }
    }
}
