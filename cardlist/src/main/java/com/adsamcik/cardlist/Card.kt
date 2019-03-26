package com.adsamcik.cardlist

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView
import androidx.core.graphics.ColorUtils
import com.adsamcik.cardlist.Util.toPx

abstract class Card<VH> {
	/**
	 * Append behaviour helps the sort function with ordering the cards in ListView
	 */
	abstract val appendBehaviour: AppendBehaviour

	private var dividerColor = 0

	private fun updateDividerColor(cardView: androidx.cardview.widget.CardView) {
		val color = cardView.cardBackgroundColor.defaultColor
		val lum = ColorUtils.calculateLuminance(color)

		dividerColor = if (lum > 0.5)
			Color.argb(30, 0, 0, 0)
		else
			Color.argb(30, 255, 255, 255)
	}

	abstract fun updateView()

	/**
	 * Returns view
	 */
	open fun createView(context: Context, recycle: View?, requireWrapper: Boolean, @StyleRes theme: Int, wrapperMarginDp: Int): View {
		val addWrapper = wrapperMarginDp != 0 || requireWrapper

		var cardView: CardView? = null
		var frameLayout: FrameLayout? = null

		if (recycle != null) {
			if (recycle is CardView) {
				cardView = recycle
			} else if (recycle is FrameLayout) {
				val viewTest = recycle.getChildAt(0)
				if (viewTest is CardView) {
					cardView = viewTest
					frameLayout = recycle
					//For some reason there can be view or views that have HW acceleration disabled
					//and without it shadows are not drawn. This solution recreates those views and fixes the issue
					/*addWrapper = if (!frameLayout.isHardwareAccelerated) {
						recycle.removeView(cardView)
						true
					} else
						false*/
				}
			}
		}

		if (cardView == null) {
			cardView = CardView(context, null, theme)
			/*val layoutParams = cardView.layoutParams ?: ViewGroup.LayoutParams()
			cardView.setm(16.toPx(), 16.toPx(), 16.toPx(), 16.toPx())*/
		}

		if (dividerColor == 0)
			updateDividerColor(cardView)

		if (addWrapper && false) {
			frameLayout = FrameLayout(context)

			if (wrapperMarginDp != 0) {
				val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
				val margin = wrapperMarginDp.toPx()
				layoutParams.setMargins(margin, margin, margin, margin)
				cardView.layoutParams = layoutParams
			}
			frameLayout.addView(cardView)
		}

		return frameLayout ?: cardView
	}
}