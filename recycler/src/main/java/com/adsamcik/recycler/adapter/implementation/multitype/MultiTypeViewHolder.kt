package com.adsamcik.recycler.adapter.implementation.multitype

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * View holder for multi type adapter
 */
abstract class MultiTypeViewHolder<Data : BaseMultiTypeData>(rootView: View) : RecyclerView.ViewHolder(
		rootView
) {
	/**
	 * Binds view holder with data.
	 *
	 * @param data Data the holder is bound with
	 */
	abstract fun bind(data: Data)
}
