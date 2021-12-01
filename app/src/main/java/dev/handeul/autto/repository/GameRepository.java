package dev.handeul.autto.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import dev.handeul.autto.model.Game;
import dev.handeul.autto.webservice.DHLotteryService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRepository {
    private static final String TAG = "GameRepository";
    private static final GameRepository instance = new GameRepository();

    private final DHLotteryService service;

    private GameRepository() {
        service = BaseRepository.getInstance().getService();
    }

    public static GameRepository getInstance() {
        return instance;
    }


    @SuppressLint("SimpleDateFormat")
    public void getBuyHistory(Date startDate, Date endDate, Consumer<List<Game>> callback) {
        UserRepository userRepo = UserRepository.getInstance();
        SimpleDateFormat dfNoDash = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dfWithDash = new SimpleDateFormat("yyyy-MM-dd");


        Call<ResponseBody> call =  service.getBuyHistoryPage(
                1, dfNoDash.format(startDate), dfNoDash.format(endDate),
                "LO40", 2,
                dfWithDash.format(startDate), dfWithDash.format(endDate),
                "DESC");

        Consumer<Response<ResponseBody>> handleResponse = (response) -> {
            try {
                ResponseBody body = response.body();
                String html = "";

                if (body != null) {
                    html = body.string();
                }

                Document doc = Jsoup.parse(html);
                Element eLogin = doc.selectFirst(".nodata");

                if(eLogin == null) {
                    Log.d(TAG, "구매 이력 가져오기 성공");
                    Elements eRows = doc.select("tbody tr");
                    List<String> barcodeList = new ArrayList<>();
                    for(Element eRow : eRows) {
                        Elements eCells = eRow.getElementsByTag("td");
                        String barcode = eCells.get(3).text();

                        barcodeList.add(barcode);
                    }

                    getGamesFromBarcode(barcodeList, callback);
                } else if(eLogin.html().contains("로그인")) {
                    Log.d(TAG, "로그인 재시도");
                    userRepo.login((isSuccess) -> {
                        if(isSuccess) {
                            getBuyHistory(startDate, endDate, callback);
                        } else {
                            callback.accept(null);
                        }
                    });
                } else {
                    callback.accept(new ArrayList<>());
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

    private void getGamesFromBarcode(String barcode, Consumer<List<Game>> callback) {
        UserRepository userRepo = UserRepository.getInstance();
        String barCodeWithoutSpace = barcode.replace(" ", "");


        Call<ResponseBody> call =  service.getPaperPage(barCodeWithoutSpace);

        Consumer<Response<ResponseBody>> handleResponse = (response) -> {
            try {
                List<Game> gameList = new ArrayList<>();
                ResponseBody body = response.body();
                String html = "";

                if (body != null) {
                    html = body.string();
                } else {
                    Log.d(TAG, "로그인 재시도");
                    userRepo.login((isSuccess) -> {
                        if(isSuccess) {
                            getGamesFromBarcode(barCodeWithoutSpace, callback);
                        } else {
                            callback.accept(null);
                        }
                    });
                }

                Document doc = Jsoup.parse(html);

                Log.d(TAG, "게임정보 가져오기 성공");
                Element eRoundId = doc.selectFirst(".date-info h3 strong");
                Elements eGames = doc.select(".selected ul li");

                assert eRoundId != null;

                int roundId = Integer.parseInt(
                        eRoundId.text().replaceAll("[^0-9]", ""));


                for(Element eGame : eGames) {
                    Element eState = eGame.selectFirst("strong span:last-child");
                    Element eNums = eGame.selectFirst(".nums");

                    assert eState != null;
                    assert eNums != null;

                    String state = eState.text();

                    String[] sNumbers = eNums.text().split(" ");
                    Log.d(TAG, eNums.text());
                    Integer[] iNumbers = new Integer[6];
                    for(int i=0; i<sNumbers.length; i++) {
                        iNumbers[i] = Integer.parseInt(sNumbers[i]);
                    }

                    gameList.add(new Game(roundId, iNumbers, state));
                }
                callback.accept(gameList);


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

    private void getGamesFromBarcode(List<String> barcodes, Consumer<List<Game>> callback) {
        final int[] idx = {0};
        int len = barcodes.size();
        List<Game> gameList = new ArrayList<>();

        getGamesFromBarcode(barcodes.get(idx[0]), games -> {
            gameList.addAll(games);
            idx[0]++;
            if(idx[0] == len) {
                callback.accept(gameList);
            }
        });
    }
}
