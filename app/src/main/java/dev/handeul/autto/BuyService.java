package dev.handeul.autto;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.function.Consumer;

import dev.handeul.autto.activity.MainActivity;
import dev.handeul.autto.repository.BaseRepository;
import dev.handeul.autto.repository.RoundRepository;
import dev.handeul.autto.webservice.DHLotteryService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BuyService extends IntentService {
    private static final String TAG = "BuyService";

    private static final String ACTION_BUY = "dev.handeul.autto.action.BUY";

    private static final String PARAM_AMOUNT = "dev.handeul.autto.extra.amount";
    private final DHLotteryService lotteryService;

    public BuyService() {
        super("BuyService");
        lotteryService = BaseRepository.getInstance().getService();
    }

    public static void startActionBuy(Context context, int amount) {
        Intent intent = new Intent(context, BuyService.class);
        intent.setAction(ACTION_BUY);
        intent.putExtra(PARAM_AMOUNT, amount);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_BUY.equals(action)) {
                final int amount = intent.getIntExtra(PARAM_AMOUNT, 0);
                handleActionBuy(amount);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBuy(int paramAmount) {
        Log.d(TAG, String.valueOf(paramAmount));
        int count = paramAmount / 1000;
        StringBuilder jsonParamBuilder = new StringBuilder();
        jsonParamBuilder.append('[');
        for(int i=0; i<count; i++) {
            if(i > 0) {
                jsonParamBuilder.append(",");
            }

            jsonParamBuilder.append('{')
                    .append("\"genType\": \"0\", \"arrGameChoiceNum\": null, \"alpabet\": \"")
                    .append((char) ('A' + i))
                    .append("\"}");
        }
        jsonParamBuilder.append(']');
        Log.d(TAG, jsonParamBuilder.toString());

        RoundRepository.getInstance().getLatestRound(round-> {
            int roundNo = round.getId() + 1;

            Call<ResponseBody> call = lotteryService.buyLotto(
                    "https://ol.dhlottery.co.kr/olotto/game/execBuy.do", roundNo,
                    "172.17.20.52", paramAmount,
                    jsonParamBuilder.toString(), count);

            Consumer<Response<ResponseBody>> handleResponse = (response) -> {
                try {
                    ResponseBody body = response.body();
                    assert body != null;
                    Log.d(TAG, body.string());

                    Toast.makeText(AuttoApp.getContext(), "성공적으로 구매했습니다!", Toast.LENGTH_LONG).show();

                    Intent mainIntent = new Intent("Update");

                    LocalBroadcastManager.getInstance(AuttoApp.getContext()).sendBroadcast(mainIntent);

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
        });

    }
}