package com.adsamcik.recycler.adapter.implementation

import androidx.annotation.MainThread
import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.recycler.adapter.abstraction.MutableAdapter
import com.adsamcik.recycler.adapter.abstraction.ReadableAdapter

/**
 * Base recycler adapter that handles data changes.
 * It should reduce boilerplate code for most adapters.
 */
@MainThread
abstract class BaseRecyclerAdapter<DataType, VH : RecyclerView.ViewHolder>
	: RecyclerView.Adapter<VH>(), ReadableAdapter<DataType>, MutableAdapter<DataType> {
	private val dataList = mutableListOf<DataType>()

	override fun getItemCount(): Int = dataList.size

	override fun indexOf(data: DataType): Int {
		return dataList.indexOf(data)
	}

	override fun removeAll() {
		dataList.clear()
		notifyDataSetChanged()
	}

	override fun removeAt(index: Int): DataType {
		return dataList.removeAt(index).also {
			notifyItemRemoved(index)
		}
	}

	override fun add(data: DataType) {
		this.dataList.add(data)
		notifyItemInserted(this.dataList.size - 1)
	}

	override fun addAll(collection: Collection<DataType>) {
		dataList.addAll(collection)
		val lastIndex = dataList.size - 1
		notifyItemRangeInserted(lastIndex - collection.size, lastIndex)
	}

	override fun remove(data: DataType) {
		this.dataList.indexOf(data).let { index ->
			this.dataList.removeAt(index)
			notifyItemRemoved(index)
		}
	}

	override fun get(index: Int): DataType {
		return dataList[index]
	}
}
