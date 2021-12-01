package dev.handeul.autto.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.function.Consumer;

import dev.handeul.autto.AuttoApp;
import dev.handeul.autto.R;
import dev.handeul.autto.activity.LoginActivity;
import dev.handeul.autto.webservice.DHLotteryService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private static final String TAG = "UserRepository";
    @SuppressLint("StaticFieldLeak")
    private static final UserRepository instance = new UserRepository();

    private final DHLotteryService service;
    private final SharedPreferences sharedPreferences;

    private UserRepository() {
        service = BaseRepository.getInstance().getService();
        Context context = AuttoApp.getContext();
        sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    public static UserRepository getInstance() {
        return instance;
    }

    public void login(Consumer<Boolean> callback) {
        getUserAccount((account) -> {
            if(account == null) {
                callback.accept(false);
                return;
            }

            Call<ResponseBody> dummyReq = this.service.dummyReqForSession(); // Assign Session ID
            Call<ResponseBody> loginReq = this.service.login(account[0], account[1], "on");

            Callback<ResponseBody> loginCb = new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    Log.d(TAG, "loginReq");
                    try {
                        ResponseBody body = response.body();
                        String html;

                        if (body == null) {
                            callback.accept(true);
                            return;
                        }

                        html = body.string();

                        Document doc = Jsoup.parse(html);
                        Element eLogin = doc.selectFirst(".account .log");

                        if(eLogin != null && "로그인".equals(eLogin.text())) {
                            callback.accept(false);
                        } else {
                            callback.accept(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

                }
            };
            Callback<ResponseBody> dummyCb = new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    Log.d(TAG, "dummyReq");
                    loginReq.enqueue(loginCb);
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {

                }
            };

            dummyReq.enqueue(dummyCb);
        });
    }

    private void getUserAccount(Consumer<String[]> callback) {
        String uid = sharedPreferences.getString("uid", null);
        String password = sharedPreferences.getString("password", null);

        if (uid == null || password == null) {
            callback.accept(null);
        } else {
            callback.accept(new String[]{uid, password});
            Log.d(TAG, "I got " + uid + ", " + password);
        }
    }

    public void setUserAccount(String uid, String password) {
        sharedPreferences.edit()
                .putString("uid", uid)
                .putString("password", password)
                .apply();
    }
}
