package com.adsamcik.recycler.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.adsamcik.recycler.Util.dp
import kotlin.math.ceil

/**
 * Implementation of [RecyclerView.ItemDecoration] providing margins to items.
 * Supports [GridLayoutManager], [StaggeredGridLayoutManager], [LinearLayoutManager].
 *
 * @param verticalMargin Vertical margin between items
 * @param horizontalMargin Horizontal margin between items
 * @param firstLineMargin First line margin in orientation of the recycler
 * @param lastLineMargin Last line margin in orientation of the recycler
 */
class MarginDecoration(
		private val verticalMargin: Int = DEFAULT_MARGIN.dp,
		private val horizontalMargin: Int = DEFAULT_MARGIN.dp,
		private val firstLineMargin: Int = DEFAULT_MARGIN.dp,
		private val lastLineMargin: Int = DEFAULT_MARGIN.dp
) : RecyclerView.ItemDecoration() {

	private fun getMaxItemsForGrid(itemCount: Int, columnCount: Int): Int {
		val columnCountFloat = columnCount.toFloat()
		// This needs to use ceil in order to correctly handle case when column is full and when it is not
		return (ceil(itemCount.toFloat() / columnCountFloat) * columnCountFloat).toInt()
	}

	private fun setOffsetsHorizontal(
			outRect: Rect,
			parent: RecyclerView,
			position: Int,
			columnCount: Int
	) {
		with(outRect) {
			left = if (position < columnCount) {
				firstLineMargin
			} else {
				horizontalMargin
			}

			val itemCount = parent.adapter?.itemCount ?: 0
			val maxGridItems = getMaxItemsForGrid(itemCount, columnCount)
			if (position >= maxGridItems - columnCount) right = lastLineMargin

			top = verticalMargin
			bottom = verticalMargin
		}
	}

	private fun setOffsetsVertical(
			outRect: Rect,
			parent: RecyclerView,
			position: Int,
			columnCount: Int
	) {
		with(outRect) {
			top = if (position < columnCount) {
				firstLineMargin
			} else {
				verticalMargin
			}

			val itemCount = parent.adapter?.itemCount ?: 0
			val maxGridItems = getMaxItemsForGrid(itemCount, columnCount)
			if (position >= maxGridItems - columnCount) bottom = lastLineMargin

			left = horizontalMargin
			right = horizontalMargin
		}
	}

	override fun getItemOffsets(
			outRect: Rect,
			view: View,
			parent: RecyclerView,
			state: RecyclerView.State
	) {

		val columnCount: Int
		val orientation: Int
		when (val layoutManager = parent.layoutManager) {
			is GridLayoutManager -> {
				columnCount = layoutManager.spanCount
				orientation = layoutManager.orientation
			}
			is LinearLayoutManager -> {
				columnCount = 1
				orientation = layoutManager.orientation
			}
			is StaggeredGridLayoutManager -> {
				columnCount = layoutManager.spanCount
				orientation = layoutManager.orientation
			}
			else -> {
				columnCount = 1
				orientation = RecyclerView.VERTICAL
			}
		}

		val position = parent.getChildAdapterPosition(view)

		when (orientation) {
			RecyclerView.VERTICAL -> setOffsetsVertical(outRect, parent, position, columnCount)
			RecyclerView.HORIZONTAL -> setOffsetsHorizontal(outRect, parent, position, columnCount)
			else -> throw IllegalStateException("Unknown orientation with value $orientation")
		}
	}

	companion object {
		private const val DEFAULT_MARGIN = 16
	}
}
