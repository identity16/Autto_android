package dev.handeul.autto.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import dev.handeul.autto.model.Game;
import dev.handeul.autto.model.Round;
import dev.handeul.autto.webservice.DHLotteryService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoundRepository {
    private static final String TAG = "RoundRepository";
    private static final RoundRepository instance = new RoundRepository();

    private final DHLotteryService service;

    private RoundRepository() {
        service = BaseRepository.getInstance().getService();
    }

    public static RoundRepository getInstance() {
        return instance;
    }

    public void getLatestRound(Consumer<Round> callback) {
        Call<ResponseBody> resultPage = this.service.getRoundResult();

        resultPage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Round latestRound = null;
                ResponseBody body = response.body();
                String html = "";

                try {
                    if (body != null) {
                        html = body.string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat = new SimpleDateFormat("(yyyy년 MM월 dd일 추첨)");

                Document doc = Jsoup.parse(html);
                Element eOption = doc.selectFirst("#dwrNoList option[selected]");
                Elements eBalls = doc.select(".win.num .ball_645");
                Element eBonus = doc.selectFirst(".bonus.num .ball_645");
                Element eDate = doc.selectFirst(".win_result .desc");

                if (eOption != null && eBonus != null && eDate != null) {
                    int roundId = Integer.parseInt(eOption.text());
                    int[] numbers = new int[6];
                    int bonusNum = Integer.parseInt(eBonus.text());
                    Date date = null;

                    for (int i = 0; i < 6; i++) {
                        Element eBall = eBalls.get(i);
                        int num = Integer.parseInt(eBall.text());
                        numbers[i] = num;
                    }

                    try {
                        Date tmpDate = dateFormat.parse(eDate.text());

                        if(tmpDate != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(tmpDate);
                            calendar.set(Calendar.HOUR_OF_DAY, 20);
                            calendar.set(Calendar.MINUTE, 45);
                            calendar.set(Calendar.SECOND, 0);

                            date=calendar.getTime();
                        }

                        latestRound = new Round(roundId, numbers, bonusNum, date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                callback.accept(latestRound);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
            }
        });
    }

}
