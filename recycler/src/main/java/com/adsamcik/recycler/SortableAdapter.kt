package com.adsamcik.recycler

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList

abstract class SortableAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
	//Working with SortableData with specific generic type introduces a lot of mess into the adapter, because it's difficult to get the java class for it
	//It's better to cast it when needed and ensure that only the right type is added
	private val dataList: SortedList<SortableData<*>> = SortedList<SortableData<*>>(SortableData::class.java, SortedListCallback())


	override fun getItemCount() = dataList.size()

	/**
	 * Add tableCard to adapter
	 *
	 * @param element tableCard
	 */
	fun add(element: T, priority: AppendPriority) {
		add(SortableData(priority, element))
	}

	fun add(data: SortableData<T>) {
		dataList.add(data)
	}

	/**
	 * Adds list of [T] to the adapter
	 *
	 * @param list List of items to add
	 */
	fun addAll(list: Collection<SortableData<T>>) {
		dataList.addAll(list)
	}

	/**
	 * Remove all tableCards from adapter
	 */
	fun clear() {
		dataList.clear()
	}

	/**
	 * Removes specific element from adapter
	 * @param element Element to removeBy
	 */
	fun remove(element: T) {
		val index = indexOf(element)
		if (index >= 0)
			dataList.removeItemAt(index)
	}

	private fun indexOf(element: T): Int {
		val size = dataList.size()
		for (i in 0 until size) {
			if (dataList[i].data == element) {
				return i
			}
		}
		return -1
	}

	@Suppress("unchecked_cast")
	fun getItem(index: Int): T = dataList[index].data as T

	fun size() = dataList.size()

	data class SortableData<T>(val priority: AppendPriority, val data: T)

	inner class SortedListCallback : SortedList.Callback<SortableData<*>>() {
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
			return if (o1.priority.behavior == o2.priority.behavior)
				o1.priority.priority - o2.priority.priority
			else
				o1.priority.behavior.ordinal - o2.priority.behavior.ordinal
		}

		override fun areContentsTheSame(oldItem: SortableData<*>, newItem: SortableData<*>): Boolean {
			return oldItem.data == newItem.data
		}
	}
}