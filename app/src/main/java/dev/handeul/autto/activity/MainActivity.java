package dev.handeul.autto.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import dev.handeul.autto.AuttoApp;
import dev.handeul.autto.R;

public class MainActivity extends AppCompatActivity {
    private static boolean isCleared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Autto);
        super.onCreate(savedInstanceState);

        if(!isCleared) {
            Context context = AuttoApp.getContext();
            context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    .edit().clear().apply();
            isCleared = true;
        }
        setContentView(R.layout.activity_main);
    }
}