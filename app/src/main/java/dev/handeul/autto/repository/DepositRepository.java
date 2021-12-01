package dev.handeul.autto.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.function.Consumer;

import dev.handeul.autto.webservice.DHLotteryService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepositRepository {
    private static final String TAG = "DepositRepository";
    private static final DepositRepository instance = new DepositRepository();

    private final DHLotteryService service;

    private DepositRepository() {
        service = BaseRepository.getInstance().getService();
    }

    public static DepositRepository getInstance() {
        return instance;
    }

    public void getMyDeposit(Consumer<Integer> callback) {
        UserRepository userRepo = UserRepository.getInstance();

        // 가장 적은 용량의 페이지에서 GNB에 있는 예치금 정보 파싱
        Call<ResponseBody> call =  service.getLightestPage();

        Consumer<Response<ResponseBody>> handleResponse = (response) -> {
            try {
                ResponseBody body = response.body();
                String html = "";

                if (body != null) {
                    html = body.string();
                }

                Document doc = Jsoup.parse(html);
                Element eLogin = doc.selectFirst(".log");
                assert eLogin != null;

                if("로그아웃".equals(eLogin.text())) {
                    Log.d(TAG, "예치금 가져오기 성공");
                    Element eMoney = doc.selectFirst(".money a");
                    assert eMoney != null;

                    Integer balance = Integer.parseInt(
                            eMoney.text().replaceAll("[^0-9]", ""));

                    callback.accept(balance);
                } else {
                    Log.d(TAG, "로그인 재시도");
                    userRepo.login((isSuccess) -> {
                        if(isSuccess) {
                            getMyDeposit(callback);
                        } else {
                            callback.accept(null);
                        }
                    });
                }


            } catch(Exception e) {
                e.printStackTrace();
            }
        };


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                handleResponse.accept(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {}
        });
    }
}
