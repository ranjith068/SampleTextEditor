package com.sampletexteditor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PreviewActivity extends AppCompatActivity {

    private TextView mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        mPreview = (TextView) findViewById(R.id.preview);

        String previewText = getIntent().getStringExtra("preview_text");
        mPreview.setText(previewText);
    }
}
