package com.adsamcik.cardlist.card

import android.os.Build
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.cardlist.AppendPriority
import com.adsamcik.cardlist.ViewHolderCreator

open class CardListAdapter<VH, D>(private val creator: ViewHolderCreator<VH, D>) : RecyclerView.Adapter<VH>() where VH : RecyclerView.ViewHolder, D : Card {
	private val cardList: MutableList<D> = mutableListOf()

	/**
	 * Add tableCard to adapter
	 *
	 * @param element tableCard
	 */
	fun add(element: D) {
		cardList.add(element)
		notifyDataSetChanged()
	}

	/**
	 * Adds list of [D] to the adapter
	 *
	 * @param list List of items to add
	 */
	fun addAll(list: List<D>) {
		cardList.addAll(list)
		notifyDataSetChanged()
	}

	/**
	 * Remove all tableCards from adapter
	 */
	fun clear() {
		cardList.clear()
		notifyDataSetChanged()
	}

	/**
	 * Sorts tableCards based on their [AppendPriority].
	 */
	fun sort() {
		cardList.sortBy { it.appendPriority.ordinal }
		notifyDataSetChanged()
	}

	/**
	 * Removed all elements with specific [AppendPriority]
	 * @param appendPriority Append behaviour
	 */
	fun remove(appendPriority: AppendPriority) {
		if (Build.VERSION.SDK_INT >= 24)
			cardList.removeIf { table -> table.appendPriority == appendPriority }
		else {
			var i = 0
			while (i < cardList.size) {
				if (cardList[i].appendPriority == appendPriority)
					cardList.removeAt(i--)
				i++
			}
		}
		notifyDataSetChanged()
	}

	/**
	 * Removes specific element from adapter
	 * @param element Element to remove
	 */
	fun remove(element: D) {
		cardList.remove(element)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
		val cardView = CardView(parent.context, null, creator.getTheme())
		val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
		cardView.layoutParams = layoutParams

		return creator.createView(cardView, viewType)
	}

	override fun getItemCount() = cardList.size

	override fun onBindViewHolder(holder: VH, position: Int) {
		creator.updateView(holder.itemView.context, holder, cardList[position])
	}


	companion object {
		fun <VH, D> addTo(recyclerView: RecyclerView, creator: ViewHolderCreator<VH, D>) : CardListAdapter<VH, D> where VH : RecyclerView.ViewHolder, D : Card {
			val adapter = CardListAdapter(creator)
			recyclerView.adapter = adapter
			recyclerView.addItemDecoration(CardItemDecoration())
			return adapter
		}
	}
}
