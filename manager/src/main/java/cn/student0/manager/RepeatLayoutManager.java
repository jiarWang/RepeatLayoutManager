package cn.student0.manager;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author : wangjian
 * @Data : 2020-12-23 10:17
 * @Describe :
 */
public class RepeatLayoutManager extends RecyclerView.LayoutManager {

    @RecyclerView.Orientation
    int mOrientation = RecyclerView.HORIZONTAL;

    public RepeatLayoutManager(@RecyclerView.Orientation int orientation) {
        mOrientation = orientation;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollHorizontally() {
        return mOrientation == RecyclerView.HORIZONTAL;
    }

    @Override
    public boolean canScrollVertically() {
        return mOrientation == RecyclerView.VERTICAL;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0) {
            return;
        }
        if (state.isPreLayout()) {
            return;
        }
        //将所有Item分离至scrap
        detachAndScrapAttachedViews(recycler);
        layoutChunk(recycler);
    }

    private void layoutChunk(RecyclerView.Recycler recycler) {

        if (mOrientation == RecyclerView.HORIZONTAL) {
            int itemLeft = getPaddingLeft();
            for (int i = 0; ; i++) {
                if (itemLeft > getWidth() - getPaddingRight()) {
                    break;
                }
                View itemView = recycler.getViewForPosition(i % getItemCount());

                addView(itemView);
                measureChildWithMargins(itemView, 0, 0);

                int top = getPaddingTop();
                int right = itemLeft + getDecoratedMeasuredWidth(itemView);
                int bottom = top + getDecoratedMeasuredHeight(itemView);
                layoutDecorated(itemView, itemLeft, top, right, bottom);
                itemLeft = right;
            }
        } else {
            int itemTop = getPaddingTop();
            for (int i = 0; ; i++) {
                if (itemTop > getHeight() - getPaddingBottom()) {
                    break;
                }
                View itemView = recycler.getViewForPosition(i % getItemCount());

                addView(itemView);
                measureChildWithMargins(itemView, 0, 0);

                int left = getPaddingLeft();
                int bottom = itemTop + getDecoratedMeasuredHeight(itemView);
                int right = left + getDecoratedMeasuredWidth(itemView);
                layoutDecorated(itemView, left, itemTop, right, bottom);
                itemTop = bottom;
            }
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        fillHorizontal(recycler, dx > 0);
        offsetChildrenHorizontal(-dx);
        recyclerChildView(dx > 0, recycler);
        return dx;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        fillVertical(recycler, dy > 0);
        offsetChildrenVertical(-dy);
        recyclerChildView(dy > 0, recycler);
        return dy;
    }

    /*
     *横向填充
     */
    private void fillHorizontal(RecyclerView.Recycler recycler, boolean fillEnd) {
        if (getChildCount() == 0) return;
        if (fillEnd) {
            //填充尾部
            View anchorView = getChildAt(getChildCount() - 1);
            int anchorPosition = getPosition(anchorView);
            for (; anchorView.getRight() < getWidth() - getPaddingRight(); ) {
                int position = (anchorPosition + 1) % getItemCount();
                if (position < 0) position += getItemCount();

                View scrapItem = recycler.getViewForPosition(position);
                addView(scrapItem);
                measureChildWithMargins(scrapItem, 0, 0);
                int left = anchorView.getRight();
                int top = getPaddingTop();
                int right = left + getDecoratedMeasuredWidth(scrapItem);
                int bottom = top + getDecoratedMeasuredHeight(scrapItem);
                layoutDecorated(scrapItem, left, top, right, bottom);
                anchorView = scrapItem;
            }
        } else {
            //填充首部
            View anchorView = getChildAt(0);
            int anchorPosition = getPosition(anchorView);
            for (; anchorView.getLeft() > getPaddingLeft(); ) {
                int position = (anchorPosition - 1) % getItemCount();
                if (position < 0) position += getItemCount();

                View scrapItem = recycler.getViewForPosition(position);
                addView(scrapItem, 0);
                measureChildWithMargins(scrapItem, 0, 0);
                int right = anchorView.getLeft();
                int top = getPaddingTop();
                int left = right - getDecoratedMeasuredWidth(scrapItem);
                int bottom = top + getDecoratedMeasuredHeight(scrapItem);
                layoutDecorated(scrapItem, left, top,
                        right, bottom);
                anchorView = scrapItem;
            }
        }
        return;
    }


    /*
     *纵向填充
     */
    private void fillVertical(RecyclerView.Recycler recycler, boolean fillEnd) {
        if (getChildCount() == 0) return;
        if (fillEnd) {
            //填充尾部
            View anchorView = getChildAt(getChildCount() - 1);
            int anchorPosition = getPosition(anchorView);
            for (; anchorView.getBottom() < getHeight() - getPaddingBottom(); ) {
                int position = (anchorPosition + 1) % getItemCount();
                if (position < 0) position += getItemCount();

                View scrapItem = recycler.getViewForPosition(position);
                addView(scrapItem);
                measureChildWithMargins(scrapItem, 0, 0);
                int left = getPaddingLeft();
                int top = anchorView.getBottom();
                int right = left + getDecoratedMeasuredWidth(scrapItem);
                int bottom = top + getDecoratedMeasuredHeight(scrapItem);
                layoutDecorated(scrapItem, left, top, right, bottom);
                anchorView = scrapItem;
            }
        } else {
            //填充首部
            View anchorView = getChildAt(0);
            int anchorPosition = getPosition(anchorView);
            for (; anchorView.getTop() > getPaddingTop(); ) {
                int position = (anchorPosition - 1) % getItemCount();
                if (position < 0) position += getItemCount();

                View scrapItem = recycler.getViewForPosition(position);
                addView(scrapItem, 0);
                measureChildWithMargins(scrapItem, 0, 0);
                int left = getPaddingLeft();
                int right = left + getDecoratedMeasuredWidth(scrapItem);
                int bottom = anchorView.getTop();
                int top = bottom - getDecoratedMeasuredHeight(scrapItem);
                layoutDecorated(scrapItem, left, top,
                        right, bottom);
                anchorView = scrapItem;
            }
        }
        return;
    }

    /**
     * 回收界面不可见的view
     */
    private void recyclerChildView(boolean fillEnd, RecyclerView.Recycler recycler) {
        if (fillEnd) {
            //回收头部
            for (int i = 0; ; i++) {
                View view = getChildAt(i);
                if (view == null) return;
                boolean needRecycler = mOrientation == RecyclerView.HORIZONTAL ?
                        view.getRight() < getPaddingLeft() : view.getBottom() < getPaddingTop();
                if (needRecycler) {
                    removeAndRecycleView(view, recycler);
                } else {
                    return;
                }
            }
        } else {
            //回收尾部
            for (int i = getChildCount() - 1; ; i--) {
                View view = getChildAt(i);
                if (view == null) return;
                boolean needRecycler = mOrientation == RecyclerView.HORIZONTAL ?
                        view.getLeft() > getWidth() - getPaddingRight() : view.getTop() > getHeight() - getPaddingBottom();
                if (needRecycler) {
                    removeAndRecycleView(view, recycler);
                } else {
                    return;
                }
            }
        }
    }
}
