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

public class LottoBallView extends View {
    private static final String TAG = "LottoBallView";

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
        textPaint.setTextSize(convertDpToPixel(12));
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
            return Color.parseColor("#333");
        }

        if(number <= 10) {
            return Color.parseColor("#fbc400");
        } else if(number <= 20) {
            return Color.parseColor("#69c8f2");
        } else if(number <= 30) {
            return Color.parseColor("#ff7272");
        } else if(number <= 40) {
            return Color.parseColor("#aaaaaa");
        } else {
            return Color.parseColor("#b0d840");
        }
    }

    /* Getter, Setter */
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
        invalidate();
        requestLayout();
    }

    private float convertDpToPixel(float dp){
        Resources resources = this.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
