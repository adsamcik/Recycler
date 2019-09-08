package com.adsamcik.recycler.adapter.implementation.sort

import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.recycler.adapter.implementation.sort.PrioritySortAdapter.PriorityWrap
import com.adsamcik.recycler.adapter.implementation.sort.callback.ChangeCallback
import com.adsamcik.recycler.adapter.implementation.sort.callback.SortCallback

/**
 * [RecyclerView.Adapter] that supports sorting of elements. Uses == operator (equals method) for recognition of duplicates and [PriorityWrap].
 * It is recommended to implemented the [equals] method for type [Data].
 * Sorting is not guaranteed to be stable and append priority should be used instead of relying on it.
 *
 * @param Data Data type
 * @param VH ViewHolder type
 */
@Suppress("unused")
abstract class PrioritySortAdapter<Data : Any, VH : RecyclerView.ViewHolder>(
		changeCallback: ChangeCallback? = null
) : BaseWrapSortAdapter<Data, PriorityWrap<Data>, VH>(
		PriorityWrap<Data>(null).javaClass,
		changeCallback,
		SortedListCallback<Data>()
) {
	/**
	 * Add [Data] to the adapter
	 *
	 * @param element data
	 * @param priority priority used for sorting
	 */
	fun add(element: Data, priority: AppendPriority) {
		addWrap(PriorityWrap(element, priority))
	}

	override fun wrap(data: Data): PriorityWrap<Data> {
		return PriorityWrap(data, AppendPriority.Any)
	}

	override fun rewrap(newData: Data, originalWrap: PriorityWrap<Data>): PriorityWrap<Data> {
		return originalWrap.copy(data = newData)
	}

	public override fun addAllWrap(collection: Collection<PriorityWrap<Data>>) {
		super.addAllWrap(collection)
	}

	public override fun addWrap(dataWrap: PriorityWrap<Data>) {
		super.addWrap(dataWrap)
	}

	/**
	 * Wrapper data class for [Data] and [AppendPriority].
	 */
	data class PriorityWrap<Data : Any>(
			private val data: Data?,
			val priority: AppendPriority = AppendPriority.Any
	) : DataWrapper<Data> {
		override val rawData: Data
			get() = requireNotNull(data)
	}

	private class SortedListCallback<Data : Any> : SortCallback<PriorityWrap<Data>> {
		override fun areItemsTheSame(
				a: PriorityWrap<Data>, b: PriorityWrap<Data>
		): Boolean {
			return a.rawData == b.rawData
		}

		override fun compare(a: PriorityWrap<Data>, b: PriorityWrap<Data>): Int {
			return if (a.priority.behavior == b.priority.behavior) {
				a.priority.priority - b.priority.priority
			} else {
				a.priority.behavior.ordinal - b.priority.behavior.ordinal
			}
		}

		override fun areContentsTheSame(a: PriorityWrap<Data>, b: PriorityWrap<Data>): Boolean {
			return a.rawData == b.rawData
		}
	}
}
