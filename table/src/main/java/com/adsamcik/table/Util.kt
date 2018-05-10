package com.adsamcik.table

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.support.annotation.ColorInt
import android.util.DisplayMetrics
import android.util.TypedValue
import java.text.DecimalFormat

internal object Util {
    /**
     * Slightly more optimized function for conversion from density-independent pixels to pixels
     *
     * @param dm display metrics
     * @param dp Density-independent Pixels
     * @return pixels
     */
    fun dpToPx(dm: DisplayMetrics, dp: Int): Int {
        return Math.round(dp * dm.density)
    }

    /**
     * Function for conversion from dp to px
     *
     * @param c  context
     * @param dp Density-independent Pixels
     * @return pixels
     */
    fun dpToPx(c: Context, dp: Int): Int {
        return dpToPx(getDisplayMetrics(c), dp)
    }

    fun getDisplayMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }

    /**
     * Generate ripple drawable
     *
     * @param normalColor  if 0, background is transparent
     * @param pressedColor pressed color
     * @return RippleDrawable
     */
    fun getPressedColorRippleDrawable(normalColor: Int, pressedColor: Int, mask: Drawable?): RippleDrawable {
        return RippleDrawable(getPressedColorSelector(pressedColor), if (normalColor == 0) null else getColorDrawableFromColor(normalColor), mask)
    }

    private fun getPressedColorSelector(pressedColor: Int): ColorStateList {
        return ColorStateList(arrayOf(intArrayOf()), intArrayOf(pressedColor)
        )
    }

    private fun getColorDrawableFromColor(color: Int): ColorDrawable {
        return ColorDrawable(color)
    }

    @ColorInt
    fun getAccentColor(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
        return typedValue.data
    }

    /**
     * Formats 1000 as 1 000
     *
     * @param number input number
     * @return formatted number
     */
    fun formatNumber(number: Int): String {
        val df = DecimalFormat("#,###,###")
        return df.format(number.toLong())
    }
}

