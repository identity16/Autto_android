package dev.handeul.autto.webservice;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DHLotteryService {
    @GET("gameResult.do?drwNoStart=1&drwNoEnd=999999&method=allWinExel")
    Call<ResponseBody> getRoundHistoryPage();

    @GET("gameResult.do?method=byWin")
    Call<ResponseBody> getRoundResult();
}
