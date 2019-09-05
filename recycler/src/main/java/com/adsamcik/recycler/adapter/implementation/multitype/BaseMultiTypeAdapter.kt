package com.adsamcik.recycler.adapter.implementation.multitype

import android.view.ViewGroup
import com.adsamcik.recycler.adapter.implementation.sortable.SortableAdapter

open class BaseMultiTypeAdapter<Data : BaseMultiTypeData> : SortableAdapter<Data, MultiTypeViewHolder<Data>>() {
	private val typeMap = mutableMapOf<Int, MultiTypeViewHolderCreator<Data>>()

	override fun getItemViewType(position: Int): Int = getItem(position).typeValue

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiTypeViewHolder<Data> {
		val type = typeMap[viewType]
		           ?: throw NotRegisteredException("Type $viewType not registered")
		return type.createViewHolder(parent)
	}

	override fun onBindViewHolder(holder: MultiTypeViewHolder<Data>, position: Int) {
		holder.bind(getItem(position))
	}

	@Throws(AlreadyRegisteredException::class)
	fun registerType(typeValue: Int, creator: MultiTypeViewHolderCreator<Data>) {
		if (typeMap.containsKey(typeValue)) {
			throw AlreadyRegisteredException("Type $typeValue already registered")
		}

		typeMap[typeValue] = creator
	}

	class NotRegisteredException(message: String) : Exception(message)
	class AlreadyRegisteredException(message: String) : Exception(message)
}

