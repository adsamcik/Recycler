package com.adsamcik.table;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.adsamcik.table.AppendBehavior.Any;
import static com.adsamcik.table.AppendBehavior.First;
import static com.adsamcik.table.AppendBehavior.FirstFirst;
import static com.adsamcik.table.AppendBehavior.FirstLast;
import static com.adsamcik.table.AppendBehavior.Last;


/**
 * FirstFirst - Should be used for first element
 * First - Should be used for elements on top
 * Any - Should be used when it does not matter where element ends up in the list
 * Last - Should be used for elements on bottom
 * LastLast - Should be used for last element
 */
@IntDef({FirstFirst, First, FirstLast, Any, Last})
@Retention(RetentionPolicy.SOURCE)
public @interface AppendBehavior {
	int FirstFirst = 0;
	int First = 1;
	int FirstLast = 2;
	int Any = 3;
	int Last = 4;
}
