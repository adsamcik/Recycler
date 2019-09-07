package com.adsamcik.recycler.adapter.abstraction.predicate

import com.adsamcik.recycler.adapter.abstraction.base.ReadableAdapter

/**
 * Extension of [ReadableAdapter] interfaces that provides additional methods that use predicates.
 */
interface PredicateReadableAdapter<DataType> : ReadableAdapter<DataType> {
	/**
	 * Returns index of item that matches given predicate.
	 *
	 * @return Index or -1 if item was not found
	 */
	fun indexOf(predicate: (DataType) -> Boolean): Int

	/**
	 * Finds item that matches predicate.
	 *
	 * @param predicate Predicate that returns true if item is searched for
	 *
	 * @return Return found item or null if item was not found
	 */
	fun find(predicate: (DataType) -> Boolean): DataType?
}
