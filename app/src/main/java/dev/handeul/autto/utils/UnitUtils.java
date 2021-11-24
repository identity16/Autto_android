package dev.handeul.autto.utils;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.Nullable;

public class UnitUtils {
    private UnitUtils() {}

    public static int calcDpForLayoutParams(@Nullable Context context, int dpValue) {
        if(context == null) return -1;
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics());
    }
}
