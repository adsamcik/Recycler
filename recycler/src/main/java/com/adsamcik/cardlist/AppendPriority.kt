package com.adsamcik.cardlist


/**
 * FirstFirst - Should be used for first element
 * First - Should be used for elements on top
 * Any - Should be used when it does not matter where element ends up in the list
 * Last - Should be used for elements on bottom
 * LastLast - Should be used for last element
 */
data class AppendPriority(val behavior: AppendBehavior, var priority: Int = 0) {

	//Kotlin shortcuts
	companion object {
		inline val Any get() = AppendPriority(AppendBehavior.Any)
		inline val Start get() = AppendPriority(AppendBehavior.Start)
		inline val End get() = AppendPriority(AppendBehavior.End)
	}
}

enum class AppendBehavior {
	Start,
	Any,
	End
}
