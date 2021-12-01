package dev.handeul.autto.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dev.handeul.autto.AuttoApp;
import dev.handeul.autto.databinding.ActivityLoginBinding;
import dev.handeul.autto.repository.UserRepository;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        binding.btnLogin.setOnClickListener((v) -> doLogin(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            finish();
        }));

        binding.linkNoAccount.setOnClickListener((v) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://m.dhlottery.co.kr/loginJoin.do?method=joinFormAgree"));
            startActivity(browserIntent);
        });
    }

    private void doLogin(Runnable callback) {
        UserRepository repo = UserRepository.getInstance();
        repo.setUserAccount(binding.inputID.getText().toString(), binding.inputPW.getText().toString());
        repo.login((isSuccess) -> {
            if(isSuccess) {
                callback.run();
            } else {
                Toast.makeText(getApplicationContext(), "로그인이 실패했습니다 :(", Toast.LENGTH_LONG)
                        .show();

                Log.e(TAG, "Login Failed..");
            }
        });
    }
}