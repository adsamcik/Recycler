package com.adsamcik.cardlist

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.Pair
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView
import androidx.core.graphics.ColorUtils
import com.adsamcik.cardlist.Util.formatNumber
import com.adsamcik.cardlist.Util.getAccentColor
import com.adsamcik.cardlist.Util.getPressedColorRippleDrawable
import com.adsamcik.cardlist.Util.toPx
import java.util.*
import kotlin.collections.ArrayList

open class TableCard
/**
 * TableCard constructor
 *
 * @param rowCount   number of data (used to initialize array holding data)
 * @param showNumber show number of row (starts at 1)
 */
(private val showNumber: Boolean,
 private val wrapperMarginDp: Int,
 rowCount: Int = 4,
 @AppendBehaviors.AppendBehavior var appendBehavior: Int = AppendBehaviors.Any) {

	var title: String? = null

	private val data: ArrayList<Pair<String, String>> = ArrayList(rowCount)
	private var buttons: ArrayList<Pair<String, View.OnClickListener>> = ArrayList(0)

	private var dividerColor = 0

	private fun updateDividerColor(cardView: androidx.cardview.widget.CardView) {
		val color = cardView.cardBackgroundColor.defaultColor
		val lum = ColorUtils.calculateLuminance(color)

		dividerColor = if (lum > 0.5)
			Color.argb(30, 0, 0, 0)
		else
			Color.argb(30, 255, 255, 255)
	}

	/**
	 * Add button to the bottom of the table
	 *
	 * @param text     title of the button
	 * @param callback on click callback
	 * @return this table
	 */
	fun addButton(text: String, callback: View.OnClickListener): TableCard {
		buttons.add(Pair(text, callback))
		return this
	}

	/**
	 * Adds data to 2 columns on the last row, only use this with 2 columns (+1 if row numbering is enabled)
	 *
	 * @param name  row name
	 * @param value row value
	 * @return this table
	 */
	fun addData(name: String, value: String): TableCard {
		data.add(Pair(name, value))
		return this
	}

	private fun generateButtonsRow(context: Context, @StyleRes theme: Int, sideMargin: Int): TableRow? {
		if (buttons.isNotEmpty()) {
			val row = TableRow(context)
			val lp = TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
			lp.topMargin = 4.toPx()
			lp.setMargins(sideMargin, 0, sideMargin, 0)
			row.layoutParams = lp

			for (i in buttons.indices)
				row.addView(generateButton(context, i, theme))
			return row
		}
		return null
	}

	private fun generateButton(context: Context, index: Int, @StyleRes theme: Int): TextView {
		//value caching
		val dp48px = 48.toPx()
		val dp16px = 16.toPx()

		return Button(context, null, theme).apply {
			//dimensions
			minWidth = dp48px
			setPadding(dp16px, 0, dp16px, 0)
			height = dp48px

			//layout params
			val layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
			layoutParams.setMargins(0, dp16px, 0, 0)
			this.layoutParams = layoutParams

			//style
			text = buttons[index].first.toUpperCase()
			typeface = Typeface.defaultFromStyle(Typeface.BOLD)
			setOnClickListener(buttons[index].second)
			textSize = 16f
			gravity = Gravity.CENTER
			background = getPressedColorRippleDrawable(0, getAccentColor(context), context.getDrawable(R.drawable.rectangle))
		}
	}

	private fun generateDataRow(context: Context, index: Int, @StyleRes theme: Int): TableRow {
		val row = TableRow(context)

		if (showNumber) {
			val rowNum = TextView(context, null, theme)
			rowNum.text = String.format(Locale.UK, "%d", index + 1)
			rowNum.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f)
			row.addView(rowNum)
		}

		val textId = TextView(context, null, theme)
		textId.text = data[index].first
		textId.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f)
		row.addView(textId)

		val textValue = TextView(context, null, theme)
		val value = data[index].second
		try {
			textValue.text = formatNumber(Integer.parseInt(value))
		} catch (e: NumberFormatException) {
			textValue.text = value
		}

		textValue.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f)
		textValue.gravity = Gravity.END
		row.addView(textValue)

		return row
	}

	/**
	 * Generates view for the table
	 *
	 * @param context        context
	 * @param requireWrapper FrameView wrapper for margin
	 * @return card view with the new table
	 */
	fun getView(context: Context, recycle: View?, requireWrapper: Boolean, @StyleRes theme: Int): View {
		val r = context.resources
		var addWrapper = wrapperMarginDp != 0 || requireWrapper

		var cardView: CardView? = null
		var frameLayout: FrameLayout? = null

		/*if (recycle != null) {
			if (recycle is CardView) {
				cardView = recycle
			} else if (recycle is FrameLayout) {
				val viewTest = recycle.getChildAt(0)
				if (viewTest is CardView) {
					cardView = viewTest
					frameLayout = recycle
					//For some reason there can be view or views that have HW acceleration disabled
					//and without it shadows are not drawn. This solution recreates those views and fixes the issue
					addWrapper = if (!frameLayout.isHardwareAccelerated) {
						recycle.removeView(cardView)
						true
					} else
						false
				}
			}
		}*/

		if (cardView == null)
			cardView = CardView(context, null, theme)

		if (dividerColor == 0)
			updateDividerColor(cardView)

		val padding = r.getDimension(R.dimen.table_padding).toInt()
		val itemVerticalPadding = r.getDimension(R.dimen.table_item_vertical_padding).toInt()

		var layout = cardView.getChildAt(0) as TableLayout?
		if (layout == null) {
			layout = TableLayout(context)
			layout.setPadding(0, 30, 0, 30)
			cardView.addView(layout)
		} else
			layout.removeAllViews()

		if (title != null) {
			val titleView = layout.getChildAt(0) as TextView?
					?: TextView(context, null, theme).apply {
						textSize = 18f
						setTypeface(null, Typeface.BOLD)
						gravity = Gravity.CENTER
						setLines(1)
						ellipsize = TextUtils.TruncateAt.END

						val titleLayoutParams = TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
						titleLayoutParams.setMargins(padding, 0, padding, 16.toPx())
						layoutParams = titleLayoutParams

						layout.addView(this, 0)
					}

			titleView.text = title
		}

		if (data.size > 0) {
			var row = generateDataRow(context, 0, theme)
			row.setPadding(padding, itemVerticalPadding, padding, itemVerticalPadding)
			layout.addView(row)
			if (data.size > 1) {
				val layoutParams = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1.toPx())

				for (i in 1 until data.size) {
					val divider = View(context)
					divider.layoutParams = layoutParams
					divider.setBackgroundColor(dividerColor)
					layout.addView(divider)

					row = generateDataRow(context, i, theme)
					row.setPadding(padding, itemVerticalPadding, padding, itemVerticalPadding)
					layout.addView(row)
				}
			}
		}

		val buttonsRow = generateButtonsRow(context, theme, padding)
		if (buttonsRow != null)
			layout.addView(buttonsRow)

		if (addWrapper) {
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

