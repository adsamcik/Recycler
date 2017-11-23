package com.adsamcik.table;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import java.text.DecimalFormat;

public class Util {
	/**
	 * Slightly more optimized function for conversion from density-independent pixels to pixels
	 *
	 * @param dm display metrics
	 * @param dp Density-independent Pixels
	 * @return pixels
	 */
	static int dpToPx(@NonNull DisplayMetrics dm, int dp) {
		return Math.round(dp * dm.density);
	}

	/**
	 * Function for conversion from dp to px
	 *
	 * @param c  context
	 * @param dp Density-independent Pixels
	 * @return pixels
	 */
	static int dpToPx(@NonNull Context c, int dp) {
		return Math.round(dp * c.getResources().getDisplayMetrics().density);
	}

	/**
	 * Generate ripple drawable
	 *
	 * @param normalColor  if 0, background is transparent
	 * @param pressedColor pressed color
	 * @return RippleDrawable
	 */
	static RippleDrawable getPressedColorRippleDrawable(int normalColor, int pressedColor, @Nullable Drawable mask) {
		return new RippleDrawable(getPressedColorSelector(pressedColor), normalColor == 0 ? null : getColorDrawableFromColor(normalColor), mask);
	}

	private static ColorStateList getPressedColorSelector(int pressedColor) {
		return new ColorStateList(new int[][]{new int[]{}}, new int[]{pressedColor}
		);
	}

	private static ColorDrawable getColorDrawableFromColor(int color) {
		return new ColorDrawable(color);
	}

	/**
	 * Formats 1000 as 1 000
	 *
	 * @param number input number
	 * @return formatted number
	 */
	static String formatNumber(int number) {
		DecimalFormat df = new DecimalFormat("#,###,###");
		return df.format(number);
	}
}

