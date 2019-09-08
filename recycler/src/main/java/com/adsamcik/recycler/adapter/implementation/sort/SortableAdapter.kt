package com.adsamcik.recycler.adapter.implementation.sort

import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.recycler.adapter.implementation.sort.SortableAdapter.SortableData

/**
 * [RecyclerView.Adapter] that supports sorting of elements. Uses == operator (equals method) for recognition of duplicates and [SortableData].
 * It is recommended to implemented the [equals] method for type [Data].
 * Sorting is not guaranteed to be stable and append priority should be used instead of relying on it.
 *
 * @param Data Data type
 * @param VH ViewHolder type
 */
@Suppress("unused")
abstract class SortableAdapter<Data : Any, VH : RecyclerView.ViewHolder>(
		changeCallback: ChangeCallback? = null
) : BaseWrapSortAdapter<Data, SortableData<Data>, VH>(
		SortableData<Data>(null).javaClass,
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
		addWrap(SortableData(element, priority))
	}

	override fun wrap(data: Data): SortableData<Data> {
		return SortableData(data, AppendPriority.Any)
	}

	override fun rewrap(newData: Data, originalWrap: SortableData<Data>): SortableData<Data> {
		return originalWrap.copy(data = newData)
	}

	public override fun addAllWrap(collection: Collection<SortableData<Data>>) {
		super.addAllWrap(collection)
	}

	public override fun addWrap(dataWrap: SortableData<Data>) {
		super.addWrap(dataWrap)
	}

	/**
	 * Wrapper data class for [Data] and [AppendPriority]
	 */
	data class SortableData<Data : Any>(
			private val data: Data?,
			val priority: AppendPriority = AppendPriority.Any
	) : DataWrapper<Data> {
		override val rawData: Data
			get() = requireNotNull(data)
	}

	private class SortedListCallback<Data : Any> : SortCallback<SortableData<Data>> {
		override fun areItemsTheSame(
				a: SortableData<Data>, b: SortableData<Data>
		): Boolean {
			return a.rawData == b.rawData
		}

		override fun compare(a: SortableData<Data>, b: SortableData<Data>): Int {
			return if (a.priority.behavior == b.priority.behavior) {
				a.priority.priority - b.priority.priority
			} else {
				a.priority.behavior.ordinal - b.priority.behavior.ordinal
			}
		}

		override fun areContentsTheSame(a: SortableData<Data>, b: SortableData<Data>): Boolean {
			return a.rawData == b.rawData
		}
	}
}
