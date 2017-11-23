package com.adsamcik.table;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import static com.adsamcik.table.Util.dpToPx;
import static com.adsamcik.table.Util.formatNumber;
import static com.adsamcik.table.Util.getAccentColor;
import static com.adsamcik.table.Util.getDisplayMetrics;
import static com.adsamcik.table.Util.getPressedColorRippleDrawable;

public class Table {
	public @AppendBehavior
	int appendBehavior;

	public String getTitle() {
		return title;
	}

	private String title = null;
	private ArrayList<Pair<String, String>> data;
	private ArrayList<Pair<String, View.OnClickListener>> buttons = null;

	private boolean showNumber;

	private int wrapperMarginDp;

	private int dividerColor = 0;

	/**
	 * Table constructor
	 *
	 * @param rowCount   number of data (used to initialize array holding data)
	 * @param showNumber show number of row (starts at 1)
	 */
	public Table(@NonNull Context context, int rowCount, boolean showNumber, int wrapperMarginDp, @AppendBehavior int appendBehavior) {
		this.data = new ArrayList<>(rowCount);
		this.showNumber = showNumber;
		this.appendBehavior = appendBehavior;
		this.wrapperMarginDp = wrapperMarginDp;
	}

	private void updateDividerColor(@NonNull CardView cardView) {
		int color = cardView.getCardBackgroundColor().getDefaultColor();
		double lum = ColorUtils.calculateLuminance(color);

		if (lum > 0.5)
			dividerColor = Color.argb(30, 0, 0, 0);
		else
			dividerColor = Color.argb(30, 255, 255, 255);
	}

	/*public void addToViewGroup(@NonNull ViewGroup viewGroup, @NonNull Context context, int index, boolean animate, long delay) {
		if (index >= 0 && index < viewGroup.getChildCount())
			viewGroup.addView(view, index);
		else
			viewGroup.addView(view);

		if (animate) {
			view.setTranslationY(viewGroup.getHeight());
			view.setAlpha(0);
			view.animate()
					.translationY(0)
					.setInterpolator(new DecelerateInterpolator(3.f))
					.setDuration(700)
					.setStartDelay(delay)
					.alpha(1)
					.start();
		}
	}*/

	/**
	 * Sets single title for whole table
	 *
	 * @param title title
	 * @return this table
	 */
	public Table setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * Add button to the bottom of the table
	 *
	 * @param text     title of the button
	 * @param callback on click callback
	 * @return this table
	 */
	public Table addButton(String text, View.OnClickListener callback) {
		if (buttons == null)
			buttons = new ArrayList<>(2);
		buttons.add(new Pair<>(text, callback));
		return this;
	}

	/**
	 * Adds data to 2 columns on the last row, only use this with 2 columns (+1 if row numbering is enabled)
	 *
	 * @param name  row name
	 * @param value row value
	 * @return this table
	 */
	public Table addData(String name, String value) {
		data.add(new Pair<>(name, value));
		return this;
	}

	private TableRow generateButtonsRow(@NonNull Context context, @StyleRes int theme, int sideMargin) {
		if (buttons != null) {
			DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
			TableRow row = new TableRow(context);
			TableLayout.LayoutParams lp = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			lp.topMargin = dpToPx(displayMetrics, 4);
			lp.setMargins(sideMargin, 0, sideMargin, 0);
			row.setLayoutParams(lp);

			for (int i = 0; i < buttons.size(); i++)
				row.addView(generateButton(context, displayMetrics, i, theme));
			return row;
		}
		return null;
	}

	private TextView generateButton(@NonNull Context context, DisplayMetrics displayMetrics, int index, @StyleRes int theme) {
		Button button = new Button(context, null, theme);

		//value caching
		int dp48px = dpToPx(displayMetrics, 48);
		int dp16px = dpToPx(displayMetrics, 16);

		//dimensions
		button.setMinWidth(dp48px);
		button.setPadding(dp16px, 0, dp16px, 0);
		button.setHeight(dp48px);

		//layout params
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, dp16px, 0, 0);
		button.setLayoutParams(layoutParams);

		//style
		button.setText(buttons.get(index).first.toUpperCase());
		button.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		button.setOnClickListener(buttons.get(index).second);
		button.setTextSize(16);
		button.setGravity(Gravity.CENTER);
		button.setBackground(getPressedColorRippleDrawable(0, getAccentColor(context), context.getDrawable(R.drawable.rectangle)));


		return button;
	}

	private TableRow generateDataRow(@NonNull Context context, int index, @StyleRes int theme) {
		TableRow row = new TableRow(context);

		if (showNumber) {
			TextView rowNum = new TextView(context, null, theme);
			rowNum.setText(String.format(Locale.UK, "%d", index + 1));
			rowNum.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f));
			row.addView(rowNum);
		}

		TextView textId = new TextView(context, null, theme);
		textId.setText(data.get(index).first);
		textId.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 3f));
		row.addView(textId);

		TextView textValue = new TextView(context, null, theme);
		String value = data.get(index).second;
		try {
			textValue.setText(formatNumber(Integer.parseInt(value)));
		} catch (NumberFormatException e) {
			textValue.setText(value);
		}
		textValue.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
		textValue.setGravity(Gravity.END);
		row.addView(textValue);

		return row;
	}

	/**
	 * Generates view for the table
	 *
	 * @param context        context
	 * @param requireWrapper FrameView wrapper for margin
	 * @return card view with the new table
	 */
	public View getView(@NonNull Context context, @Nullable View recycle, boolean requireWrapper, @StyleRes int theme) {
		Resources r = context.getResources();
		boolean addWrapper = wrapperMarginDp != 0 || requireWrapper;

		CardView cardView = null;
		FrameLayout frameLayout = null;

		DisplayMetrics displayMetrics = getDisplayMetrics(context);

		if (recycle != null) {
			if (recycle instanceof CardView) {
				cardView = (CardView) recycle;
			} else if (recycle instanceof FrameLayout) {
				View viewTest = ((FrameLayout) recycle).getChildAt(0);
				if (viewTest instanceof CardView) {
					cardView = (CardView) viewTest;
					frameLayout = (FrameLayout) recycle;
					//For some reason there can be view or views that have HW acceleration disabled
					//and without it shadows are not drawn. This solution recreates those views and fixes the issue
					if (!frameLayout.isHardwareAccelerated()) {
						((FrameLayout) recycle).removeView(cardView);
						addWrapper = true;
					} else
						addWrapper = false;
				}
			}
		}

		if (cardView == null) {
			cardView = new CardView(context, null, theme);
			updateDividerColor(cardView);
		}

		final int padding = (int) r.getDimension(R.dimen.table_padding);
		final int itemVerticalPadding = (int) r.getDimension(R.dimen.table_item_vertical_padding);

		TableLayout layout = (TableLayout) cardView.getChildAt(0);
		if (layout == null) {
			layout = new TableLayout(context);
			layout.setPadding(0, 30, 0, 30);
			cardView.addView(layout);
		} else
			layout.removeAllViews();

		if (title != null) {
			TextView titleView = (TextView) layout.getChildAt(0);
			if (titleView == null) {
				titleView = new TextView(context, null, theme);
				titleView.setTextSize(18);
				titleView.setTypeface(null, Typeface.BOLD);
				titleView.setGravity(Gravity.CENTER);
				titleView.setLines(1);
				titleView.setEllipsize(TextUtils.TruncateAt.END);

				TableLayout.LayoutParams titleLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				titleLayoutParams.setMargins(padding, 0, padding, dpToPx(displayMetrics, 16));
				titleView.setLayoutParams(titleLayoutParams);

				layout.addView(titleView, 0);
			}

			titleView.setText(title);
		}

		if (data.size() > 0) {
			TableRow row = generateDataRow(context, 0, theme);
			row.setPadding(padding, itemVerticalPadding, padding, itemVerticalPadding);
			layout.addView(row);
			if (data.size() > 1) {
				TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(displayMetrics, 1));

				for (int i = 1; i < data.size(); i++) {
					View divider = new View(context);
					divider.setLayoutParams(layoutParams);
					divider.setBackgroundColor(dividerColor);
					layout.addView(divider);

					row = generateDataRow(context, i, theme);
					row.setPadding(padding, itemVerticalPadding, padding, itemVerticalPadding);
					layout.addView(row);
				}
			}
		}

		TableRow buttonsRow = generateButtonsRow(context, theme, padding);
		if (buttonsRow != null)
			layout.addView(buttonsRow);

		if (addWrapper) {
			frameLayout = new FrameLayout(context);

			if (wrapperMarginDp != 0) {
				FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
				int margin = dpToPx(displayMetrics, this.wrapperMarginDp);
				layoutParams.setMargins(margin, margin, margin, margin);
				cardView.setLayoutParams(layoutParams);
			}
			frameLayout.addView(cardView);
		}

		return frameLayout != null ? frameLayout : cardView;
	}
}

