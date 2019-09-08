package com.adsamcik.recycler.adapter.implementation.sort

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.adsamcik.recycler.adapter.abstraction.predicate.PredicateMutableAdapter
import com.adsamcik.recycler.adapter.abstraction.predicate.PredicateReadableAdapter
import com.adsamcik.recycler.adapter.implementation.sort.callback.ChangeCallback
import com.adsamcik.recycler.adapter.implementation.sort.callback.SortCallback

/**
 * Base sort adapter providing basic sorting functionality with custom wrap objects.
 * Allows Data sorting using callback.
 * If raw data is enough for sorting use [BaseSortAdapter] instead.
 *
 * @param Data Data type that this adapter will store
 * @param DataWrap Data type of wrapper that will store [Data] instances
 * @param VH View holder
 * @param tClass Class needed to initialize [SortedList], because [Data] is deleted.
 */
@Suppress("TooManyFunctions")
abstract class BaseWrapSortAdapter<Data, DataWrap : DataWrapper<Data>, VH : RecyclerView.ViewHolder>(
		tClass: Class<DataWrap>
) : RecyclerView.Adapter<VH>(), PredicateReadableAdapter<Data>, PredicateMutableAdapter<Data> {

	//Working with PriorityWrap with specific generic type introduces a lot of mess into the adapter, because it's difficult to get the java class for it
	//It's better to cast it when needed and ensure that only the right type is added
	private val dataList: SortedList<DataWrap> = SortedList<DataWrap>(tClass, SortedListCallback())

	/**
	 * Called when data changes
	 */
	protected val changeCallback: ChangeCallback? = null

	/**
	 * Provides sorting methods
	 */
	protected abstract val sortCallback: SortCallback<DataWrap>

	override fun getItemCount() = dataList.size()

	/**
	 * Create new wrap instance, wrapping [data].
	 *
	 * @param data Data to wrap inside [DataWrap]
	 * @return [DataWrap] containing [data]
	 */
	protected abstract fun wrap(data: Data): DataWrap

	/**
	 * Used to rewrap existing wrapper with new data.
	 * Creating new instances as a result is recommended.
	 *
	 * @param newData New data to wrap inside [DataWrap]
	 * @param originalWrap Wrapper used for the original data
	 * @return New [DataWrap] containing containing [newData] and extra data from [originalWrap]
	 */
	protected abstract fun rewrap(newData: Data, originalWrap: DataWrap): DataWrap

	override fun add(data: Data) {
		addWrap(wrap(data))
	}

	protected open fun addWrap(dataWrap: DataWrap) {
		dataList.add(dataWrap)
	}

	override fun addAll(collection: Collection<Data>) {
		addAllWrap(collection.map { wrap(it) })
	}

	protected open fun addAllWrap(collection: Collection<DataWrap>) {
		dataList.addAll(collection)
	}

	override fun find(predicate: (Data) -> Boolean): Data? {
		return findWrap { dataWrap -> predicate(dataWrap.rawData) }?.rawData
	}

	protected open fun findWrap(predicate: (DataWrap) -> Boolean): DataWrap? {
		for (i in 0 until dataList.size()) {
			val item = getWrapItem(i)
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
	 * Removes specific element from adapter
	 * Uses equals (== operator) internally on [Data].
	 *
	 * @param data [Data] to remove.
	 */
	protected open fun remove(data: DataWrap): Boolean {
		val index = indexOfWrap(data)
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
		return dataList.removeItemAt(index).rawData
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

	protected open fun removeWrapIf(predicate: (DataWrap) -> Boolean): Boolean {
		val index = indexOfWrap(predicate)
		return if (index >= 0) {
			removeAt(index)
			true
		} else {
			false
		}
	}

	override fun removeAll(predicate: (Data) -> Boolean): Boolean {
		return removeWrapAll { predicate(it.rawData) }
	}

	protected open fun removeWrapAll(predicate: (DataWrap) -> Boolean): Boolean {
		val removeList = mutableListOf<Int>()
		for (i in 0 until dataList.size()) {
			if (predicate(getWrapItem(i))) {
				removeList.add(i)
			}
		}

		for (i in dataList.size() - 1..0) {
			dataList.removeItemAt(removeList[i])
		}

		return removeList.isNotEmpty()
	}

	override fun updateAt(index: Int, value: Data) {
		val original = dataList.get(index)
		val newWrap = rewrap(value, original)
		dataList.updateItemAt(index, newWrap)
	}

	protected open fun updateWrapAt(index: Int, value: DataWrap) {
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

	protected open fun updateWrapIf(predicate: (DataWrap) -> Boolean, value: DataWrap): Boolean {
		val index = indexOfWrap(predicate)
		return if (index >= 0) {
			updateWrapAt(index, value)
			true
		} else {
			false
		}
	}

	override fun indexOf(data: Data): Int {
		return indexOfWrapInline { data == it.rawData }
	}

	protected open fun indexOfWrap(data: DataWrap): Int {
		return indexOfWrapInline { data == it }
	}

	override fun indexOf(predicate: (Data) -> Boolean): Int {
		return indexOfWrapInline { predicate(it.rawData) }
	}

	protected open fun indexOfWrap(predicate: (DataWrap) -> Boolean): Int {
		return indexOfWrapInline(predicate)
	}

	private inline fun indexOfWrapInline(predicate: (DataWrap) -> Boolean): Int {
		for (i in 0 until dataList.size()) {
			if (predicate(getWrapItem(i))) {
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
	override fun getItem(index: Int): Data = getWrapItem(index).rawData

	protected fun getWrapItem(index: Int): DataWrap = dataList[index]

	private inner class SortedListCallback : SortedList.Callback<DataWrap>() {
		override fun areItemsTheSame(item1: DataWrap, item2: DataWrap): Boolean {
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

		override fun compare(o1: DataWrap, o2: DataWrap): Int {
			return sortCallback.compare(o1, o2)
		}

		override fun areContentsTheSame(oldItem: DataWrap, newItem: DataWrap): Boolean {
			return sortCallback.areContentsTheSame(oldItem, newItem)
		}
	}
}
