package com.adsamcik.recycler.adapter.implementation.sort.callback

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
