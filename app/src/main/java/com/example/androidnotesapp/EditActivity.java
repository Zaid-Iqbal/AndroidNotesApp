package com.example.androidnotesapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editText;
    private int pos;
    private Note note;
    private String OGTitle;
    private String OGText;

    private ActivityResultLauncher<Intent> mainActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTitle = findViewById(R.id.TitleEdit);
        editText = findViewById(R.id.TextEdit);


        if (getIntent().hasExtra("NOTE")) {
            note = (Note) getIntent().getSerializableExtra("NOTE");
            String title = note.getTitle();
            String text = note.getText();
            editTitle.setText(title);
            editText.setText(text);
        }
        else {
            editTitle.setText("Error: Title Not Found");
            editText.setText("Error: Text Not Found");
        }

        OGTitle = editTitle.getText().toString();
        OGText = editText.getText().toString();
        pos = getIntent().getIntExtra("POS", -1);
    }

    @Override
    public void onBackPressed(){
        // TODO alert
//        Intent intent = new Intent();
//        Note sendNote = new Note(editTitle.getText().toString(), editText.getText().toString(),System.currentTimeMillis());
//        intent.putExtra("NOTE", sendNote);
//        intent.putExtra("POS", pos);
//        setResult(Activity.RESULT_CANCELED, intent);
//        finish();

        String title = editTitle.getText().toString();
        String text = editText.getText().toString();

        if( !title.equals(OGTitle) || !text.equals(OGText) ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("YES", (dialog, id) -> {
                Intent intent = new Intent();
                Note sendNote = new Note(title, text,System.currentTimeMillis());
                intent.putExtra("NOTE", sendNote);
                intent.putExtra("POS", pos);
                setResult(Activity.RESULT_OK, intent);
                finish();
            });

            builder.setNegativeButton("NO", (dialog, id) -> {
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            });

            builder.setMessage("Your note is not saved!\nSave note '" + editTitle.getText().toString() + "'?");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save) {
            // TODO send to info activity
//            Toast.makeText(this, "save click", Toast.LENGTH_SHORT).show();
            if(editTitle.getText().toString().isEmpty()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setPositiveButton("Ok", (dialog, id) -> {
                    Intent intent = new Intent();
                    Note sendNote = new Note(editTitle.getText().toString(), editText.getText().toString(),-1);
                    intent.putExtra("NOTE", sendNote);
                    intent.putExtra("POS", pos);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                });

                builder.setNegativeButton("Cancel", (dialog, id) -> {});

                builder.setMessage("Your note will not be saved without a title!\nExit without saving '" + editTitle.getText().toString() + "'?");

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
            else
            {
                Intent intent = new Intent();
                Note sendNote = new Note(editTitle.getText().toString(), editText.getText().toString(),System.currentTimeMillis());
                intent.putExtra("NOTE", sendNote);
                intent.putExtra("POS", pos);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            }

        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }
}
