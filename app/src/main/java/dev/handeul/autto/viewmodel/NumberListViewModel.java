package dev.handeul.autto.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.handeul.autto.model.Game;
import dev.handeul.autto.repository.GameRepository;
import dev.handeul.autto.repository.RoundRepository;

public class NumberListViewModel extends ViewModel {
    private static final String TAG = "NumberListViewModel";

    public LiveData<List<Game>> buyList;

    public NumberListViewModel() {
        buyList = new MutableLiveData<>();

        update();
    }

    public void update() {
        Log.d(TAG, "update called!");
        MutableLiveData<List<Game>> liveBuyList = (MutableLiveData<List<Game>>) buyList;

        Calendar calendar = Calendar.getInstance();
        Date today;

        today = calendar.getTime();

        RoundRepository.getInstance().getLatestRound(round -> {
            calendar.setTime(round.getDate());
            calendar.add(Calendar.DATE, 1);

            Date startDate = calendar.getTime();
            GameRepository.getInstance().getBuyHistory(startDate, today, liveBuyList::setValue);
        });
    }
}
