package com.adsamcik.cardlist.card

import com.adsamcik.cardlist.AppendPriority

/**
 * Card interface used by [CardListAdapter]
 */
interface Card {
	/**
	 * Append behaviour helps the sort function with ordering the cards in ListView
	 */
	val appendPriority: AppendPriority
}