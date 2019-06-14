package com.adsamcik.recycler

import android.content.Context
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

/**
 * Base class for creation of ViewHolders. Used by [SortableAdapter].
 * @param VH ViewHolder that implements [RecyclerView.ViewHolder]
 * @param D Data type
 */
interface ViewHolderCreator<VH : RecyclerView.ViewHolder, D> {

	/**
	 *  Returns int [StyleRes] for theme that should be used.
	 */
	//StyleRes cannot be added to val or var in interface
	@StyleRes
	fun getTheme(): Int

	/**
	 * Creates view inside [parent] and maps it to [RecyclerView.ViewHolder] object ([VH]).
	 *
	 * @param parent Parent of the View
	 * @param viewType View type
	 * @return View holder
	 */
	fun createView(parent: CardView, viewType: Int): VH

	/**
	 * Updates view using [viewHolder] with data from [data].
	 *
	 * @param context Any context instance
	 * @param viewHolder View holder for instance that needs to be updated with data
	 * @param data Data that should be put into the View
	 */
	fun updateView(context: Context, viewHolder: VH, data: D)
}