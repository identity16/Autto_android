package dev.handeul.autto.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dev.handeul.autto.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Autto);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}