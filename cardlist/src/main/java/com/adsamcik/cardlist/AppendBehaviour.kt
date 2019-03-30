package com.adsamcik.cardlist


/**
 * FirstFirst - Should be used for first element
 * First - Should be used for elements on top
 * Any - Should be used when it does not matter where element ends up in the list
 * Last - Should be used for elements on bottom
 * LastLast - Should be used for last element
 */
enum class AppendBehaviour {
	FirstFirst,
	First,
	FirstLast,
	Any,
	Last
}
