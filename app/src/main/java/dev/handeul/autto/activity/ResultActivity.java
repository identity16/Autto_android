package dev.handeul.autto.activity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import dev.handeul.autto.R;
import dev.handeul.autto.databinding.ActivityResultBinding;
import dev.handeul.autto.model.Game;
import dev.handeul.autto.model.Round;
import dev.handeul.autto.utils.UnitUtils;
import dev.handeul.autto.view.LottoBallView;
import dev.handeul.autto.viewmodel.ResultViewModel;

public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";

    private ActivityResultBinding binding;
    private ResultViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ResultViewModel();
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.btnBack2.setOnClickListener((v) -> onBackPressed());

        viewModel.lastRound.observe(this, this::setResultView);
        viewModel.buyList.observe(this, this::setBuyListView);

        setContentView(view);
    }

    private void setResultView(Round round) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("(yyyy. MM. dd) 추첨");
        List<Integer> numbers = round.getNumbers();

        binding.txtDrwNum.setText(String.valueOf(round.getId()));
        binding.txtDrwDate.setText(df.format(round.getDate()));

        if(numbers.size() == 6) {
            binding.lottoBallView1.setNumber(numbers.get(0));
            binding.lottoBallView2.setNumber(numbers.get(1));
            binding.lottoBallView3.setNumber(numbers.get(2));
            binding.lottoBallView4.setNumber(numbers.get(3));
            binding.lottoBallView5.setNumber(numbers.get(4));
            binding.lottoBallView6.setNumber(numbers.get(5));
        }

        binding.bonusBallView.setNumber(round.getBonusNum());
    }

    private void setBuyListView(List<Game> buyList) {
        if(buyList == null || buyList.isEmpty()) return;

        int ballSize = UnitUtils.calcDpForLayoutParams(this, LottoBallView.DEFAULT_SIZE);
        int ballMargin = UnitUtils.calcDpForLayoutParams(this, 8);
        int cardElevation = UnitUtils.calcDpForLayoutParams(this, 4);
        int cardPadding = UnitUtils.calcDpForLayoutParams(this, 16);
        int cardMargin = UnitUtils.calcDpForLayoutParams(this, 8);

        binding.buyListLayout.removeAllViews();

        for(Game game : buyList) {
            FrameLayout card = new FrameLayout(this);
            LinearLayout lGame = new LinearLayout(this);
            Integer[] numbers = game.getNumbers();

            LinearLayout.LayoutParams cardParams =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(0, 0, 0, cardMargin);
            card.setLayoutParams(cardParams);
            card.setBackgroundResource(R.drawable.rounded);
            card.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            card.setElevation(cardElevation);

            lGame.setPadding(cardPadding, cardPadding, cardPadding, cardPadding);
            lGame.setGravity(Gravity.CENTER);

            for (Integer number : numbers) {
                LottoBallView vBall = new LottoBallView(this, null);
                LinearLayout.LayoutParams params
                        = new LinearLayout.LayoutParams(ballSize, ballSize);
                params.setMargins(ballMargin, 0, ballMargin, 0);

                vBall.setNumber(number);
                vBall.setLayoutParams(params);

                lGame.addView(vBall);
            }

            card.addView(lGame);

            TextView txtState = new TextView(this);
            txtState.setText(game.getState());
            FrameLayout.LayoutParams stateParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            txtState.setLayoutParams(stateParams);
            txtState.setTextColor(Color.WHITE);
            txtState.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            txtState.setBackgroundResource(R.drawable.rounded);
            txtState.setBackgroundTintList(ColorStateList.valueOf(
                    Color.parseColor(getBackgroundColorByState(game.getState()))));
            txtState.setGravity(Gravity.CENTER);
            txtState.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            txtState.setTypeface(Typeface.DEFAULT_BOLD);

            card.addView(txtState);

            binding.buyListLayout.addView(card);
        }
    }

    private String getBackgroundColorByState(String state) {
        switch (state) {
            case Game.State.FIRST:
            case Game.State.SECOND:
            case Game.State.THIRD:
            case Game.State.FOURTH:
            case Game.State.FIFTH:
                return "#99f33d5d";
            case Game.State.FAIL:
            default:
                return "#66000000";

        }
    }
}