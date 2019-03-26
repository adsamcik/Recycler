package com.adsamcik.cardlist

import androidx.annotation.IntDef


/**
 * FirstFirst - Should be used for first element
 * First - Should be used for elements on top
 * Any - Should be used when it does not matter where element ends up in the list
 * Last - Should be used for elements on bottom
 * LastLast - Should be used for last element
 */
object AppendBehaviors {
    const val FirstFirst = 0
    const val First = 1
    const val FirstLast = 2
    const val Any = 3
    const val Last = 4

    @IntDef(FirstFirst, First, FirstLast, Any, Last)
    @Retention(AnnotationRetention.SOURCE)
    annotation class AppendBehavior
}

