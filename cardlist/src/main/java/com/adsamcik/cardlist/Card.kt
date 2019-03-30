package com.adsamcik.cardlist

interface Card {
	/**
	 * Append behaviour helps the sort function with ordering the cards in ListView
	 */
	val appendBehaviour: AppendBehaviour
}