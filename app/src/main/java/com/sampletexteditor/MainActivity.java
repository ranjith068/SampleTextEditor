package com.sampletexteditor;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sampletexteditor.model.TextEditorModel;
import com.sampletexteditor.utils.GetDatabaseTexts;
import com.sampletexteditor.utils.TextEditorDB;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

public class MainActivity extends AppCompatActivity implements GetDatabaseTexts.SavedTexts{

    private RichEditor mEditor;
    private TextView wordCount;

    private String previewText;

    TextEditorDB database;
    GetDatabaseTexts.SavedTexts savedTexts;

    boolean isBold = false;
    boolean isItalic = false;
    boolean isUnderline = false;

    boolean isBlockQuotes = false;

    @BindView(R.id.action_bold)
    public ImageButton boldButton;
    @BindView(R.id.action_italic)
    public ImageButton italicButton;
    @BindView(R.id.action_underline)
    public ImageButton underlineButton;
    @BindView(R.id.action_align_left)
    public ImageButton alignLeftButton;
    @BindView(R.id.action_align_center)
    public ImageButton alignCenterButton;
    @BindView(R.id.action_align_right)
    public ImageButton alignRightButton;
    @BindView(R.id.action_blockquote)
    public ImageButton blockquotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        database = new TextEditorDB(getApplicationContext());

        savedTexts = this;

        mEditor = (RichEditor) findViewById(R.id.editor);
        wordCount = (TextView) findViewById(R.id.wordcount);

        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);
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


    }

    @OnClick(R.id.previewBtn)
    public void actionPreviewBtn(View view) {

        String previewTextWithBody;
        if(previewText.contains("<body>")){
             previewTextWithBody = previewText;
        }else{
             previewTextWithBody = "<body>"+previewText+"</body>";
        }

        startActivity(new Intent(MainActivity.this,PreviewActivity.class).putExtra("preview_text",previewTextWithBody));


    }

    @OnClick(R.id.save)
    public void actionSave(View view) {

        if(database.getTextsCount()>0){
            database.deleteAll();
        }

        String previewTextWithBody = "<body>"+previewText+"</body>";

        database.addTexts(new TextEditorModel(previewTextWithBody));

        Toast.makeText(MainActivity.this,"Saved",Toast.LENGTH_SHORT).show();

    }



    @OnClick(R.id.action_bold)
    public void actionBold(View view) {

        if(isBold){
            Drawable mDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.bold).mutate();
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
            boldButton.setImageDrawable(mDrawable);
            isBold = false;

        }else{

            Drawable mDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.bold).mutate();
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN));
            boldButton.setImageDrawable(mDrawable);
            isBold = true;
        }

        mEditor.setBold();

    }

    @OnClick(R.id.action_italic)
    public void actionItalic(View view) {

        mEditor.setItalic();

        if(isItalic){

            Drawable mDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.italic).mutate();
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
            italicButton.setImageDrawable(mDrawable);
        }else{
            Drawable mDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.italic).mutate();
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN));
            italicButton.setImageDrawable(mDrawable);
        }
        isItalic = !isItalic; // reverse

    }

    @OnClick(R.id.action_underline)
    public void actionUnderline(View view) {

        mEditor.setUnderline();
        if(isUnderline){

            Drawable mDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.underline).mutate();
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
            underlineButton.setImageDrawable(mDrawable);
        }else{
            Drawable mDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.underline).mutate();
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN));
            underlineButton.setImageDrawable(mDrawable);
        }
        isUnderline = !isUnderline; // reverse

    }

    @OnClick(R.id.action_align_left)
    public void actionAlignLeft(View view){
        if(mEditor.getHtml().contains("<ul><li>")|| mEditor.getHtml().contains("blockquote")){
            Toast.makeText(MainActivity.this, "Blockquotes and bullet points are not allowed to be aligned", Toast.LENGTH_SHORT).show();

        }else {
            mEditor.setAlignLeft();

            Drawable mDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.justify_left).mutate();
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN));
            alignLeftButton.setImageDrawable(mDrawable);

            Drawable mDrawable_center = ContextCompat.getDrawable(getApplicationContext(), R.drawable.justify_center).mutate();
            mDrawable_center.setColorFilter(new
                    PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
            alignCenterButton.setImageDrawable(mDrawable_center);

            Drawable mDrawable_right = ContextCompat.getDrawable(getApplicationContext(), R.drawable.justify_right).mutate();
            mDrawable_right.setColorFilter(new
                    PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
            alignRightButton.setImageDrawable(mDrawable_right);
        }

    }

    @OnClick(R.id.action_align_center)
    public void actionAlignCenter(View view) {
        if(mEditor.getHtml().contains("<ul><li>")|| mEditor.getHtml().contains("blockquote")){
            Toast.makeText(MainActivity.this, "Blockquotes and bullet points are not allowed to be aligned", Toast.LENGTH_SHORT).show();

        }else {
            mEditor.setAlignCenter();

            Drawable mDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.justify_left).mutate();
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
            alignLeftButton.setImageDrawable(mDrawable);

            Drawable mDrawable_center = ContextCompat.getDrawable(getApplicationContext(), R.drawable.justify_center).mutate();
            mDrawable_center.setColorFilter(new
                    PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN));
            alignCenterButton.setImageDrawable(mDrawable_center);

            Drawable mDrawable_right = ContextCompat.getDrawable(getApplicationContext(), R.drawable.justify_right).mutate();
            mDrawable_right.setColorFilter(new
                    PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
            alignRightButton.setImageDrawable(mDrawable_right);
        }
    }

    @OnClick(R.id.action_align_right)
    public void actionAlignRight(View view) {
        if(mEditor.getHtml().contains("<ul><li>")|| mEditor.getHtml().contains("blockquote")){
            Toast.makeText(MainActivity.this, "Blockquotes and bullet points are not allowed to be aligned", Toast.LENGTH_SHORT).show();

        }else {
            mEditor.setAlignRight();

            Drawable mDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.justify_left).mutate();
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
            alignLeftButton.setImageDrawable(mDrawable);

            Drawable mDrawable_center = ContextCompat.getDrawable(getApplicationContext(), R.drawable.justify_center).mutate();
            mDrawable_center.setColorFilter(new
                    PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
            alignCenterButton.setImageDrawable(mDrawable_center);

            Drawable mDrawable_right = ContextCompat.getDrawable(getApplicationContext(), R.drawable.justify_right).mutate();
            mDrawable_right.setColorFilter(new
                    PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN));
            alignRightButton.setImageDrawable(mDrawable_right);
        }
    }

    @OnClick(R.id.action_blockquote)
    public void actionBlockQuote(View view) {
        Log.d("Sample","tag : "+mEditor.getHtml());

        if(isBlockQuotes){
            mEditor.undo();
            Drawable mDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.blockquote).mutate();
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));
            blockquotes.setImageDrawable(mDrawable);

        }else{
            if(mEditor.getHtml().contains("<ul><li>")){
                Toast.makeText(MainActivity.this, "Blockquotes and bullet points are not allowed together", Toast.LENGTH_SHORT).show();

            }else {
                mEditor.setBlockquote();
                Drawable mDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.blockquote).mutate();
                mDrawable.setColorFilter(new
                        PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN));
                blockquotes.setImageDrawable(mDrawable);

            }
        }
        isBlockQuotes = !isBlockQuotes; // reverse
    }

    @OnClick(R.id.action_insert_bullets)
    public void actionInsertBullets(View view) {
        Log.d("Sample","tag : "+mEditor.getHtml());
        if(mEditor.getHtml().contains("blockquote")){

            Toast.makeText(MainActivity.this, "Blockquotes and bullet points are not allowed together", Toast.LENGTH_SHORT).show();
        }else {
            mEditor.setBullets();
        }
    }

    @OnClick(R.id.action_insert_numbers)
    public void actionInsertNumbers(View view) {
        if(mEditor.getHtml().contains("blockquote")){
            Toast.makeText(MainActivity.this, "Blockquotes and bullet points are not allowed together", Toast.LENGTH_SHORT).show();

        }else {
            mEditor.setNumbers();
        }
    }

}
