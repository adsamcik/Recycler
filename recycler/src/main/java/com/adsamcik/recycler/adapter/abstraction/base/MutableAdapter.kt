package com.adsamcik.recycler.adapter.abstraction.base

/**
 * Mutable Adapter interface provides basic functions for mutable Recycler Adapters.
 */
interface MutableAdapter<DataType> {
	/**
	 * Add data to the end of the list.
	 *
	 * @param data Data to add
	 */
	fun add(data: DataType)

	/**
	 * Add all data from [collection] to the end of the list.
	 *
	 * @param collection Collection to add
	 */
	fun addAll(collection: Collection<DataType>)

	/**
	 * Updates element at [index] with [value].
	 *
	 * @param index Index of element to update (replace)
	 * @param value Value to replace element with
	 */
	fun updateAt(index: Int, value: DataType)

	/**
	 * Removed data at [index].
	 *
	 *
	 * @param index Index used for data removal
	 */
	fun removeAt(index: Int): DataType

	/**
	 * Remove data if present.
	 *
	 * @param data Data to remove
	 *
	 * @return True if item was removed
	 */
	fun remove(data: DataType): Boolean

	/**
	 * Removes all elements from the adapter.
	 */
	fun removeAll()
}
