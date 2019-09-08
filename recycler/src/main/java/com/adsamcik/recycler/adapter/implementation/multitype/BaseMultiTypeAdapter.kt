package com.adsamcik.recycler.adapter.implementation.multitype

import android.view.ViewGroup
import com.adsamcik.recycler.adapter.implementation.sort.SortableAdapter

/**
 * Base class for multi type adapter.
 */
open class BaseMultiTypeAdapter<Data : BaseMultiTypeData, ViewHolder : MultiTypeViewHolder<Data>> : SortableAdapter<Data, ViewHolder>() {
	private val typeMap = mutableMapOf<Int, MultiTypeViewHolderCreator<Data, ViewHolder>>()

	override fun getItemViewType(position: Int): Int = getItem(position).typeValue

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val type = typeMap[viewType]
		           ?: throw NotRegisteredException("Type $viewType not registered")
		return type.createViewHolder(parent)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	/**
	 * Register new type for view creation.
	 *
	 * @throws AlreadyRegisteredException Thrown if type was already registered, to prevent accidental overrides.
	 *
	 * @param typeValue Number identification of the type
	 * @param creator Creator that will handle view creation
	 */
	@Throws(AlreadyRegisteredException::class)
	fun registerType(typeValue: Int, creator: MultiTypeViewHolderCreator<Data, ViewHolder>) {
		if (typeMap.containsKey(typeValue)) {
			throw AlreadyRegisteredException("Type $typeValue already registered")
		}

		typeMap[typeValue] = creator
	}

	/**
	 * Not registered class exception.
	 */
	class NotRegisteredException(message: String) : Exception(message)

	/**
	 * Already registered class exception.
	 */
	class AlreadyRegisteredException : Exception {
		constructor(message: String?) : super(message)
		constructor(message: String?, cause: Throwable?) : super(message, cause)

	}
}

