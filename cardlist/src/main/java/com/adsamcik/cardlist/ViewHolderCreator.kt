package com.adsamcik.cardlist

import android.content.Context
import androidx.annotation.StyleRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

interface ViewHolderCreator<VH, D> where VH : RecyclerView.ViewHolder {
	//StyleRes cannot be added to val or var in interface
	@StyleRes
	fun getTheme(): Int

	fun createView(parent: CardView, viewType: Int) : VH

	fun updateView(context: Context, viewHolder: VH, data: D)
}