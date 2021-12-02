package dev.handeul.autto.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import dev.handeul.autto.R;
import dev.handeul.autto.databinding.ActivityPurchaseSettingBinding;

public class PurchaseSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPurchaseSettingBinding binding =
                ActivityPurchaseSettingBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnConfirm.setOnClickListener(v -> {
            // TODO: 주어진 설정값 저장 및 서비스 (재)시작
            finish();
        });

        setContentView(view);


        // Initialize Spinner
        Spinner freqSpinner = findViewById(R.id.freqSpinner);
        ArrayAdapter<CharSequence> freqAdapter = ArrayAdapter.createFromResource(this,
                R.array.purchase_frequency, android.R.layout.simple_spinner_item);
        freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freqSpinner.setAdapter(freqAdapter);

        Spinner amountSpinner = findViewById(R.id.amountSpinner);
        ArrayAdapter<CharSequence> amountAdapter = ArrayAdapter.createFromResource(this,
                R.array.purchase_amount, android.R.layout.simple_spinner_item);
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amountSpinner.setAdapter(amountAdapter);

        Spinner autoSpinner = findViewById(R.id.autoSpinner);
        ArrayAdapter<CharSequence> autoAdapter = ArrayAdapter.createFromResource(this,
                R.array.purchase_auto, android.R.layout.simple_spinner_item);
        autoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoSpinner.setAdapter(autoAdapter);
    }
}