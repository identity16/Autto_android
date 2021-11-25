package dev.handeul.autto.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dev.handeul.autto.databinding.FragmentDepositBinding;
import dev.handeul.autto.viewmodel.DepositViewModel;

public class DepositFragment extends Fragment {
    FragmentDepositBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        DepositViewModel viewModel = new ViewModelProvider(this).get(DepositViewModel.class);
        binding = FragmentDepositBinding.inflate(inflater, container, false);

        viewModel.balance.observe(getViewLifecycleOwner(), balance ->
                binding.depositBalance.setText(String.valueOf(balance)));

        binding.button.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://m.dhlottery.co.kr/myPage.do?method=depositListView"));
            startActivity(browserIntent);
        });

        return binding.getRoot();
    }
}