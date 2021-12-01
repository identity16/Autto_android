package dev.handeul.autto.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import dev.handeul.autto.databinding.ActivityPurchaseSettingBinding;

public class PurchaseSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPurchaseSettingBinding binding =
                ActivityPurchaseSettingBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        setContentView(view);

    }
}