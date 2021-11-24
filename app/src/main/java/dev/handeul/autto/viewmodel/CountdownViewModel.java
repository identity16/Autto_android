package dev.handeul.autto.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import dev.handeul.autto.model.Round;
import dev.handeul.autto.repository.RoundRepository;

public class CountdownViewModel extends ViewModel {
    private static final String TAG = "CountdownViewModel";

    public LiveData<Round> nextRound;
    public LiveData<Long> remainTime;

    public CountdownViewModel() {
        MutableLiveData<Round> liveRound = new MutableLiveData<>();
        MutableLiveData<Long> liveRemain = new MutableLiveData<>();

        RoundRepository.getInstance().getLatestRound((round) -> {
            Calendar nextDate = Calendar.getInstance();
            nextDate.setTime(round.getDate());
            nextDate.add(Calendar.DATE, 7);

            liveRound.setValue(new Round(round.getId() + 1, nextDate.getTime()));
        });

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                Round round = nextRound.getValue();
                if(round == null) return;

                Date next = round.getDate();
                Date now = new Date();

                long diff = next.getTime() - now.getTime();
                liveRemain.postValue(diff);
            }
        };
        timer.schedule(timerTask, 0, 1000);

        nextRound = liveRound;
        remainTime = liveRemain;
    }
}
