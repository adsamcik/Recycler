package com.adsamcik.cardlist

import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.cardlist.Util.toPx
import java.util.ArrayList
import kotlin.Comparator

open class CardListAdapter<VH>(context: Context, itemMarginDp: Int, @param:StyleRes @field:StyleRes
private val themeInt: Int) : RecyclerView.Adapter<VH>() where VH : RecyclerView.ViewHolder {
	private val tableCards: ArrayList<Card<VH>> = ArrayList()
	private val context: Context = context.applicationContext

	private val itemMarginPx: Int = itemMarginDp.toPx()

	/**
	 * Add tableCard to adapter
	 *
	 * @param tableCard tableCard
	 */
	fun add(tableCard: Card) {
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
	 * Sorts tableCards based on their [AppendBehaviour].
	 */
	fun sort() {
		tableCards.sortWith(Comparator { tx, ty -> tx.appendBehaviour.ordinal - ty.appendBehaviour.ordinal })
		notifyDataSetChanged()
	}

	/**
	 * Removed all elements with specific [AppendBehaviour]
	 * @param appendBehaviour append behavior
	 */
	fun remove(appendBehaviour: AppendBehaviour) {
		if (Build.VERSION.SDK_INT >= 24)
			tableCards.removeIf { table -> table.appendBehaviour == appendBehaviour }
		else {
			var i = 0
			while (i < tableCards.size) {
				if (tableCards[i].appendBehaviour == appendBehaviour)
					tableCards.removeAt(i--)
				i++
			}
		}
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
		val v = tableCards[i].createView(context, view, true, themeInt, 16) as ViewGroup

		val lp = v.getChildAt(0).layoutParams as FrameLayout.LayoutParams
		lp.setMargins(lp.leftMargin, if (i > 0) itemMarginPx / 2 else itemMarginPx, lp.rightMargin, if (i < count - 1) itemMarginPx / 2 else itemMarginPx)
		v.layoutParams = lp
	}

	override fun getItemCount(): Int {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onBindViewHolder(holder: VH, position: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

		return v
	}
}
