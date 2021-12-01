package dev.handeul.autto.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.handeul.autto.activity.ResultActivity;
import dev.handeul.autto.model.Game;
import dev.handeul.autto.model.Round;
import dev.handeul.autto.repository.DepositRepository;
import dev.handeul.autto.repository.GameRepository;
import dev.handeul.autto.repository.RoundRepository;

public class ResultViewModel extends ViewModel {
    private static final String TAG = "ResulltViewModel";

    public LiveData<Round> lastRound;
    public LiveData<List<Game>> buyList;

    public ResultViewModel() {
        MutableLiveData<Round> liveLastRound = new MutableLiveData<>();
        MutableLiveData<List<Game>> liveBuyList = new MutableLiveData<>();


        RoundRepository.getInstance().getLatestRound(round -> {
            liveLastRound.setValue(round);

            Calendar calendar = Calendar.getInstance();
            Date startDate, endDate = round.getDate();

            calendar.setTime(round.getDate());
            calendar.add(Calendar.DATE, -6);

            startDate = calendar.getTime();

            GameRepository.getInstance().getBuyHistory(startDate, endDate, liveBuyList::setValue);
        });

        lastRound = liveLastRound;
        buyList = liveBuyList;
    }
}
