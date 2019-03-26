package com.adsamcik.cardlist

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.annotation.StyleRes
import androidx.core.graphics.ColorUtils
import androidx.cardview.widget.CardView
import android.text.TextUtils
import android.util.Pair
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.adsamcik.cardlist.Util.formatNumber
import com.adsamcik.cardlist.Util.getAccentColor
import com.adsamcik.cardlist.Util.getPressedColorRippleDrawable
import com.adsamcik.cardlist.Util.toPx
import java.util.*

open class Table
/**
 * Table constructor
 *
 * @param rowCount   number of data (used to initialize array holding data)
 * @param showNumber show number of row (starts at 1)
 */
(rowCount: Int,
 private val showNumber: Boolean,
 private val wrapperMarginDp: Int,
 @AppendBehaviors.AppendBehavior var appendBehavior: Int) {

    var title: String? = null

    private val data: ArrayList<Pair<String, String>> = ArrayList(rowCount)
    private var buttons: ArrayList<Pair<String, View.OnClickListener>>? = null

    private var dividerColor = 0

    private fun updateDividerColor(cardView: androidx.cardview.widget.CardView) {
        val color = cardView.cardBackgroundColor.defaultColor
        val lum = ColorUtils.calculateLuminance(color)

        dividerColor = if (lum > 0.5)
            Color.argb(30, 0, 0, 0)
        else
            Color.argb(30, 255, 255, 255)
    }

    /*public void addToViewGroup(@NonNull ViewGroup viewGroup, @NonNull Context context, int index, boolean animate, long delay) {
		if (index >= 0 && index < viewGroup.getChildCount())
			viewGroup.addView(view, index);
		else
			viewGroup.addView(view);

		if (animate) {
			view.setTranslationY(viewGroup.getHeight());
			view.setAlpha(0);
			view.animate()
					.translationY(0)
					.setInterpolator(new DecelerateInterpolator(3.f))
					.setDuration(700)
					.setStartDelay(delay)
					.alpha(1)
					.start();
		}
	}*/

    /**
     * Add button to the bottom of the table
     *
     * @param text     title of the button
     * @param callback on click callback
     * @return this table
     */
    fun addButton(text: String, callback: View.OnClickListener): Table {
        if (buttons == null)
            buttons = ArrayList(2)
        buttons!!.add(Pair(text, callback))
        return this
    }

    /**
     * Adds data to 2 columns on the last row, only use this with 2 columns (+1 if row numbering is enabled)
     *
     * @param name  row name
     * @param value row value
     * @return this table
     */
    fun addData(name: String, value: String): Table {
        data.add(Pair(name, value))
        return this
    }

    private fun generateButtonsRow(context: Context, @StyleRes theme: Int, sideMargin: Int): TableRow? {
        if (buttons != null) {
            val row = TableRow(context)
            val lp = TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.topMargin = 4.toPx()
            lp.setMargins(sideMargin, 0, sideMargin, 0)
            row.layoutParams = lp

            for (i in buttons!!.indices)
                row.addView(generateButton(context, i, theme))
            return row
        }
        return null
    }

    private fun generateButton(context: Context, index: Int, @StyleRes theme: Int): TextView {
        val button = Button(context, null, theme)

        //value caching
        val dp48px = 48.toPx()
        val dp16px = 16.toPx()

        //dimensions
        button.minWidth = dp48px
        button.setPadding(dp16px, 0, dp16px, 0)
        button.height = dp48px

        //layout params
        val layoutParams = TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, dp16px, 0, 0)
        button.layoutParams = layoutParams

        //style
        button.text = buttons!![index].first.toUpperCase()
        button.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        button.setOnClickListener(buttons!![index].second)
        button.textSize = 16f
        button.gravity = Gravity.CENTER
        button.background = getPressedColorRippleDrawable(0, getAccentColor(context), context.getDrawable(R.drawable.rectangle))


        return button
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
                    addWrapper = if (!frameLayout.isHardwareAccelerated) {
                        recycle.removeView(cardView)
                        true
                    } else
                        false
                }
            }
        }

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
            var titleView = layout.getChildAt(0) as TextView?
            if (titleView == null) {
                titleView = TextView(context, null, theme)
                titleView.textSize = 18f
                titleView.setTypeface(null, Typeface.BOLD)
                titleView.gravity = Gravity.CENTER
                titleView.setLines(1)
                titleView.ellipsize = TextUtils.TruncateAt.END

                val titleLayoutParams = TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                titleLayoutParams.setMargins(padding, 0, padding, 16.toPx())
                titleView.layoutParams = titleLayoutParams

                layout.addView(titleView, 0)
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

