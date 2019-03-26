package com.adsamcik.cardlist

import android.content.Context
import android.os.Build
import androidx.annotation.StyleRes
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import com.adsamcik.cardlist.Util.toPx
import java.util.ArrayList
import kotlin.Any
import kotlin.Comparator
import kotlin.Int
import kotlin.Long

open class CardListAdapter(context: Context, itemMarginDp: Int, @param:StyleRes @field:StyleRes
private val themeInt: Int) : BaseAdapter() {
    private val tableCards: ArrayList<TableCard> = ArrayList()
    private val context: Context = context.applicationContext

    private val itemMarginPx: Int = itemMarginDp.toPx()

    /**
     * Add tableCard to adapter
     *
     * @param tableCard tableCard
     */
    fun add(tableCard: TableCard) {
        tableCards.add(tableCard)
    }

    /**
     * Remove all tableCards from adapter
     */
    fun clear() {
        tableCards.clear()
        notifyDataSetChanged()
    }

    /**
     * Sorts tableCards based on their [AppendBehaviors.AppendBehavior].
     */
    fun sort() {
        tableCards.sortWith(Comparator { tx, ty -> tx.appendBehavior - ty.appendBehavior })
        notifyDataSetChanged()
    }

    /**
     * Removed all elements with specific [AppendBehaviors.AppendBehavior]
     * @param appendBehavior append behavior
     */
    fun remove(@AppendBehaviors.AppendBehavior appendBehavior: Int) {
        if (Build.VERSION.SDK_INT >= 24)
            tableCards.removeIf { table -> table.appendBehavior == appendBehavior }
        else {
            var i = 0
            while (i < tableCards.size) {
                if (tableCards[i].appendBehavior == appendBehavior)
                    tableCards.removeAt(i--)
                i++
            }
        }
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return tableCards.size
    }

    override fun getItem(i: Int): Any {
        return tableCards[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        val v = tableCards[i].getView(context, view, true, themeInt) as ViewGroup

        val lp = v.getChildAt(0).layoutParams as FrameLayout.LayoutParams
        lp.setMargins(lp.leftMargin, if (i > 0) itemMarginPx / 2 else itemMarginPx, lp.rightMargin, if (i < count - 1) itemMarginPx / 2 else itemMarginPx)
        v.layoutParams = lp
        return v
    }
}
