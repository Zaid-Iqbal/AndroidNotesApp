package com.example.androidnotesapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteViewAdapter extends RecyclerView.Adapter<NoteViewHolder>
{

    private final List<Note> noteList;
    private final MainActivity mainAct;

    NoteViewAdapter(List<Note> empList, MainActivity ma) {
        this.noteList = empList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Note note = noteList.get(position);

        holder.Title.setText(note.getTitle());
        holder.Text.setText(note.getText());
        holder.Date.setText(note.getDateString());
    }

    @Override
    public int getItemCount() {
        if (noteList == null){
            return 0;
        }
        return noteList.size();
    }

}
