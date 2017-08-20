package com.sampletexteditor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sampletexteditor.model.TextEditorModel;
import com.sampletexteditor.utils.GetDatabaseTexts;
import com.sampletexteditor.utils.TextEditorDB;

import java.util.ArrayList;

import jp.wasabeef.richeditor.RichEditor;

public class MainActivity extends AppCompatActivity implements GetDatabaseTexts.SavedTexts{

    private RichEditor mEditor;
    private TextView wordCount;

    private String previewText;

    TextEditorDB database;
    GetDatabaseTexts.SavedTexts savedTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new TextEditorDB(getApplicationContext());

        savedTexts = this;

        mEditor = (RichEditor) findViewById(R.id.editor);
        wordCount = (TextView) findViewById(R.id.wordcount);

        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");

        mEditor.focusEditor();


        if(database.getTextsCount()>0){
            new GetDatabaseTexts(getApplicationContext(), savedTexts).execute();
        }


        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override public void onTextChange(String text) {
                previewText  = text;
//                Toast.makeText(MainActivity.this, "word count : "+wordCount(text), Toast.LENGTH_SHORT).show();
                wordCount.setText("Word Count = "+wordCount(text));
            }
        });




        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setItalic();
            }
        });


        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setUnderline();
            }
        });


        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                if(mEditor.getHtml().contains("<ul><li>")|| mEditor.getHtml().contains("blockquote")){
                    Toast.makeText(MainActivity.this, "Blockquotes and bullet points are not allowed to be aligned", Toast.LENGTH_SHORT).show();

                }else {
                    mEditor.setAlignLeft();
                }
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(mEditor.getHtml().contains("<ul><li>")|| mEditor.getHtml().contains("blockquote")){
                    Toast.makeText(MainActivity.this, "Blockquotes and bullet points are not allowed to be aligned", Toast.LENGTH_SHORT).show();

                }else {
                    mEditor.setAlignCenter();
                }
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(mEditor.getHtml().contains("<ul><li>")|| mEditor.getHtml().contains("blockquote")){
                    Toast.makeText(MainActivity.this, "Blockquotes and bullet points are not allowed to be aligned", Toast.LENGTH_SHORT).show();

                }else {
                    mEditor.setAlignRight();
                }
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Log.d("Sample","tag : "+mEditor.getHtml());

                if(mEditor.getHtml().contains("<ul><li>")){
                    Toast.makeText(MainActivity.this, "Blockquotes and bullet points are not allowed together", Toast.LENGTH_SHORT).show();

                }else {
                    mEditor.setBlockquote();
                }
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Log.d("Sample","tag : "+mEditor.getHtml());
                if(mEditor.getHtml().contains("blockquote")){

                    Toast.makeText(MainActivity.this, "Blockquotes and bullet points are not allowed together", Toast.LENGTH_SHORT).show();
                }else {
                    mEditor.setBullets();
                }
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                if(mEditor.getHtml().contains("blockquote")){
                    Toast.makeText(MainActivity.this, "Blockquotes and bullet points are not allowed together", Toast.LENGTH_SHORT).show();

                }else {
                    mEditor.setNumbers();
                }
            }
        });

        findViewById(R.id.previewBtn).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                String previewTextWithBody = "<body>"+previewText+"</body>";
                startActivity(new Intent(MainActivity.this,PreviewActivity.class).putExtra("preview_text",previewTextWithBody));
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                String previewTextWithBody = "<body>"+previewText+"</body>";

                database.addTexts(new TextEditorModel(previewTextWithBody));

                        }
        });


    }

    public int wordCount(String str){

        String words = str.trim();
        if (words.isEmpty())
            return 0;
        return words.split("\\s+").length;
    }

    @Override
    public void getSavedText(ArrayList<String> savedTexts) {

        String example = "";
        for (int i = 0; i < savedTexts.size(); i++) {
            example = example+savedTexts.get(i);
        }

        mEditor.setHtml(example);
        mEditor.focusEditor();

        previewText = mEditor.getHtml();



//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//           Spanned spanned_text = Html.fromHtml(previewText,Html.FROM_HTML_MODE_LEGACY);
//            wordCount(previewText);
//        }else {
//            Spanned spanned_text = Html.fromHtml(previewText);
//            wordCount(previewText);
//        }


    }
}
