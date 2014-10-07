package com.rizafu.stickyswipepullindexlistview.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;


import com.handmark.pulltorefresh.library.OverscrollHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.rizafu.stickyswipepullindexlistview.R;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class PullToRefreshStickyList extends PullToRefreshBase<StickyListHeadersListView> {

private static final OnRefreshListener<StickyListHeadersListView> defaultOnRefreshListener = new OnRefreshListener<StickyListHeadersListView>() {

    @Override
    public void onRefresh(PullToRefreshBase<StickyListHeadersListView> refreshView) {

    }

};

public PullToRefreshStickyList(Context context) {
    super(context);

    /**
     * Added so that by default, Pull-to-Refresh refreshes the page
     */
    setOnRefreshListener(defaultOnRefreshListener);
}

public PullToRefreshStickyList(Context context, AttributeSet attrs) {
    super(context, attrs);

    /**
     * Added so that by default, Pull-to-Refresh refreshes the page
     */
    setOnRefreshListener(defaultOnRefreshListener);
}

public PullToRefreshStickyList(Context context, Mode mode) {
    super(context, mode);

    /**
     * Added so that by default, Pull-to-Refresh refreshes the page
     */
    setOnRefreshListener(defaultOnRefreshListener);
}

public PullToRefreshStickyList(Context context, Mode mode, AnimationStyle style) {
    super(context, mode, style);

    /**
     * Added so that by default, Pull-to-Refresh refreshes the page
     */
    setOnRefreshListener(defaultOnRefreshListener);
}

@Override
public final Orientation getPullToRefreshScrollDirection() {
    return Orientation.VERTICAL;
}

@Override
protected StickyListHeadersListView createRefreshableView(Context context, AttributeSet attrs) {
	StickyListHeadersListView StickySwipe;

    if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
	    StickySwipe = new InternalStaggeredGridViewSDK9(context, attrs);
    } else {
	    StickySwipe = new StickyListHeadersListView(context, attrs);
    }

	StickySwipe.setId(R.id.gridview);
    return StickySwipe;
}

@Override
protected boolean isReadyForPullStart() {

    boolean result = false;
    View v = getRefreshableView().getChildAt(0);
    if (getRefreshableView().getFirstVisiblePosition() == 0) {
        if (v != null) {
            // getTop() and getBottom() are relative to the ListView,
            // so if getTop() is negative, it is not fully visible
            boolean isTopFullyVisible = v.getTop() >= 0;

            result = isTopFullyVisible;
        }
    }
    return result;
}

@Override
protected boolean isReadyForPullEnd() {
    boolean result = false;
    int last = getRefreshableView().getChildCount() - 1;
    View v = getRefreshableView().getChildAt(last);

    int firstVisiblePosition = getRefreshableView().getFirstVisiblePosition();
    int visibleItemCount = getRefreshableView().getChildCount();
    int itemCount = getRefreshableView().getAdapter().getCount();
    if (firstVisiblePosition + visibleItemCount >= itemCount) {
        if (v != null) {
            boolean isLastFullyVisible = v.getBottom() <= getRefreshableView().getHeight();

            result = isLastFullyVisible;
        }
    }
    return result;
}

@Override
protected void onPtrRestoreInstanceState(Bundle savedInstanceState) {
    super.onPtrRestoreInstanceState(savedInstanceState);
}

@Override
protected void onPtrSaveInstanceState(Bundle saveState) {
    super.onPtrSaveInstanceState(saveState);
}

@TargetApi(9)
final class InternalStaggeredGridViewSDK9 extends StickyListHeadersListView {

    // WebView doesn't always scroll back to it's edge so we add some
    // fuzziness
    static final int OVERSCROLL_FUZZY_THRESHOLD = 2;

    // WebView seems quite reluctant to overscroll so we use the scale
    // factor to scale it's value
    static final float OVERSCROLL_SCALE_FACTOR = 1.5f;

    public InternalStaggeredGridViewSDK9(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
            int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

        // Does all of the hard work...
        // OverscrollHelper.overScrollBy(PullToRefreshStaggeredGridView.this,
        // deltaX, scrollX, deltaY, scrollY,
        // getScrollRange(), OVERSCROLL_FUZZY_THRESHOLD,
        // OVERSCROLL_SCALE_FACTOR, isTouchEvent);

        // Does all of the hard work...
        OverscrollHelper.overScrollBy(PullToRefreshStickyList.this, deltaX, scrollX, deltaY,
		        getScrollRange(), isTouchEvent);

        return returnValue;
    }

    /**
     * Taken from the AOSP ScrollView source
     */
    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }

}
}