package dev.handeul.autto.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.handeul.autto.model.Game;
import dev.handeul.autto.repository.DepositRepository;
import dev.handeul.autto.repository.GameRepository;
import dev.handeul.autto.repository.RoundRepository;

public class DepositViewModel extends ViewModel {
    private static final String TAG = "DepositViewModel";

    public LiveData<Integer> balance;

    public DepositViewModel() {
        balance = new MutableLiveData<>();

        update();
    }

    public void update() {
        Log.d(TAG, "update called!");
        MutableLiveData<Integer> liveBalance = (MutableLiveData<Integer>) balance;
        DepositRepository.getInstance().getMyDeposit(liveBalance::setValue);
    }
}
