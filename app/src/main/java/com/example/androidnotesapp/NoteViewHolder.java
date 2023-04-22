package com.example.androidnotesapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    public TextView Title;
    public TextView Text;
    public TextView Date;

    NoteViewHolder(View view) {
        super(view);
        Title = view.findViewById(R.id.TitleText);
        Date = view.findViewById(R.id.DateText);
        Text = view.findViewById(R.id.TextText);
    }
}
