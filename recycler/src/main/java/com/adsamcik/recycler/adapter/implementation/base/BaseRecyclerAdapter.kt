package com.adsamcik.recycler.adapter.implementation.base

import android.os.Build
import androidx.annotation.MainThread
import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.recycler.adapter.abstraction.base.ReorderableAdapter
import com.adsamcik.recycler.adapter.abstraction.predicate.PredicateMutableAdapter
import com.adsamcik.recycler.adapter.abstraction.predicate.PredicateReadableAdapter

/**
 * Base recycler adapter that handles data changes.
 * It should reduce boilerplate code for most adapters.
 */
@MainThread
@Suppress("TooManyFunctions")
abstract class BaseRecyclerAdapter<DataType, VH : RecyclerView.ViewHolder>
	: RecyclerView.Adapter<VH>(), PredicateReadableAdapter<DataType>,
		PredicateMutableAdapter<DataType>, ReorderableAdapter {

	private val dataList = mutableListOf<DataType>()

	override fun getItemCount(): Int = dataList.size

	override fun indexOf(data: DataType): Int {
		return dataList.indexOf(data)
	}

	override fun indexOf(predicate: (DataType) -> Boolean): Int {
		return dataList.indexOfFirst(predicate)
	}

	override fun remove(data: DataType): Boolean {
		this.dataList.indexOf(data).let { index ->
			return if (index >= 0) {
				this.dataList.removeAt(index)
				notifyItemRemoved(index)
				true
			} else {
				false
			}
		}
	}

	override fun removeAt(index: Int): DataType {
		return dataList.removeAt(index).also {
			notifyItemRemoved(index)
		}
	}

	override fun removeIf(predicate: (DataType) -> Boolean): Boolean {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			dataList.removeIf(predicate)
		} else {
			val index = indexOf(predicate)
			if (index >= 0) {
				removeAt(index)
				true
			} else {
				false
			}
		}
	}

	override fun removeAll() {
		dataList.clear()
		notifyDataSetChanged()
	}

	override fun removeAll(predicate: (DataType) -> Boolean): Boolean {
		return dataList.removeAll(predicate)
	}

	override fun add(data: DataType) {
		this.dataList.add(data)
		notifyItemInserted(this.dataList.size - 1)
	}

	/**
	 * Insert item at given index.
	 *
	 * @param index Index at which item should be inserted
	 * @param data Data that will be inserted at given index
	 */
	fun add(index: Int, data: DataType) {
		this.dataList.add(index, data)
		notifyItemInserted(index)
	}

	override fun move(from: Int, to: Int) {
		if (from == to) return

		val tmp = this.dataList.removeAt(from)
		this.dataList.add(to, tmp)
		notifyItemMoved(from, to)
	}

	override fun addAll(collection: Collection<DataType>) {
		dataList.addAll(collection)
		val lastIndex = dataList.size - 1
		notifyItemRangeInserted(lastIndex - collection.size, lastIndex)
	}

	override fun updateAt(index: Int, value: DataType) {
		dataList[index] = value
		notifyItemChanged(index)
	}

	override fun updateIf(predicate: (DataType) -> Boolean, value: DataType): Boolean {
		val index = indexOf(predicate)
		return if (index >= 0) {
			updateAt(index, value)
			true
		} else {
			false
		}
	}

	override fun getItem(index: Int): DataType {
		return dataList[index]
	}

	override fun find(predicate: (DataType) -> Boolean): DataType? {
		return dataList.find(predicate)
	}
}
