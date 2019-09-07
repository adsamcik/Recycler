package com.adsamcik.recycler.adapter.implementation.multitype

import android.view.ViewGroup

/**
 * Interface for multi type view holder creation.
 * As of Kotlin 1.3.50 DataType is required for proper generic resolving.
 */
interface MultiTypeViewHolderCreator<DataType : BaseMultiTypeData, ViewHolder : MultiTypeViewHolder<DataType>> {
	/**
	 * Creates View Holder for multi type adapter.
	 */
	fun createViewHolder(parent: ViewGroup): ViewHolder
}
