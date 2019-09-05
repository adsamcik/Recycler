package com.adsamcik.recycler.adapter.implementation.multitype

import android.view.ViewGroup

/**
 * Interface for multi type view holder creation
 */
interface MultiTypeViewHolderCreator<DataType : BaseMultiTypeData> {
	/**
	 * Creates View Holder for multi type adapter
	 */
	fun createViewHolder(parent: ViewGroup): MultiTypeViewHolder<DataType>
}
