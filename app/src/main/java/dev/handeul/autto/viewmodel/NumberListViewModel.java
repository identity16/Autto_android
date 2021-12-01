package dev.handeul.autto.viewmodel;

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
        MutableLiveData<List<Game>> liveBuyList = new MutableLiveData<>();

        Calendar calendar = Calendar.getInstance();
        Date today;

        today = calendar.getTime();

        RoundRepository.getInstance().getLatestRound(round -> {
            calendar.setTime(round.getDate());
            calendar.add(Calendar.DATE, 1);

            Date startDate = calendar.getTime();
            GameRepository.getInstance().getBuyHistory(startDate, today, liveBuyList::setValue);
        });

        buyList = liveBuyList;
    }
}
