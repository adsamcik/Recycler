package com.adsamcik.cardlist

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.adsamcik.cardlist.Util.toPx

/**
 * Implementation of [RecyclerView.ItemDecoration] for [CardListAdapter]. It will add uniform margin to all sides.
 */
class CardItemDecoration(private val uniformMargin: Int = 16.toPx()) : RecyclerView.ItemDecoration() {
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
}