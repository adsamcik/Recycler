package com.adsamcik.recycler.adapter.implementation.multitype

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class MultiTypeViewHolder<Data : BaseMultiTypeData>(rootView: View) : RecyclerView.ViewHolder(rootView) {
	abstract fun bind(value: Data)
}
