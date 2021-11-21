package dev.handeul.autto.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Consumer;

import dev.handeul.autto.model.Round;
import dev.handeul.autto.webservice.DHLotteryService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DHLotteryRepository {
    private static final String TAG = "DHLottery";
    private static final DHLotteryRepository instance = new DHLotteryRepository();

    private final DHLotteryService service;

    private DHLotteryRepository() {
        String BASE_URL = "https://dhlottery.co.kr/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(DHLotteryService.class);
    }

    public static DHLotteryRepository getInstance() {
        return instance;
    }

    public void listRounds(Consumer<List<Round>> callback) {
        Call<ResponseBody> historyPage = this.service.getRoundHistoryPage();

        historyPage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    List<Round> roundList = new ArrayList<>();
                    ResponseBody body = response.body();
                    String html = "";

                    if (body != null) {
                        html = body.string();
                    }

                    Document doc = Jsoup.parse(html);
                    Elements eRows = doc.select("tr:nth-child(n+3)");

                    for(Element eRow : eRows) {
                        Elements eCells = eRow.select("td:not([rowspan])");

                        Round round;

                        int roundId;
                        int[] numbers = {1, 2, 3, 4, 5, 6};
                        int bonusNum;
                        Date date = new Date();


                        // Round Id
                        roundId = Integer.parseInt(eCells.get(0).text());

                        // Date
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");

                        Date tmpDate = df.parse(eCells.get(1).text());
                        if(tmpDate != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(tmpDate);
                            calendar.set(Calendar.HOUR_OF_DAY, 20);
                            calendar.set(Calendar.MINUTE, 45);
                            calendar.set(Calendar.SECOND, 0);

                            date=calendar.getTime();
                        }

                        // Numbers
                        for(int i=0; i<6; i++) {
                            numbers[i] = Integer.parseInt(eCells.get(12 + i).text());
                        }

                        // Bonus Number
                        bonusNum = Integer.parseInt(eCells.get(18).text());

                        round = new Round(roundId, numbers, bonusNum, date);
                        roundList.add(round);
                    }

                    callback.accept(roundList);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Round Contruction Error!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
            }
        });
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
