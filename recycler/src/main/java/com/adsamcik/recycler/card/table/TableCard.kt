package com.adsamcik.recycler.card.table

import android.util.Pair
import android.view.View
import android.widget.TableLayout
import androidx.recyclerview.widget.RecyclerView

open class TableCard
/**
 * TableCard constructor.
 *
 * @param rowCount   number of data (used to initialize array holding data)
 * @param showRowNumber show number of row (starts at 1)
 */
(val showRowNumber: Boolean,
 rowCount: Int = 4) {

	/**
	 * View holder used with data type [TableCard].
	 */
	data class ViewHolder(val cardView: View, val layout: TableLayout) : RecyclerView.ViewHolder(cardView)

	/**
	 * Table title.
	 */
	var title: String? = null

	private val mutableData: MutableList<Pair<String, String>> = ArrayList(rowCount)

	/**
	 * Immutable list of added data pairs.
	 * Pair(title, value)
	 */
	val data: List<Pair<String, String>> get() = mutableData

	private val mutableButtons: MutableList<Pair<String, View.OnClickListener>> = ArrayList(0)

	/**
	 * Immutable list of added buttons.
	 * Pair(button text, listener)
	 */
	val buttons: List<Pair<String, View.OnClickListener>> get() = mutableButtons


	/**
	 * Add button to the bottom of the table.
	 *
	 * @param text     title of the button
	 * @param callback on click callback
	 * @return this table
	 */
	fun addButton(text: String, callback: View.OnClickListener): TableCard {
		mutableButtons.add(Pair(text, callback))
		return this
	}

	/**
	 * Adds data to 2 columns on the last row, only use this with 2 columns (+1 if row numbering is enabled).
	 *
	 * @param name  row name
	 * @param value row value
	 * @return this table
	 */
	fun addData(name: String, value: String): TableCard {
		mutableData.add(Pair(name, value))
		return this
	}
}

