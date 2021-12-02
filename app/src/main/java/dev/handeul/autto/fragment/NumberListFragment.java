package dev.handeul.autto.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dev.handeul.autto.activity.LoginActivity;
import dev.handeul.autto.databinding.FragmentNumberListBinding;
import dev.handeul.autto.model.Game;
import dev.handeul.autto.utils.UnitUtils;
import dev.handeul.autto.view.LottoBallView;
import dev.handeul.autto.viewmodel.NumberListViewModel;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NumberListFragment extends Fragment {
    private FragmentNumberListBinding binding;
    private NumberListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(NumberListViewModel.class);
        binding = FragmentNumberListBinding.inflate(inflater, container, false);

        int ballSize = UnitUtils.calcDpForLayoutParams(getContext(), LottoBallView.DEFAULT_SIZE);
        int ballMargin = UnitUtils.calcDpForLayoutParams(getContext(), 8);
        int linePadding = UnitUtils.calcDpForLayoutParams(getContext(), 6);

        viewModel.buyList.observe(getViewLifecycleOwner(), games -> {
            if(games == null) {
                Activity parentActivity = getActivity();
                assert parentActivity != null;

                Intent intent = new Intent(parentActivity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                parentActivity.finish();
                return;
            }

            binding.numberListFragment.removeAllViews();
            if(games.size() == 0) {
                TextView noDataText = new TextView(getContext());
                noDataText.setText("구매 기록이 없습니다.");

                noDataText.setGravity(Gravity.CENTER);
                noDataText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                binding.numberListFragment.addView(noDataText);
            }

            for(Game game : games) {
                LinearLayout lGame = new LinearLayout(getContext());
                lGame.setPadding(0, linePadding, 0, linePadding);
                Integer[] numbers = game.getNumbers();

                lGame.setGravity(Gravity.CENTER);

                for (Integer number : numbers) {
                    LottoBallView vBall = new LottoBallView(getContext(), null);
                    LinearLayout.LayoutParams params
                            = new LinearLayout.LayoutParams(ballSize, ballSize);
                    params.setMargins(ballMargin, 0, ballMargin, 0);

                    vBall.setNumber(number);
                    vBall.setLayoutParams(params);

                    lGame.addView(vBall);
                }

                binding.numberListFragment.addView(lGame);
            }
        });

        return binding.getRoot();
    }

    public void update() {
        viewModel.update();
    }
}