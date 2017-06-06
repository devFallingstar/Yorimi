package com.fallingstar.yorimi;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

/**
 * Created by Jiran on 2017-06-06.
 */

public class ViewUtil {
    public static int TEXT_SIZE  = -1;
    public static int TEXT_SIZE_BIG = -1;

    public static Drawable drawable(Context context, int id) {
        if (TEXT_SIZE == -1) {
            TEXT_SIZE = (int) new TextView(context).getTextSize();
            TEXT_SIZE_BIG = (int) (TEXT_SIZE * 1.5);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return context.getResources().getDrawable(id, context.getTheme());
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    public static CharSequence iconText(Drawable icon, String text) {
        SpannableString iconText = new SpannableString(" "+text);
        icon.setBounds(0, 0, TEXT_SIZE_BIG, TEXT_SIZE_BIG);
        ImageSpan imageSpan = new ImageSpan(icon);

        iconText.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return iconText;
    }
}