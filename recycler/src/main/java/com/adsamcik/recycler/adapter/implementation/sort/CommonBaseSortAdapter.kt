package com.adsamcik.recycler.adapter.implementation.sort

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.adsamcik.recycler.adapter.abstraction.predicate.PredicateMutableAdapter
import com.adsamcik.recycler.adapter.abstraction.predicate.PredicateReadableAdapter
import com.adsamcik.recycler.adapter.implementation.sort.callback.ChangeCallback
import com.adsamcik.recycler.adapter.implementation.sort.callback.SortCallback

abstract class CommonBaseSortAdapter<Data : Any, DataWrap : Any, ViewHolder : RecyclerView.ViewHolder>(
		dataClass: Class<DataWrap>
) :
		RecyclerView.Adapter<ViewHolder>(),
		PredicateMutableAdapter<Data>,
		PredicateReadableAdapter<Data> {
	protected val dataList: SortedList<DataWrap> = SortedList<DataWrap>(
			dataClass,
			SortedListCallback()
	)

	/**
	 * Called when data changes.
	 */
	protected open val changeCallback: ChangeCallback? = null

	/**
	 * Provides sorting methods.
	 */
	protected abstract val sortCallback: SortCallback<DataWrap>


	private inner class SortedListCallback : SortedList.Callback<DataWrap>() {
		override fun areItemsTheSame(item1: DataWrap, item2: DataWrap): Boolean {
			return sortCallback.areItemsTheSame(item1, item2)
		}

		override fun onMoved(fromPosition: Int, toPosition: Int) {
			notifyItemMoved(fromPosition, toPosition)
			changeCallback?.onMoved(fromPosition, toPosition)
		}

		override fun onChanged(position: Int, count: Int) {
			if (count == 1) {
				notifyItemChanged(position)
			} else {
				notifyItemRangeChanged(position, count)
			}
			changeCallback?.onChanged(position, count)
		}

		override fun onInserted(position: Int, count: Int) {
			if (count == 1) {
				notifyItemInserted(position)
			} else {
				notifyItemRangeInserted(position, count)
			}
			changeCallback?.onInserted(position, count)
		}

		override fun onRemoved(position: Int, count: Int) {
			if (count == 1) {
				notifyItemRemoved(position)
			} else {
				notifyItemRangeRemoved(position, count)
			}
			changeCallback?.onRemoved(position, count)
		}

		override fun compare(o1: DataWrap, o2: DataWrap): Int {
			return sortCallback.compare(o1, o2)
		}

		override fun areContentsTheSame(oldItem: DataWrap, newItem: DataWrap): Boolean {
			return sortCallback.areContentsTheSame(oldItem, newItem)
		}
	}
}
