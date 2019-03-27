package com.adsamcik.cardlist.table

import android.util.Pair
import android.view.View
import android.widget.TableLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.cardlist.AppendBehaviour
import com.adsamcik.cardlist.Card

class TableCard
/**
 * TableCard constructor
 *
 * @param rowCount   number of data (used to initialize array holding data)
 * @param showNumber show number of row (starts at 1)
 */
(val showNumber: Boolean,
 val rowCount: Int = 4,
 override val appendBehaviour: AppendBehaviour = AppendBehaviour.Any) : Card {

	data class ViewHolder(val cardView: View, val titleView: TextView, val layout: TableLayout) : RecyclerView.ViewHolder(cardView)

	var title: String? = null

	val data: ArrayList<Pair<String, String>> = ArrayList(rowCount)
	var buttons: ArrayList<Pair<String, View.OnClickListener>> = ArrayList(0)


	/**
	 * Add button to the bottom of the table
	 *
	 * @param text     title of the button
	 * @param callback on click callback
	 * @return this table
	 */
	fun addButton(text: String, callback: View.OnClickListener): TableCard {
		buttons.add(Pair(text, callback))
		return this
	}

	/**
	 * Adds data to 2 columns on the last row, only use this with 2 columns (+1 if row numbering is enabled)
	 *
	 * @param name  row name
	 * @param value row value
	 * @return this table
	 */
	fun addData(name: String, value: String): TableCard {
		data.add(Pair(name, value))
		return this
	}
}

