package com.adsamcik.recycler.adapter.implementation.multitype

import android.view.ViewGroup

interface MultiTypeViewHolderCreator<DataType : BaseMultiTypeData> {
	fun createViewHolder(parent: ViewGroup): MultiTypeViewHolder<DataType>
}
