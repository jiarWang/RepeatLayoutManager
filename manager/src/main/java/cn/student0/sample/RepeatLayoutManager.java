package cn.student0.sample;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author : wangjian
 * @Data : 2020-12-23 10:17
 * @Describe :
 */
public class RepeatLayoutManager extends RecyclerView.LayoutManager {
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
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
        int itemLeft = getPaddingLeft();
        for (int i = 0; ; i++) {
            if (itemLeft > getWidth() - getPaddingRight()) {
                break;
            }
            View itemView = recycler.getViewForPosition(i % getItemCount());

            addView(itemView);
            measureChildWithMargins(itemView, 0, 0);

            int right = itemLeft + getDecoratedMeasuredWidth(itemView);
            int top = getPaddingTop();
            int bottom = top + getDecoratedMeasuredHeight(itemView) - getPaddingBottom();
            layoutDecorated(itemView, itemLeft, top, right, bottom);
            itemLeft = right;
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        fill(recycler, dx > 0);
        offsetChildrenHorizontal(-dx);
        recyclerChildView(dx > 0, recycler);
        return dx;
    }

    /**
     * 左右滑动的时候，填充
     */
    private void fill(RecyclerView.Recycler recycler, boolean fillEnd) {
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
                int width = getDecoratedMeasuredWidth(scrapItem);
                int height = getDecoratedMeasuredHeight(scrapItem);
                int left = anchorView.getRight();
                int top = getPaddingTop();
                int right = left + width;
                int bottom = top + height - getPaddingBottom();
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
                int width = getDecoratedMeasuredWidth(scrapItem);
                int height = getDecoratedMeasuredHeight(scrapItem);
                int right = anchorView.getLeft();
                int top = getPaddingTop();
                int left = right - width;
                int bottom = top + height - getPaddingBottom();
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
                boolean needRecycler = view != null && view.getRight() < getPaddingLeft();
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
                boolean needRecycler = view != null && view.getLeft() > getWidth() - getPaddingRight();
                if (needRecycler) {
                    removeAndRecycleView(view, recycler);
                } else {
                    return;
                }
            }
        }
    }
}
