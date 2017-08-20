package com.sampletexteditor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PreviewActivity extends AppCompatActivity {

    private TextView mPreview;
    String previewText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        mPreview = (TextView) findViewById(R.id.preview);

        Intent intent = getIntent();
        if (intent.hasExtra("preview_text")) {
            Bundle bd = getIntent().getExtras();
            if (!bd.getString("preview_text").equals(null)) {
                previewText = bd.getString("preview_text");
            }
        }

        mPreview.setText(previewText);
    }
}
