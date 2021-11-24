package dev.handeul.autto.repository;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import dev.handeul.autto.AuttoApp;
import dev.handeul.autto.webservice.DHLotteryService;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class BaseRepository {
    private static final String TAG = "BaseRepository";
    private static final BaseRepository instance = new BaseRepository();

    private final DHLotteryService service;


    private BaseRepository() {
        String BASE_URL = "https://dhlottery.co.kr/";
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(AuttoApp.getContext()));

        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36")
                            .header("Connection", "keep-alive")
                            .header("Cache-Control", "max-age=0")
                            .header("Upgrade-Insecure-Requests", "1")
                            .header("Origin", "https://dhlottery.co.kr")
                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                            .header("Referer", "https://dhlottery.co.kr/")
                            .header("Sec-Fetch-Site", "same-site")
                            .header("Sec-Fetch-Mode", "navigate")
                            .header("Sec-Fetch-User", "?1")
                            .header("Sec-Fetch-Dest", "document")
                            .header("Accept-Language", "ko,en-US;q=0.9,en;q=0.8,ko-KR;q=0.7")
                            .build();

                    return chain.proceed(request);
                })
                .cookieJar(cookieJar)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .build();

        service = retrofit.create(DHLotteryService.class);
    }

    public static BaseRepository getInstance() {
        return instance;
    }

    public DHLotteryService getService() {
        return service;
    }

}
