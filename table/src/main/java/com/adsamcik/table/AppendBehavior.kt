package com.adsamcik.table

import android.support.annotation.IntDef

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import com.adsamcik.table.AppendBehavior.Any
import com.adsamcik.table.AppendBehavior.First
import com.adsamcik.table.AppendBehavior.FirstFirst
import com.adsamcik.table.AppendBehavior.FirstLast
import com.adsamcik.table.AppendBehavior.Last


/**
 * FirstFirst - Should be used for first element
 * First - Should be used for elements on top
 * Any - Should be used when it does not matter where element ends up in the list
 * Last - Should be used for elements on bottom
 * LastLast - Should be used for last element
 */
@IntDef(FirstFirst, First, FirstLast, Any, Last)
@Retention(RetentionPolicy.SOURCE)
annotation class AppendBehavior {
    companion object {
        val FirstFirst = 0
        val First = 1
        val FirstLast = 2
        val Any = 3
        val Last = 4
    }
}
