package dev.handeul.autto.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import dev.handeul.autto.R;
import dev.handeul.autto.utils.UnitUtils;

public class LottoBallView extends View {
    private static final String TAG = "LottoBallView";
    public static final int DEFAULT_SIZE = 34;

    private Integer number;
    private Paint ballPaint;
    private Paint textPaint;

    public LottoBallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LottoBall,
                0, 0);

        try {
            this.number = a.getInteger(R.styleable.LottoBall_number, 0);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(UnitUtils.calcDpForLayoutParams(getContext(), 12));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        ballPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ballPaint.setColor(getColorForNumber(number));
        ballPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getWidth() / 2.0f;
        float centerY = getHeight() / 2.0f;

        // Draw the pointer
        canvas.drawCircle(centerX, centerY, Math.min(centerX, centerY), ballPaint);

        // Draw the label text
        canvas.drawText(String.valueOf(number),
                centerX,
                (centerY - ((textPaint.descent() + textPaint.ascent()) / 2)),
                textPaint);
    }

    private int getColorForNumber(int number) {
        if(number <= 0 || number > 45) {
            return Color.parseColor("#aaaaaa");
        }

        if(number <= 10) {
            return Color.parseColor("#ffb600");
        } else if(number <= 20) {
            return Color.parseColor("#1b92f2");
        } else if(number <= 30) {
            return Color.parseColor("#ff4435");
        } else if(number <= 40) {
            return Color.parseColor("#222222");
        } else {
            return Color.parseColor("#1bb400");
        }
    }

    public void setNumber(Integer number) {
        this.number = number;
        ballPaint.setColor(getColorForNumber(number));
        invalidate();
        requestLayout();
    }

}
