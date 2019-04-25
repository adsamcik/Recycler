package com.adsamcik.recycler.card.table

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.Pair
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView
import androidx.core.graphics.ColorUtils
import com.adsamcik.recycler.R
import com.adsamcik.recycler.Util
import com.adsamcik.recycler.Util.toPx
import com.adsamcik.recycler.ViewHolderCreator
import java.util.*

/**
 * Implementation of [ViewHolderCreator] for use with [TableCard]
 */
class TableCardCreator(@StyleRes private val theme: Int) : ViewHolderCreator<TableCard.ViewHolder, TableCard> {
	override fun getTheme() = theme

	private var dividerColor = 0

	override fun updateView(context: Context, viewHolder: TableCard.ViewHolder, card: TableCard) {
		val resources = context.resources
		val padding = resources.getDimension(R.dimen.table_padding).toInt()

		viewHolder.layout.removeAllViews()

		card.title?.let { generateTitleView(context, viewHolder.layout, it) }

		generateDataRows(context, viewHolder.layout, padding, card)

		generateButtonsRow(card.buttons, context, theme, padding)?.also { viewHolder.layout.addView(it) }
	}

	private fun generateDataRows(context: Context, rootLayout: ViewGroup, padding: Int, card: TableCard) {
		if (card.data.isEmpty()) return

		val layoutParams = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1.toPx())
		val itemVerticalPadding = context.resources.getDimension(R.dimen.table_item_vertical_padding).toInt()

		for (i in 0 until card.data.size) {
			if (i > 0) {
				val divider = View(context)
				divider.layoutParams = layoutParams
				divider.setBackgroundColor(dividerColor)
				rootLayout.addView(divider)
			}

			val rowLayout = generateDataRow(context, card.showRowNumber, card.data[i], i, theme)
			rowLayout.setPadding(padding, itemVerticalPadding, padding, itemVerticalPadding)
			rootLayout.addView(rowLayout)
		}
	}

	private fun generateTitleView(context: Context, layout: TableLayout, title: String) {
		val titleView = TextView(context, null, theme).apply {
			textSize = 18f
			setTypeface(null, Typeface.BOLD)
			gravity = Gravity.CENTER
			setLines(1)
			ellipsize = TextUtils.TruncateAt.END

			val padding = context.resources.getDimension(R.dimen.table_padding).toInt()

			val titleLayoutParams = TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
			titleLayoutParams.setMargins(padding, 0, padding, 16.toPx())
			layoutParams = titleLayoutParams

			text = title
		}

		layout.addView(titleView, 0)
	}

	private fun updateDividerColor(cardView: CardView) {
		val color = cardView.cardBackgroundColor.defaultColor
		val lum = ColorUtils.calculateLuminance(color)

		dividerColor = if (lum > 0.5) {
			Color.argb(30, 0, 0, 0)
		} else {
			Color.argb(30, 255, 255, 255)
		}
	}

	private fun generateButton(context: Context, button: Pair<String, View.OnClickListener>, @StyleRes theme: Int): TextView {
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
			text = button.first.toUpperCase()
			typeface = Typeface.defaultFromStyle(Typeface.BOLD)
			setOnClickListener(button.second)
			textSize = 16f
			gravity = Gravity.CENTER
			background = Util.getPressedColorRippleDrawable(0, Util.getAccentColor(context), context.getDrawable(R.drawable.rectangle))
		}
	}

	private fun generateDataRow(context: Context, showNumber: Boolean, rowData: Pair<String, String>, index: Int, @StyleRes theme: Int): TableRow {
		val row = TableRow(context)

		if (showNumber) {
			val rowNum = TextView(context, null, theme)
			rowNum.text = String.format(Locale.UK, "%d", index + 1)
			rowNum.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f)
			row.addView(rowNum)
		}

		val textId = TextView(context, null, theme)
		textId.text = rowData.first
		textId.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f)
		row.addView(textId)

		val textValue = TextView(context, null, theme)
		val value = rowData.second
		try {
			textValue.text = Util.formatNumber(Integer.parseInt(value))
		} catch (e: NumberFormatException) {
			textValue.text = value
		}

		textValue.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f)
		textValue.gravity = Gravity.END
		row.addView(textValue)

		return row
	}

	private fun generateButtonsRow(buttons: List<Pair<String, View.OnClickListener>>, context: Context, @StyleRes theme: Int, sideMargin: Int): TableRow? {
		if (buttons.isNotEmpty()) {
			val row = TableRow(context)
			val lp = TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
			lp.topMargin = 4.toPx()
			lp.setMargins(sideMargin, 0, sideMargin, 0)
			row.layoutParams = lp

			for (button in buttons)
				row.addView(generateButton(context, button, theme))
			return row
		}
		return null
	}

	override fun createView(parent: CardView, viewType: Int): TableCard.ViewHolder {
		if (dividerColor == 0) updateDividerColor(parent)

		val context = parent.context

		val layout = TableLayout(context).apply {
			setPadding(0, 30, 0, 30)
			parent.addView(this)
		}

		return TableCard.ViewHolder(parent, layout)
	}

}