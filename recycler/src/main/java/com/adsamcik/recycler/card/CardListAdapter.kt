package com.adsamcik.recycler.card

import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.recycler.SortableAdapter
import com.adsamcik.recycler.ViewHolderCreator

open class CardListAdapter<VH, D>(private val creator: ViewHolderCreator<VH, D>) : SortableAdapter<D, VH>() where VH : RecyclerView.ViewHolder, D : Card {

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
		fun <VH, D> addTo(recyclerView: RecyclerView, creator: ViewHolderCreator<VH, D>): CardListAdapter<VH, D> where VH : RecyclerView.ViewHolder, D : Card {
			val adapter = CardListAdapter(creator)
			recyclerView.adapter = adapter
			recyclerView.addItemDecoration(CardItemDecoration())
			return adapter
		}
	}
}
