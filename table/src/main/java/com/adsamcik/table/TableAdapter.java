package com.adsamcik.table;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;

import static com.adsamcik.table.Util.dpToPx;

public class TableAdapter extends BaseAdapter {
	private final ArrayList<Table> tables;
	private final Context context;

	private final int itemMarginPx;

	private final @StyleRes int themeInt;

	public TableAdapter(@NonNull Context context, int itemMarginDp, @StyleRes int themeInt) {
		tables = new ArrayList<>();
		this.context = context.getApplicationContext();
		itemMarginPx = itemMarginDp == 0 ? 0 : dpToPx(context, itemMarginDp);
		this.themeInt = themeInt;
	}

	public void add(Table table) {
		tables.add(table);
	}

	public void clear() {
		tables.clear();
		notifyDataSetChanged();
	}

	public void sort() {
		Collections.sort(tables, (tx, ty) -> tx.appendBehavior - ty.appendBehavior);
		notifyDataSetChanged();
	}

	public void remove(final @AppendBehavior int appendBehavior) {
		if (Build.VERSION.SDK_INT >= 24)
			tables.removeIf(table -> table.appendBehavior == appendBehavior);
		else
			for (int i = 0; i < tables.size(); i++)
				if (tables.get(i).appendBehavior == appendBehavior)
					tables.remove(i--);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return tables.size();
	}

	@Override
	public Object getItem(int i) {
		return tables.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewGroup v = (ViewGroup) tables.get(i).getView(context, view, true, themeInt);

		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v.getChildAt(0).getLayoutParams();
		lp.setMargins(lp.leftMargin, i > 0 ? itemMarginPx / 2 : itemMarginPx, lp.rightMargin, i < getCount() - 1 ? itemMarginPx / 2 : itemMarginPx);
		v.setLayoutParams(lp);
		return v;
	}
}
