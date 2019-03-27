package com.adsamcik.cardlist

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

interface ViewHolderCreator<VH, D> where VH : RecyclerView.ViewHolder {
	fun createView(parent: CardView, viewType: Int) : VH

	fun updateView(context: Context, viewHolder: VH, data: D)
}