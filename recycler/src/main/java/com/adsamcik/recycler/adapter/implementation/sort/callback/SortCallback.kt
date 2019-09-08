package com.adsamcik.recycler.adapter.implementation.sort.callback

/**
 * Sort callback, provides sorting methods.
 */
interface SortCallback<Data> {
	/**
	 * Compare items [a] and [b] if they are the same or in which way they differ.
	 *
	 * @param a Item A to compare with [b]
	 * @param b Item B to compare with [a]
	 *
	 * @return a negative integer, zero, or a positive integer as the first argument is
	 * less than, equal to, or greater than the second.
	 */
	fun compare(a: Data, b: Data): Int

	/**
	 * Compare if contents of [a] and [b] are the same.
	 * Two different items can have the same contents.
	 *
	 * @return True if contents of [a] and [b] are the same (equal)
	 */
	fun areContentsTheSame(a: Data, b: Data): Boolean

	/**
	 * Compare if items are the same.
	 * Items can represent the same item and still have different contents.
	 * Eg. different versions of one item over time (eg. during update)
	 *
	 * @return True if [a] and [b] represent the same item.
	 */
	fun areItemsTheSame(a: Data, b: Data): Boolean
}
