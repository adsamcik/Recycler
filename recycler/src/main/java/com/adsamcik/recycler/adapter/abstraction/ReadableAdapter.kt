package com.adsamcik.recycler.adapter.abstraction

interface ReadableAdapter<DataType> {
	fun get(index: Int): DataType
	fun indexOf(data: DataType): Int
}
