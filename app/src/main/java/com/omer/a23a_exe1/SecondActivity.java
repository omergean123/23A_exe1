package com.omer.a23a_exe1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class SecondActivity extends AppCompatActivity {
    private TextView TXT_Success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);
        TXT_Success = findViewById(R.id.TXT_Success);
    }
}
