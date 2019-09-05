package com.adsamcik.recycler.adapter.implementation.multitype

interface BaseMultiTypeData {
	val typeValue: Int
}

interface MultiTypeData<T : Enum<*>> : BaseMultiTypeData {
	override val typeValue: Int get() = type.ordinal
	val type: T
}

