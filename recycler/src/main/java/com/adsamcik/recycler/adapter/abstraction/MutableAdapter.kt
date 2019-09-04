package com.adsamcik.recycler.adapter.abstraction

interface MutableAdapter<DataType> {
	/**
	 * Add data to the end of the list
	 *
	 * @param data Data to add
	 */
	fun add(data: DataType)

	/**
	 * Add all data from [collection] to the end of the list
	 *
	 * @param collection Collection to add
	 */
	fun addAll(collection: Collection<DataType>)

	/**
	 * Removed data at [index].
	 *
	 *
	 * @param index Index used for data removal
	 */
	fun removeAt(index: Int): DataType

	/**
	 * Remove data if present
	 *
	 * @param data Data to remove
	 */
	fun remove(data: DataType)

	/**
	 * Removes all elements from the adapter
	 */
	fun removeAll()
}
