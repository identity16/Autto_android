package dev.handeul.autto.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import dev.handeul.autto.AuttoApp;
import dev.handeul.autto.R;
import dev.handeul.autto.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static boolean isCleared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Autto);
        super.onCreate(savedInstanceState);

        // TODO: Remove the code clearing SharedPreferences when app is started
        if(!isCleared) {
            Context context = AuttoApp.getContext();
            context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    .edit().clear().apply();
            isCleared = true;
        }

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.btnPurchase.setOnClickListener(v -> {
            Intent intent = new Intent(this, PurchaseSettingActivity.class);
            startActivity(intent);
        });

        binding.fragmentCountdown.setOnClickListener(v -> {
            Intent intent = new Intent(this, ResultActivity.class);
            startActivity(intent);
        });

        View view = binding.getRoot();
        setContentView(view);
    }
}