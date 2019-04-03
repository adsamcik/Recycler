package com.adsamcik.cardlist

/**
 * Card interface used by [CardListAdapter]
 */
interface Card {
	/**
	 * Append behaviour helps the sort function with ordering the cards in ListView
	 */
	val appendBehaviour: AppendBehaviour
}