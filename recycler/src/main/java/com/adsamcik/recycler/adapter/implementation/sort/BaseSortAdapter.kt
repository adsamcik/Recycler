package com.adsamcik.recycler.adapter.implementation.sort

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.adsamcik.recycler.adapter.abstraction.predicate.PredicateMutableAdapter
import com.adsamcik.recycler.adapter.abstraction.predicate.PredicateReadableAdapter

/**
 * Base sort adapter providing basic sorting functionality.
 * Allows Data sorting using callback.
 * If additional data than data provided by the object are needed, use [BaseWrapSortAdapter] instead.
 *
 * @param Data Data type that this adapter will store
 * @param VH View holder
 * @param tClass Class needed to initialize [SortedList], because [Data] is deleted.
 * @param changeCallback Change callback called when adapter changes
 * @param sortCallback Required callback that is called during sort
 */
abstract class BaseSortAdapter<Data, VH : RecyclerView.ViewHolder>(
		tClass: Class<Data>,
		private val changeCallback: ChangeCallback? = null,
		private val sortCallback: SortCallback<Data>
) : RecyclerView.Adapter<VH>(), PredicateReadableAdapter<Data>, PredicateMutableAdapter<Data> {

	//Working with PriorityWrap with specific generic type introduces a lot of mess into the adapter,
	// because it's difficult to get the java class for it
	// It's better to cast it when needed and ensure that only the right type is added
	private val dataList: SortedList<Data> = SortedList<Data>(tClass, SortedListCallback())

	override fun getItemCount() = dataList.size()

	override fun add(data: Data) {
		dataList.add(data)
	}

	override fun addAll(collection: Collection<Data>) {
		addAll(collection)
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
		return dataList.removeItemAt(index)
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
		dataList.updateItemAt(index, value)
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
			if (dataList[i] == data) {
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
	override fun getItem(index: Int): Data = dataList[index]

	private inner class SortedListCallback : SortedList.Callback<Data>() {
		override fun areItemsTheSame(item1: Data, item2: Data): Boolean {
			return sortCallback.areItemsTheSame(item1, item2)
		}

		override fun onMoved(fromPosition: Int, toPosition: Int) {
			notifyItemMoved(fromPosition, toPosition)
			changeCallback?.onMoved(fromPosition, toPosition)
		}

		override fun onChanged(position: Int, count: Int) {
			if (count == 1) {
				notifyItemChanged(position)
			} else {
				notifyItemRangeChanged(position, count)
			}
			changeCallback?.onChanged(position, count)
		}

		override fun onInserted(position: Int, count: Int) {
			if (count == 1) {
				notifyItemInserted(position)
			} else {
				notifyItemRangeInserted(position, count)
			}
			changeCallback?.onInserted(position, count)
		}

		override fun onRemoved(position: Int, count: Int) {
			if (count == 1) {
				notifyItemRemoved(position)
			} else {
				notifyItemRangeRemoved(position, count)
			}
			changeCallback?.onRemoved(position, count)
		}

		override fun compare(o1: Data, o2: Data): Int {
			return sortCallback.compare(o1, o2)
		}

		override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
			return sortCallback.areContentsTheSame(oldItem, newItem)
		}
	}

	/**
	 * Sort callback, provides sorting methods.
	 */
	interface SortCallback<Data> {
		/**
		 * Compare two
		 */
		fun compare(a: Data, b: Data): Int

		fun areContentsTheSame(a: Data, b: Data): Boolean
		fun areItemsTheSame(a: Data, b: Data): Boolean
	}

	/**
	 * Change callback. Does not provide data directly.
	 */
	interface ChangeCallback {
		/**
		 * Called when item moves.
		 */
		fun onMoved(fromIndex: Int, toIndex: Int)

		/**
		 * Called when item/s are inserted.
		 */
		fun onInserted(startIndex: Int, count: Int)

		/**
		 * Called when item/s are removed.
		 */
		fun onRemoved(startIndex: Int, count: Int)

		/**
		 * Called when item/s are changed.
		 */
		fun onChanged(startIndex: Int, count: Int)
	}
}
