package com.adsamcik.recycler.adapter.abstraction.base

/**
 * Readable adapter interface.
 */
interface ReadableAdapter<DataType> {

	/**
	 * Returns item at [index].
	 *
	 * @param index Index of desired item
	 *
	 * @return Item at [index]
	 */
	fun getItem(index: Int): DataType

	/**
	 * Returns index of [data].
	 *
	 * @param data Data that is looked at.
	 *
	 * @return index of [data] or -1 if not found.
	 */
	fun indexOf(data: DataType): Int
}
