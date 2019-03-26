package com.adsamcik.cardlist

import android.content.Context
import android.view.View
import androidx.annotation.StyleRes

interface Card {
	val appendBehaviour: AppendBehaviour

	fun getView(context: Context, recycle: View?, requireWrapper: Boolean, @StyleRes theme: Int): View
}