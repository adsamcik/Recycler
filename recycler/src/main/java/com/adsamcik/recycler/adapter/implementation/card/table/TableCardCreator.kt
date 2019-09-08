package com.adsamcik.recycler.adapter.implementation.card.table

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.Pair
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.ColorUtils
import com.adsamcik.recycler.R
import com.adsamcik.recycler.Util
import com.adsamcik.recycler.Util.dp
import com.adsamcik.recycler.adapter.implementation.sort.ViewHolderCreator
import java.util.*

/**
 * Implementation of [ViewHolderCreator] for use with [TableCard].
 *
 * @param theme Style resources of theme that should be used
 */
class TableCardCreator(@StyleRes private val theme: Int) : ViewHolderCreator<TableCard.ViewHolder, TableCard> {
	override fun getTheme() = theme

	private var dividerColor = 0

	override fun updateView(context: Context, viewHolder: TableCard.ViewHolder, data: TableCard) {
		viewHolder.layout.removeAllViews()

		data.title?.let { generateTitleView(context, viewHolder.layout, it) }

		generateDataRows(context, viewHolder.layout, DEFAULT_PADDING, data)

		generateButtonsRow(data.buttons, context, theme, DEFAULT_PADDING)?.also { viewHolder.layout.addView(it) }
	}

	private fun generateDataRows(context: Context, rootLayout: ViewGroup, padding: Int, card: TableCard) {
		if (card.data.isEmpty()) return

		val layoutParams = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1.dp)
		val itemVerticalPadding = context.resources.getDimension(R.dimen.table_item_vertical_padding).toInt()

		for (i in card.data.indices) {
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
		val titleView = AppCompatTextView(context, null, theme).apply {
			textSize = TITLE_TEXT_SIZE
			setTypeface(null, Typeface.BOLD)
			gravity = Gravity.CENTER
			setLines(1)
			ellipsize = TextUtils.TruncateAt.END

			val titleLayoutParams = TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
			titleLayoutParams.setMargins(DEFAULT_PADDING, 0, DEFAULT_PADDING, DEFAULT_PADDING)
			layoutParams = titleLayoutParams

			text = title
		}

		layout.addView(titleView, 0)
	}

	private fun updateDividerColor(cardView: CardView) {
		val color = cardView.cardBackgroundColor.defaultColor
		val lum = ColorUtils.calculateLuminance(color)

		dividerColor = if (lum > HALF_LUMINANCE) {
			ColorUtils.setAlphaComponent(Color.BLACK, TEXT_ALPHA)
		} else {
			ColorUtils.setAlphaComponent(Color.WHITE, TEXT_ALPHA)
		}
	}

	private fun generateButton(context: Context, button: Pair<String, View.OnClickListener>, @StyleRes theme: Int): TextView {
		return AppCompatButton(context, null, theme).apply {
			//dimensions
			minWidth = MIN_BUTTON_WIDTH
			setPadding(DEFAULT_PADDING, 0, DEFAULT_PADDING, 0)
			height = DEFAULT_HEIGHT

			//layout params
			val layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
			layoutParams.setMargins(0, DEFAULT_PADDING, 0, 0)
			this.layoutParams = layoutParams

			//style
			@SuppressLint("DefaultLocale")
			text = button.first.toUpperCase()
			typeface = Typeface.defaultFromStyle(Typeface.BOLD)
			setOnClickListener(button.second)
			textSize = BUTTON_TEXT_SIZE
			gravity = Gravity.CENTER
			background = Util.getPressedColorRippleDrawable(0, Util.getAccentColor(context), context.getDrawable(R.drawable.rectangle))
		}
	}

	private fun generateDataRow(context: Context, showNumber: Boolean, rowData: Pair<String, String>, index: Int, @StyleRes theme: Int): TableRow {
		val row = TableRow(context)

		if (showNumber) {
			val rowNum = AppCompatTextView(context, null, theme)
			rowNum.text = String.format(Locale.UK, "%d", index + 1)
			rowNum.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f)
			row.addView(rowNum)
		}

		val textId = AppCompatTextView(context, null, theme)
		textId.text = rowData.first
		textId.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f)
		row.addView(textId)

		val textValue = AppCompatTextView(context, null, theme)
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
			lp.topMargin = BUTTON_TOP_MARGIN
			lp.setMargins(sideMargin, 0, sideMargin, 0)
			row.layoutParams = lp

			for (button in buttons) {
				row.addView(generateButton(context, button, theme))
			}

			return row
		}
		return null
	}

	override fun createView(parent: CardView, viewType: Int): TableCard.ViewHolder {
		if (dividerColor == 0) updateDividerColor(parent)

		val context = parent.context

		val layout = TableLayout(context).apply {
			setPadding(0, TABLE_VERTICAL_PADDING, 0, TABLE_VERTICAL_PADDING)
			parent.addView(this)
		}

		return TableCard.ViewHolder(parent, layout)
	}

	companion object {
		const val TEXT_ALPHA = 30
		val DEFAULT_PADDING = 16.dp
		val MIN_BUTTON_WIDTH = 48.dp
		val DEFAULT_HEIGHT = 48.dp
		val TABLE_VERTICAL_PADDING = 30.dp
		val BUTTON_TOP_MARGIN = 4.dp
		const val TITLE_TEXT_SIZE = 18f
		const val BUTTON_TEXT_SIZE = 16f
		const val HALF_LUMINANCE = 0.5f
	}
}
