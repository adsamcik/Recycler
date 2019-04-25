package com.adsamcik.recycler

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import java.text.DecimalFormat

internal object Util {
	internal fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

	/**
	 * Generate ripple drawable
	 *
	 * @param normalColor  if 0, background is transparent
	 * @param pressedColor pressed color
	 * @return RippleDrawable
	 */
	internal fun getPressedColorRippleDrawable(normalColor: Int, pressedColor: Int, mask: Drawable?): RippleDrawable {
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
	internal fun getAccentColor(context: Context): Int {
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
	internal fun formatNumber(number: Int): String {
		val df = DecimalFormat("#,###,###")
		return df.format(number.toLong())
	}
}

