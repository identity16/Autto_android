package dev.handeul.autto.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import dev.handeul.autto.AuttoApp;
import dev.handeul.autto.R;
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

    public void login(Runnable callback) {
        String[] account = getUserAccount();
        Call<ResponseBody> dummyReq = this.service.dummyReqForSession(); // Assign Session ID
        Call<ResponseBody> loginReq = this.service.login(account[0], account[1], "https://www.dhlottery.co.kr/common.do?method=main", "on");

        Callback<ResponseBody> loginCb = new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d(TAG, "loginReq");
                callback.run();
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
    }

    private String[] getUserAccount() {
        String uid = sharedPreferences.getString("uid", null);
        String password = sharedPreferences.getString("password", null);

        if (uid == null || password == null) {
            // TODO: 아이디/패스워드 입력 화면으로 이동
            Log.d(TAG, "Hard Coded uid/password");
            uid = "<USER ID>";
            password = "<PASSWORD>";
            setUserAccount(uid, password);
        } else {
            Log.d(TAG, "I got " + uid + ", " + password);
        }


        return new String[]{uid, password};
    }

    private void setUserAccount(String uid, String password) {
        sharedPreferences.edit()
                .putString("uid", uid)
                .putString("password", password)
                .apply();
    }
}
