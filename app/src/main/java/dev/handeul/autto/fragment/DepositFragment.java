package dev.handeul.autto.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dev.handeul.autto.activity.LoginActivity;
import dev.handeul.autto.databinding.FragmentDepositBinding;
import dev.handeul.autto.viewmodel.DepositViewModel;

public class DepositFragment extends Fragment {
    private FragmentDepositBinding binding;
    private DepositViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DepositViewModel.class);
        binding = FragmentDepositBinding.inflate(inflater, container, false);

        viewModel.balance.observe(getViewLifecycleOwner(), balance -> {
            if(balance == null) {
                Activity parentActivity = getActivity();
                assert parentActivity != null;

                Intent intent = new Intent(parentActivity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                parentActivity.finish();
                return;
            }

            binding.depositBalance.setText(String.valueOf(balance));
        });

        binding.button.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://m.dhlottery.co.kr/myPage.do?method=depositListView"));
            startActivity(browserIntent);
        });

        return binding.getRoot();
    }

    public void update() {
        viewModel.update();
    }
}