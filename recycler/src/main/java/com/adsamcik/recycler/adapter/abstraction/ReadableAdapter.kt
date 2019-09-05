package com.adsamcik.recycler.adapter.abstraction

interface ReadableAdapter<DataType> {
	fun getItem(index: Int): DataType
	fun indexOf(data: DataType): Int
}
