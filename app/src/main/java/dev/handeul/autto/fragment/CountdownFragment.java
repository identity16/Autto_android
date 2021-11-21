package dev.handeul.autto.fragment;

import android.annotation.SuppressLint;
import android.icu.number.Scale;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import dev.handeul.autto.databinding.FragmentCountdownBinding;
import dev.handeul.autto.viewmodel.CountdownViewModel;

public class CountdownFragment extends Fragment {
    FragmentCountdownBinding binding;
    private CountdownViewModel viewModel;

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CountdownViewModel.class);
        binding = FragmentCountdownBinding.inflate(inflater, container, false);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd일 HH시간 mm분 ss초");

        viewModel.nextRound.observe(getViewLifecycleOwner(), round -> {
            String roundStr = Integer.toString(round.getId());
            binding.drwNoText.setText(roundStr);
        });
        viewModel.remainTime.observe(getViewLifecycleOwner(), (remainTime) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2020, 12, 31, 0, 0, 0);
            calendar.add(Calendar.MILLISECOND, remainTime.intValue());

            String countdownStr = dateFormat.format(calendar.getTime());

            binding.drwCountdown.setText(countdownStr);
        });

        return binding.getRoot();
    }
}