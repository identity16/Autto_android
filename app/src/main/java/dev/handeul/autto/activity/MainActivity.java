package dev.handeul.autto.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import dev.handeul.autto.AuttoApp;
import dev.handeul.autto.R;
import dev.handeul.autto.databinding.ActivityMainBinding;
import dev.handeul.autto.fragment.DepositFragment;
import dev.handeul.autto.fragment.NumberListFragment;

public class MainActivity extends AppCompatActivity {
//    private static boolean isCleared = false;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Autto);
        super.onCreate(savedInstanceState);

        // TODO: Remove the code clearing SharedPreferences when app is started
//        if(!isCleared) {
//            Context context = AuttoApp.getContext();
//            context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
//                    .edit().clear().apply();
//            isCleared = true;
//        }

        dev.handeul.autto.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
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

        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                NumberListFragment fragBuyList = (NumberListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentBuyList);
                DepositFragment fragDeposit = (DepositFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentDeposit);
                assert fragBuyList != null;
                assert fragDeposit != null;

                fragBuyList.update();
                fragDeposit.update();
            }
        };

        LocalBroadcastManager.getInstance(AuttoApp.getContext()).registerReceiver(
                mMessageReceiver, new IntentFilter("Update"));
    }
}