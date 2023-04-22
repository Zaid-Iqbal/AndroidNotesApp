package com.example.androidnotesapp;

import android.util.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Serializable {

    private String Title;
    private String Text;
    private long Date;

    public Note(String title, String text, long date){
        Title = title;
        Text = text;
        Date = date;
    }

    public Note(String title, String text, String date) throws ParseException {
        Title = title;
        Text = text;
        Date = new SimpleDateFormat("E MMM dd HH:mm aa").parse(date).getTime();
    }

    public String getTitle(){ return Title;}

    public void setTitle(String title){ Title = title;}

    public String getText(){ return Text;}

    public void setText(String text){ Text = text;}

    public long getDate(){ return Date;}

    public void setDate(long time){ Date = time;}

    public String getDateString(){ return (new SimpleDateFormat("E MMM dd HH:mm aa")).format(Date);}

    public JSONObject toJSON() {
        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();

            JSONObject obj = new JSONObject();

            obj.put("Title", Title);
            obj.put("Text", Text);
            obj.put("Date", Date);

            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
