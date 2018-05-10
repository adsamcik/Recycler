package com.adsamcik.table

import android.content.Context
import android.os.Build
import android.support.annotation.StyleRes
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import com.adsamcik.table.Util.toPx
import java.util.ArrayList
import kotlin.Any
import kotlin.Comparator
import kotlin.Int
import kotlin.Long

open class TableAdapter(context: Context, itemMarginDp: Int, @param:StyleRes @field:StyleRes
private val themeInt: Int) : BaseAdapter() {
    private val tables: ArrayList<Table> = ArrayList()
    private val context: Context = context.applicationContext

    private val itemMarginPx: Int = itemMarginDp.toPx()

    /**
     * Add table to adapter
     *
     * @param table table
     */
    fun add(table: Table) {
        tables.add(table)
    }

    /**
     * Remove all tables from adapter
     */
    fun clear() {
        tables.clear()
        notifyDataSetChanged()
    }

    /**
     * Sorts tables based on their [AppendBehaviors.AppendBehavior].
     */
    fun sort() {
        tables.sortWith(Comparator { tx, ty -> tx.appendBehavior - ty.appendBehavior })
        notifyDataSetChanged()
    }

    /**
     * Removed all elements with specific [AppendBehaviors.AppendBehavior]
     * @param appendBehavior append behavior
     */
    fun remove(@AppendBehaviors.AppendBehavior appendBehavior: Int) {
        if (Build.VERSION.SDK_INT >= 24)
            tables.removeIf { table -> table.appendBehavior == appendBehavior }
        else {
            var i = 0
            while (i < tables.size) {
                if (tables[i].appendBehavior == appendBehavior)
                    tables.removeAt(i--)
                i++
            }
        }
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return tables.size
    }

    override fun getItem(i: Int): Any {
        return tables[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        val v = tables[i].getView(context, view, true, themeInt) as ViewGroup

        val lp = v.getChildAt(0).layoutParams as FrameLayout.LayoutParams
        lp.setMargins(lp.leftMargin, if (i > 0) itemMarginPx / 2 else itemMarginPx, lp.rightMargin, if (i < count - 1) itemMarginPx / 2 else itemMarginPx)
        v.layoutParams = lp
        return v
    }
}
