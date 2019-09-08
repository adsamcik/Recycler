package com.adsamcik.recycler.adapter.implementation.sort

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.adsamcik.recycler.adapter.implementation.sort.SortableAdapter.SortableData

/**
 * [RecyclerView.Adapter] that supports sorting of elements. Uses == operator (equals method) for recognition of duplicates and [SortableData].
 * It is recommended to implemented the [equals] method for type [Data].
 * Sorting is not guaranteed to be stable and append priority should be used instead of relying on it.
 *
 * @param Data Data type
 * @param VH ViewHolder type
 */
@Suppress("unused")
abstract class SortableAdapter<Data, VH : RecyclerView.ViewHolder>(
		tClass: Class<Data>,
		changeCallback: ChangeCallback?,
		sortCallback: SortCallback<Data>
) : BaseSortAdapter<Data, VH>(tClass, changeCallback, sortCallback) {
	//Working with SortableData with specific generic type introduces a lot of mess into the adapter, because it's difficult to get the java class for it
	//It's better to cast it when needed and ensure that only the right type is added
	private val dataList: SortedList<SortableData<*>> = SortedList<SortableData<*>>(
			SortableData::class.java,
			SortedListCallback()
	)

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

	override fun add(data: Data) {
		add(data, AppendPriority.Any)
	}

	/**
	 * Adds list of [Data] to the adapter.
	 *
	 * @param list List of items to add
	 */
	@JvmName("addAllSortable")
	fun addAll(list: Collection<SortableData<Data>>) {
		dataList.addAll(list)
	}

	override fun addAll(collection: Collection<Data>) {
		addAll(collection.map { SortableData(it) })
	}

	override fun find(predicate: (Data) -> Boolean): Data? {
		for (i in 0 until dataList.size()) {
			val item = getItem(i)
			if (predicate(item)) {
				return item
			}
		}
		return null
	}

	/**
	 * Remove all [Data] from adapter
	 */
	override fun removeAll() {
		dataList.clear()
	}

	/**
	 * Removes specific element from adapter
	 * Uses equals (== operator) internally on [Data].
	 *
	 * @param data [Data] to remove.
	 */
	override fun remove(data: Data): Boolean {
		val index = indexOf(data)
		return if (index >= 0) {
			dataList.removeItemAt(index)
			true
		} else {
			false
		}
	}

	/**
	 * Removes item at index [index].
	 *
	 * @param index The index of the item to be removed.
	 * @return The removed item.
	 */
	override fun removeAt(index: Int): Data {
		@Suppress("unchecked_cast")
		return dataList.removeItemAt(index).data as Data
	}

	override fun removeIf(predicate: (Data) -> Boolean): Boolean {
		val index = indexOf(predicate)
		return if (index >= 0) {
			removeAt(index)
			true
		} else {
			false
		}
	}

	override fun removeAll(predicate: (Data) -> Boolean): Boolean {
		val removeList = mutableListOf<Int>()
		for (i in 0 until dataList.size()) {
			if (predicate(getItem(i))) {
				removeList.add(i)
			}
		}

		for (i in dataList.size() - 1..0) {
			dataList.removeItemAt(removeList[i])
		}

		return removeList.isNotEmpty()
	}

	override fun updateAt(index: Int, value: Data) {
		val originalItem = dataList[index]
		val newItem = SortableData(value, originalItem.priority)
		dataList.updateItemAt(index, newItem)
	}

	override fun updateIf(predicate: (Data) -> Boolean, value: Data): Boolean {
		val index = indexOf(predicate)
		return if (index >= 0) {
			updateAt(index, value)
			true
		} else {
			false
		}
	}

	override fun indexOf(data: Data): Int {
		for (i in 0 until dataList.size()) {
			if (dataList[i].data == data) {
				return i
			}
		}
		return -1
	}

	override fun indexOf(predicate: (Data) -> Boolean): Int {
		for (i in 0 until dataList.size()) {
			if (predicate(getItem(i))) {
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
	override fun getItem(index: Int): Data = dataList[index].data as Data

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

		override fun areContentsTheSame(
				oldItem: SortableData<*>,
				newItem: SortableData<*>
		): Boolean {
			return oldItem.data == newItem.data
		}
	}
}
