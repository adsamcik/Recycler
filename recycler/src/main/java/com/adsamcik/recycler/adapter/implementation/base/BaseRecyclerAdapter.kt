package com.adsamcik.recycler.adapter.implementation.base

import androidx.annotation.MainThread
import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.recycler.adapter.abstraction.MutableAdapter
import com.adsamcik.recycler.adapter.abstraction.ReadableAdapter
import com.adsamcik.recycler.adapter.abstraction.ReorderableAdapter

/**
 * Base recycler adapter that handles data changes.
 * It should reduce boilerplate code for most adapters.
 */
@MainThread
abstract class BaseRecyclerAdapter<DataType, VH : RecyclerView.ViewHolder>
	: RecyclerView.Adapter<VH>(), ReadableAdapter<DataType>, MutableAdapter<DataType>, ReorderableAdapter {
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

	fun add(index: Int, data: DataType) {
		this.dataList.add(index, data)
		notifyItemInserted(index)
	}

	override fun move(from: Int, to: Int) {
		if (from == to) return

		val tmp = this.dataList.removeAt(from)
		val toAdjusted = if (from < to) to - 1 else to
		this.dataList.add(toAdjusted, tmp)
		notifyItemMoved(from, to)
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

	override fun getItem(index: Int): DataType {
		return dataList[index]
	}
}
