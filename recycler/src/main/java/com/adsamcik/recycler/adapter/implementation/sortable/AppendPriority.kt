package com.adsamcik.recycler.adapter.implementation.sortable


/**
 * Data class used for sorting.
 *
 * FirstFirst - Should be used for first element
 * First - Should be used for elements on top
 * Any - Should be used when it does not matter where element ends up in the list
 * Last - Should be used for elements on bottom
 * LastLast - Should be used for last element
 *
 * @param behavior Enum of type [AppendBehavior]
 * @param priority Priority within [AppendBehavior]
 */
data class AppendPriority(val behavior: AppendBehavior, var priority: Int = 0) {

	//Kotlin shortcuts
	companion object {
		/**
		 * Shorthand for AppendPriority(AppendBehavior.Any, 0).
		 */
		val Any: AppendPriority get() = AppendPriority(AppendBehavior.Any, 0)

		/**
		 * Shorthand for AppendPriority(AppendBehavior.Start, 0).
		 */
		val Start: AppendPriority get() = AppendPriority(AppendBehavior.Start, 0)

		/**
		 * Shorthand for AppendPriority(AppendBehavior.End, 0).
		 */
		val End: AppendPriority get() = AppendPriority(AppendBehavior.End, 0)
	}
}

/**
 * Enum class representing append behavior.
 */
enum class AppendBehavior {
	Start,
	Any,
	End
}
