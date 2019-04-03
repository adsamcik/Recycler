package com.adsamcik.cardlist

import android.content.Context
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

interface ViewHolderCreator<VH, D> where VH : RecyclerView.ViewHolder, D : Card {

	/**
	 *  Returns int [StyleRes] for theme that should be used
	 */
	//StyleRes cannot be added to val or var in interface
	@StyleRes
	fun getTheme(): Int

	/**
	 * Creates view inside [parent] and maps it to [RecyclerView.ViewHolder] object ([VH])
	 *
	 * @param parent Parent of the View
	 * @param viewType View type
	 * @return View holder
	 */
	fun createView(parent: CardView, viewType: Int): VH

	/**
	 * Updates view using [viewHolder] with data from [card]
	 *
	 * @param context Any context instance
	 * @param viewHolder View holder for instance that needs to be updated with data
	 * @param card Data that should be put into the View
	 */
	fun updateView(context: Context, viewHolder: VH, card: D)
}