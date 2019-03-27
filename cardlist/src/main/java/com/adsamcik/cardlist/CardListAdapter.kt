package com.adsamcik.cardlist

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.cardlist.Util.toPx
import java.util.ArrayList
import kotlin.Comparator

open class CardListAdapter<VH, D>(itemMarginDp: Int,
                                  @param:StyleRes @field:StyleRes private val themeInt: Int,
                                  private val creator: ViewHolderCreator<VH, D>) : RecyclerView.Adapter<VH>() where VH : RecyclerView.ViewHolder, D : Card {
	private val cardList: ArrayList<D> = ArrayList()

	private val itemMarginPx: Int = itemMarginDp.toPx()

	/**
	 * Add tableCard to adapter
	 *
	 * @param tableCard tableCard
	 */
	fun add(tableCard: D) {
		cardList.add(tableCard)
	}

	/**
	 * Remove all tableCards from adapter
	 */
	fun clear() {
		cardList.clear()
		notifyDataSetChanged()
	}

	/**
	 * Sorts tableCards based on their [AppendBehaviour].
	 */
	fun sort() {
		cardList.sortWith(Comparator { tx, ty -> tx.appendBehaviour.ordinal - ty.appendBehaviour.ordinal })
		notifyDataSetChanged()
	}

	/**
	 * Removed all elements with specific [AppendBehaviour]
	 * @param appendBehaviour append behavior
	 */
	fun remove(appendBehaviour: AppendBehaviour) {
		if (Build.VERSION.SDK_INT >= 24)
			cardList.removeIf { table -> table.appendBehaviour == appendBehaviour }
		else {
			var i = 0
			while (i < cardList.size) {
				if (cardList[i].appendBehaviour == appendBehaviour)
					cardList.removeAt(i--)
				i++
			}
		}
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
		val cardView = CardView(parent.context, null, themeInt)
		val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
		cardView.layoutParams = layoutParams

		return creator.createView(cardView, viewType)
	}

	override fun getItemCount() = cardList.size

	override fun onBindViewHolder(holder: VH, position: Int) {
		creator.updateView(holder.itemView.context, holder, cardList[position])
	}
}
