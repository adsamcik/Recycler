package com.adsamcik.recycler

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.adsamcik.recycler.SortableAdapter.SortableData

/**
 * [RecyclerView.Adapter] that supports sorting of elements. Uses == operator (equals method) for recognition of duplicates and [SortableData].
 * It is recommended to implemented the [equals] method for type [Data].
 * Sorting is not guaranteed to be stable and append priority should be used instead of relying on it.
 *
 * @param Data Data type
 * @param VH ViewHolder type
 */
abstract class SortableAdapter<Data, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
	//Working with SortableData with specific generic type introduces a lot of mess into the adapter, because it's difficult to get the java class for it
	//It's better to cast it when needed and ensure that only the right type is added
	private val dataList: SortedList<SortableData<*>> = SortedList<SortableData<*>>(SortableData::class.java, SortedListCallback())

	override fun getItemCount() = dataList.size()

	/**
	 * Add [Data] to the adapter
	 *
	 * @param element data
	 * @param priority priority used for sorting
	 */
	fun add(element: Data, priority: AppendPriority) {
		add(SortableData(element, priority))
	}

	/**
	 * Add [Data] and [AppendPriority] wrapped in [SortableData] to the adapter
	 *
	 * @param data [SortableData] containing desired priority and raw data inside
	 */
	fun add(data: SortableData<Data>) {
		dataList.add(data)
	}

	/**
	 * Adds list of [Data] to the adapter.
	 *
	 * @param list List of items to add
	 */
	fun addAll(list: Collection<SortableData<Data>>) {
		dataList.addAll(list)
	}

	/**
	 * Remove all [Data] from adapter
	 */
	fun clear() {
		dataList.clear()
	}

	/**
	 * Removes specific element from adapter
	 * Uses equals (== operator) internally on [Data]
	 *
	 * @param element [Data] to remove.
	 */
	fun remove(element: Data) {
		val index = indexOf(element)
		if (index >= 0) {
			dataList.removeItemAt(index)
		}
	}

	private fun indexOf(element: Data): Int {
		val size = dataList.size()
		for (i in 0 until size) {
			if (dataList[i].data == element) {
				return i
			}
		}
		return -1
	}

	/**
	 * Returns [Data] at position [index].
	 *
	 * @param index Index used in the lookup
	 */
	@Suppress("unchecked_cast")
	fun getItem(index: Int): Data = dataList[index].data as Data

	/**
	 * Returns number of [Data] objects added to the adapter.
	 *
	 * @return Count of [Data]
	 */
	fun size() = dataList.size()

	/**
	 * Wrapper data class for [Data] and [AppendPriority]
	 */
	data class SortableData<Data>(val data: Data, val priority: AppendPriority = AppendPriority.Any)

	private inner class SortedListCallback : SortedList.Callback<SortableData<*>>() {
		override fun areItemsTheSame(item1: SortableData<*>, item2: SortableData<*>): Boolean {
			return item1.data == item2.data
		}

		override fun onMoved(fromPosition: Int, toPosition: Int) {
			notifyItemMoved(fromPosition, toPosition)
		}

		override fun onChanged(position: Int, count: Int) {
			if (count == 1) {
				notifyItemChanged(position)
			} else {
				notifyItemRangeChanged(position, count)
			}
		}

		override fun onInserted(position: Int, count: Int) {
			if (count == 1) {
				notifyItemInserted(position)
			} else {
				notifyItemRangeInserted(position, count)
			}
		}

		override fun onRemoved(position: Int, count: Int) {
			if (count == 1) {
				notifyItemRemoved(position)
			} else {
				notifyItemRangeRemoved(position, count)
			}
		}

		override fun compare(o1: SortableData<*>, o2: SortableData<*>): Int {
			return if (o1.priority.behavior == o2.priority.behavior) {
				o1.priority.priority - o2.priority.priority
			} else {
				o1.priority.behavior.ordinal - o2.priority.behavior.ordinal
			}
		}

		override fun areContentsTheSame(oldItem: SortableData<*>, newItem: SortableData<*>): Boolean {
			return oldItem.data == newItem.data
		}
	}
}