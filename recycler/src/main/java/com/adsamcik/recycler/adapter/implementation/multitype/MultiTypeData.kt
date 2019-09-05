package com.adsamcik.recycler.adapter.implementation.multitype

/**
 * Base interface for data type identification.
 */
interface BaseMultiTypeData {
	val typeValue: Int
}

/**
 * Enum based interface for data type identification.
 */
interface MultiTypeData<T : Enum<*>> : BaseMultiTypeData {
	override val typeValue: Int get() = type.ordinal
	val type: T
}

