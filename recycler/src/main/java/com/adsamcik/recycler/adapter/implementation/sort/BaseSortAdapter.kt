package com.adsamcik.recycler.adapter.implementation.sort

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList

/**
 * Base sort adapter providing basic sorting functionality.
 * Allows Data sorting using callback.
 * If additional data than data provided by the object are needed, use [BaseWrapSortAdapter] instead.
 *
 * @param Data Data type that this adapter will store
 * @param VH View holder
 * @param dataClass Class needed to initialize [SortedList], because [Data] is deleted.
 */
@Suppress("TooManyFunctions")
abstract class BaseSortAdapter<Data : Any, VH : RecyclerView.ViewHolder>(
		dataClass: Class<Data>
) : CommonBaseSortAdapter<Data, Data, VH>(dataClass) {

	override fun getItemCount() = dataList.size()

	override fun add(data: Data) {
		dataList.add(data)
	}

	override fun addAll(collection: Collection<Data>) {
		dataList.addAll(collection)
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
	 * Remove all [Data] from adapter.
	 */
	override fun removeAll() {
		dataList.clear()
	}

	/**
	 * Removes specific element from adapter.
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
}
