package com.adsamcik.recycler.adapter.implementation.card

import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.recycler.adapter.implementation.sort.SortableAdapter
import com.adsamcik.recycler.adapter.implementation.sort.ViewHolderCreator
import com.adsamcik.recycler.decoration.MarginDecoration

/**
 * @param creator View holder creator
 * @param VH ViewHolder that implements [RecyclerView.ViewHolder]
 * @param D
 */
open class CardListAdapter<VH : RecyclerView.ViewHolder, D>(private val creator: ViewHolderCreator<VH, D>) : SortableAdapter<D, VH>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
		val cardView = CardView(parent.context, null, creator.getTheme())
		val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
		cardView.layoutParams = layoutParams

		return creator.createView(cardView, viewType)
	}

	override fun onBindViewHolder(holder: VH, position: Int) {
		val item = getItem(position)
		creator.updateView(holder.itemView.context, holder, item)
	}


	companion object {

		/**
		 * Adds [CardListAdapter] with [viewHolderCreator] to the [RecyclerView]. Automatically also adds [MarginDecoration].
		 *
		 * @param recyclerView Recycler view to which [CardListAdapter] should be added
		 * @param viewHolderCreator [ViewHolderCreator] used for creation of [ViewHolder] inside new [CardListAdapter]
		 *
		 * @return Newly created adapter, that was added to the [recyclerView]
		 */
		fun <ViewHolder : RecyclerView.ViewHolder, Data> addTo(recyclerView: RecyclerView, viewHolderCreator: ViewHolderCreator<ViewHolder, Data>): CardListAdapter<ViewHolder, Data> {
			val adapter = CardListAdapter(viewHolderCreator)
			recyclerView.adapter = adapter
			recyclerView.addItemDecoration(MarginDecoration())
			return adapter
		}
	}
}
