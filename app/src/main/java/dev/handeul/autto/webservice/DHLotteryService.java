package dev.handeul.autto.webservice;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DHLotteryService {
    @GET("gameResult.do?drwNoStart=1&drwNoEnd=999999&method=allWinExel")
    Call<ResponseBody> getRoundHistoryPage();

    @GET("gameResult.do?method=byWin")
    Call<ResponseBody> getRoundResult();

    @GET("gameResult.do")
    Call<ResponseBody> dummyReqForSession();

    @FormUrlEncoded
    @POST("userSsl.do?method=login")
    Call<ResponseBody> login(
            @Field("userId") String userId,
            @Field("password") String password,
            @Field("returnUrl") String returnUrl,
            @Field("checkSave") String checkSave);

    @FormUrlEncoded
    @POST("myPage.do?method=lottoBuyList")
    Call<ResponseBody> getBuyHistoryPage(
            @Field("nowPage") int nowPage,
            @Field("searchStartDate") String startDate,
            @Field("searchEndDate") String endDate,
            @Field("lottoId") String lottoId,
            @Field("winGrade") int winGrade,
            @Field("calendarStartDt") String calendarStartDate,
            @Field("calendarEndDt") String calendarEndDate,
            @Field("sortOrder") String sortOrder);

    @GET("myPage.do?method=lotto645Detail&orderNo=_&issueNo=1")
    Call<ResponseBody> getPaperPage(
            @Query("barcode") String barcode
    );
}
