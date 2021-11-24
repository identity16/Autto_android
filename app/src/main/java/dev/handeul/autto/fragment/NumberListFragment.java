package dev.handeul.autto.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        NumberListViewModel viewModel = new ViewModelProvider(this).get(NumberListViewModel.class);
        binding = FragmentNumberListBinding.inflate(inflater, container, false);

        int ballSize = UnitUtils.calcDpForLayoutParams(getContext(), LottoBallView.DEFAULT_SIZE);
        int ballMargin = UnitUtils.calcDpForLayoutParams(getContext(), 8);

        viewModel.buyList.observe(getViewLifecycleOwner(), games -> {
            for(Game game : games) {
                LinearLayout lGame = new LinearLayout(getContext());
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
}