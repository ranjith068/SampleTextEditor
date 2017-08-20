package com.sampletexteditor.utils;

import android.content.Context;
import android.os.AsyncTask;


import java.util.ArrayList;

/**
 * Created by ranjith on 30/4/17.
 */

public class GetDatabaseTexts extends AsyncTask<String,String,String> {
    String id;
    Context context;
    ArrayList<String> spot=new ArrayList<>();
    SavedTexts savedTexts;
    private TextEditorDB database;

    public GetDatabaseTexts(Context context, SavedTexts savedTexts)
    {
//        this.id=id;
        this.context=context;
        this.savedTexts=savedTexts;
    }
    @Override
    protected String doInBackground(String... strings) {


        database = new TextEditorDB(context);
        spot.clear();
        for(int i = 0;i < database.getTextsCount();i++){

            String texts =  database.getAllTexts().get(i).get_texts();
            spot.add(texts);

        }

//        database.deleteAll();

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(spot.size()>0) {
            savedTexts.getSavedText(spot);
        }

    }
    public interface SavedTexts
    {
        void getSavedText(ArrayList<String> savedTexts);
    }
}