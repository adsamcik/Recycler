package com.adsamcik.recycler.card

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.recycler.Util.toPx

/**
 * Implementation of [RecyclerView.ItemDecoration] for [CardListAdapter]. It will add uniform margin to all sides.
 */
class CardItemDecoration(private val uniformMargin: Int = DEFAULT_MARGIN) : RecyclerView.ItemDecoration() {
	override fun getItemOffsets(outRect: Rect, view: View,
	                            parent: RecyclerView, state: RecyclerView.State) {

		with(outRect) {
			if (parent.getChildAdapterPosition(view) == 0) {
				top = uniformMargin
			}
			left = uniformMargin
			right = uniformMargin
			bottom = uniformMargin
		}
	}

	companion object {
		val DEFAULT_MARGIN = 16.toPx()
	}
}