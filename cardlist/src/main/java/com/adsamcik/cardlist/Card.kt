package com.adsamcik.cardlist

abstract class Card {
	/**
	 * Append behaviour helps the sort function with ordering the cards in ListView
	 */
	abstract val appendBehaviour: AppendBehaviour
}