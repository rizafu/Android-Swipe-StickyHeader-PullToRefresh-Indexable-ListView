package com.rizafu.stickyswipepullindexlistview.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class IndexView extends LinearLayout {
	public IndexView(Context context) {
		super(context);
	}

	public IndexView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public IndexView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(final PullToRefreshStickyList pullToRefreshStickyList, final TextView textView) {
		setOrientation(VERTICAL);

		addTextView('#');
		for (int i = 0; i < 26; i++) {
			addTextView((char) ('A' + i));
		}

		final Animation animation = new AlphaAnimation(1, 0);
		animation.setDuration(1500);
		animation.setFillAfter(true);

		final StickyListHeadersAdapter listAdapter;
		listAdapter = pullToRefreshStickyList.getRefreshableView().getAdapter();

		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					int position = (int) (event.getY() * 27 / v.getHeight());
					if (position >= 0 && position <= 26) {
						char c;
						int localPosition;
						if (position == 0) {
							c = '#';
							localPosition = 0;
						} else {
							c = (char) ('A' - 1 + position);
							localPosition = searchPosition(listAdapter, c);
						}
						if (localPosition != -1) {
							pullToRefreshStickyList.getRefreshableView().setSelection(localPosition);
						}
						textView.setText(String.valueOf(c));
						if (textView.getVisibility() == View.GONE) {
							textView.setVisibility(View.VISIBLE);
						}
						textView.startAnimation(animation);
					} else {
						pullToRefreshStickyList.getRefreshableView().setSelection(0);
					}
					return true;
				}
				return false;

			}
		});
	}

	private void addTextView(char c) {
		TextView textView = new TextView(getContext());
		textView.setText(String.valueOf(c));
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER);
		LayoutParams lllp = new LayoutParams(
				LayoutParams.MATCH_PARENT, 0);
		lllp.weight = 1;
		addView(textView, lllp);
	}

	private int searchPosition(StickyListHeadersAdapter listAdapter, char c) {
		for (int i = 0; i < listAdapter.getCount(); i++) {
			if (c == listAdapter.getItem(i).toString().charAt(0)){
				return i;
			}
		}
		return -1;
	}
}
