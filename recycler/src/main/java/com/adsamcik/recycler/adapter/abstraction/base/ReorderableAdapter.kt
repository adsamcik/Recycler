package com.adsamcik.recycler.adapter.abstraction.base

/**
 * Reorderable adapter interface
 */
interface ReorderableAdapter {
	/**
	 * Move item from one index to another.
	 *
	 * @param [from] Move item from this index
	 * @param [to] Move item to this index
	 */
	fun move(from: Int, to: Int)
}
