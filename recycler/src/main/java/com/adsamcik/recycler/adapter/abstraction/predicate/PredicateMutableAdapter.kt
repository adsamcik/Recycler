package com.adsamcik.recycler.adapter.abstraction.predicate

import com.adsamcik.recycler.adapter.abstraction.base.MutableAdapter

/**
 * Extension of [MutableAdapter] interfaces that provides additional methods that use predicates.
 */
interface PredicateMutableAdapter<DataType> : MutableAdapter<DataType> {
	/**
	 * Removes zero or one items from adapter based on predicate.
	 * Returns true if item was removed.
	 *
	 * @param predicate Predicate that returns true if item should be removed
	 *
	 * @return True if item was removed
	 */
	fun removeIf(predicate: (DataType) -> Boolean): Boolean

	/**
	 * Removes all items from adapter based on predicate.
	 *
	 * @param predicate Predicate that returns true if item should be removed
	 *
	 * @return True if any items were removed
	 */
	fun removeAll(predicate: (DataType) -> Boolean): Boolean

	/**
	 * Replaces item if predicate is matched.
	 *
	 * Note: item is replaced to encourage immutability.
	 *
	 * @param predicate Predicate that returns true if item should be replaced with [value]
	 * @param value Value that replaces item that matches predicate
	 * @return True if item was updated
	 */
	fun updateIf(predicate: (DataType) -> Boolean, value: DataType): Boolean
}
