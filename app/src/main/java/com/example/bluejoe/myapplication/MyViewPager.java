package com.example.bluejoe.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

import java.util.ArrayList;

/**
 * Created by Yunzhe on 2017/12/2.
 */

public class MyViewPager extends ViewPager implements ViewPager.OnPageChangeListener {
    int mCurrentPage;

    public MyViewPager(Context context) {
        super(context);
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        mCurrentPage = position - 1;

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public PagerAdapter getAdapter() {
        return super.getAdapter();
    }

    @Override
    public void addOnAdapterChangeListener(@NonNull OnAdapterChangeListener listener) {
        super.addOnAdapterChangeListener(listener);
    }

    @Override
    public void removeOnAdapterChangeListener(@NonNull OnAdapterChangeListener listener) {
        super.removeOnAdapterChangeListener(listener);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public int getCurrentItem() {
        return super.getCurrentItem();
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        super.setOnPageChangeListener(listener);
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(listener);
    }

    @Override
    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        super.removeOnPageChangeListener(listener);
    }

    @Override
    public void clearOnPageChangeListeners() {
        super.clearOnPageChangeListeners();
    }

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        super.setPageTransformer(reverseDrawingOrder, transformer);
    }

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer, int pageLayerType) {
        super.setPageTransformer(reverseDrawingOrder, transformer, pageLayerType);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return super.getChildDrawingOrder(childCount, i);
    }

    @Override
    public int getOffscreenPageLimit() {
        return super.getOffscreenPageLimit();
    }

    @Override
    public void setOffscreenPageLimit(int limit) {
        super.setOffscreenPageLimit(limit);
    }

    @Override
    public void setPageMargin(int marginPixels) {
        super.setPageMargin(marginPixels);
    }

    @Override
    public int getPageMargin() {
        return super.getPageMargin();
    }

    @Override
    public void setPageMarginDrawable(Drawable d) {
        super.setPageMarginDrawable(d);
    }

    @Override
    public void setPageMarginDrawable(int resId) {
        super.setPageMarginDrawable(resId);
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean beginFakeDrag() {
        return super.beginFakeDrag();
    }

    @Override
    public void endFakeDrag() {
        super.endFakeDrag();
    }

    @Override
    public void fakeDragBy(float xOffset) {
        super.fakeDragBy(xOffset);
    }

    @Override
    public boolean isFakeDragging() {
        return super.isFakeDragging();
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return super.canScrollHorizontally(direction);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        return super.canScroll(v, checkV, dx, x, y);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean executeKeyEvent(KeyEvent event) {
        return super.executeKeyEvent(event);
    }

    @Override
    public boolean arrowScroll(int direction) {
        return super.arrowScroll(direction);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        super.addFocusables(views, direction, focusableMode);
    }

    @Override
    public void addTouchables(ArrayList<View> views) {
        super.addTouchables(views);
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        return super.dispatchPopulateAccessibilityEvent(event);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return super.generateLayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return super.generateLayoutParams(attrs);
    }
}
