package com.example.androidnotesapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    public ArrayList<Note> Notes = new ArrayList<>();

    private RecyclerView recyclerView;

    private NoteViewAdapter mAdapter; // Data to recyclerview adapter

    private LinearLayoutManager linearLayoutManager;

    private ActivityResultLauncher<Intent> editActivityResultLauncher;

    private ActivityResultLauncher<Intent> infoActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Notes = loadFile();

        recyclerView = findViewById(R.id.NoteRecyclerView);
        mAdapter = new NoteViewAdapter(Notes, this);
        recyclerView.setAdapter(mAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        infoActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleInfoActivityResult);
        editActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleEditActivityResult);

        setTitle("Android Notes (" + String.format("%d", Notes.size()) + ")");
    }

//    @Override
//    protected void onPause() {
//        saveNote();
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        saveNote();
//        super.onStop();
//    }

    //    @Override
//    protected void onResume() {
//        Notes = loadFile();
//
//        if (Notes != null) {
//            for (int i = 0; i < Notes.size(); i++) {
//                Note note = Notes.get(i);
//                name.setText(note.getName());
//                description.setText(product.getDescription());
//                price.setText(String.format(Locale.getDefault(), "%.2f", product.getPrice()));
//            }
//        }
//        super.onResume();
//    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.info) {
            // TODO send to info activity
//            Toast.makeText(this, "Info click", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, InfoActivity.class);
            infoActivityResultLauncher.launch(intent);
            setTitle("Android Notes (" + String.format("%d", Notes.size()) + ")");
            return true;
        } else if (item.getItemId() == R.id.add) {
            // TODO send to edit activity
//            Toast.makeText(this, "new note click", Toast.LENGTH_SHORT).show();

            Note NewNote = new Note("","", System.currentTimeMillis());
            Notes.add(0,NewNote);
            mAdapter.notifyItemInserted(0);

            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("NOTE", NewNote);
            intent.putExtra("POS", 0);

            editActivityResultLauncher.launch(intent);
            setTitle("Android Notes (" + String.format("%d", Notes.size()) + ")");
            return true;
        }
        else if (item.getItemId() == R.id.save) {
            // TODO save note
//            Toast.makeText(this, "save click", Toast.LENGTH_SHORT).show();
            setTitle("Android Notes (" + String.format("%d", Notes.size()) + ")");
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        Note n = Notes.get(pos);

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("NOTE", n);
        intent.putExtra("POS", pos);

        editActivityResultLauncher.launch(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        String title = Notes.get(pos).getTitle();
        // TODO add dialog alert
        AskDelete(pos, title);
        setTitle("Android Notes (" + String.format("%d", Notes.size()) + ")");
        return true;
    }

    public void handleInfoActivityResult(ActivityResult result) {
        Toast.makeText(this, "Returned from Information Page", Toast.LENGTH_SHORT).show();
    }

    public void handleEditActivityResult(ActivityResult result){
//        Toast.makeText(this, "Returned from Edit Page", Toast.LENGTH_SHORT).show();
        if (result == null || result.getData() == null) {
//            Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show();
        }

        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK) {
            Note editNote = (Note) data.getSerializableExtra("NOTE");
            int pos = Integer.parseInt(data.getSerializableExtra("POS").toString());

            if (pos == -1){
                Toast.makeText(this, "Parsing Error when returning data from edit activity", Toast.LENGTH_LONG).show();
                return;
            }

            if (editNote.getDate() == -1){
                Notes.remove(0);
                mAdapter.notifyItemRemoved(0);
                Toast.makeText(this, "Note was not saved", Toast.LENGTH_SHORT).show();
                saveNote();
                setTitle("Android Notes (" + String.format("%d", Notes.size()) + ")");
                return;
            }

            if (editNote != null) {
                Notes.remove(pos);
                mAdapter.notifyItemRemoved(pos);
                Notes.add(0,editNote);
                mAdapter.notifyItemInserted(0);
                saveNote();
                setTitle("Android Notes (" + String.format("%d", Notes.size()) + ")");
            }
        }
        else if(result.getResultCode() == RESULT_CANCELED){
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            builder.setPositiveButton("YES", (dialog, id) -> {
//
//            });
//
//            builder.setNegativeButton("NO", (dialog, id) -> {});
//
//            builder.setMessage("Your note is not saved!\nSave note '" + editTitle.getText().toString() + "'?");
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
        }
        else {
            Toast.makeText(this, "OTHER result not OK!", Toast.LENGTH_SHORT).show();
        }
    }

    private void AskDelete(int pos, String Title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("YES", (dialog, id) -> {
            Notes.remove(pos);
            mAdapter.notifyItemRemoved(pos);
            saveNote();
            setTitle("Android Notes (" + String.format("%d", Notes.size()) + ")");
        });

        builder.setNegativeButton("NO", (dialog, id) -> {});

        builder.setMessage("Delete Note '" + Title + "'?");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<Note> loadFile() {

        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            ArrayList<Note> notes = new ArrayList<>();

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("Title");
                String text = jsonObject.getString("Text");
                long time = jsonObject.getLong("Date");

                notes.add(new Note(title, text, time));
            }
            //TODO check if notes are saved in correct Date sorted order
            if(notes == null)
            {
                return new ArrayList<>();
            }
            else{
                return notes;
            }


        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        }   catch (JSONException e) {

//        } catch (ParseException e) {
//            Toast.makeText(this, getString(R.string.ParseError), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.read_file_error), Toast.LENGTH_SHORT).show();
        }
        return new ArrayList<Note>();
    }

    private void saveNote() {
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < Notes.size(); i++) {
                Note save = Notes.get(i);
                jsonArray.put(save.toJSON());
            }

            printWriter.print(jsonArray);
            printWriter.close();
            fos.close();

//            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}