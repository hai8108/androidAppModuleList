/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.yanxiu.gphone.student.view.xlistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.yanxiu.gphone.student.R;

import java.util.LinkedList;
import java.util.Queue;

public class XListView extends ListView implements OnScrollListener {

    private Queue<Long> queue_succ_time = new LinkedList<Long>();
    private long LimitCount = 3;
    private long LimitTime = 10;

    // private boolean mCanPullRefresh = true;
    // private boolean mCanPullLoad = false; // is refreashing.

    private float mLastY = -1; // save event y
    private Scroller mScroller; // used for scroll back
    private OnScrollListener mScrollListener; // user's scroll listener

    // the interface to trigger refresh and load more.
    private IXListViewListener mListViewListener;

    // -- header view
    public XListViewHeaderNew mHeaderView;
    // header view content, use it to calculate the Header's height. And hide it
    // when disable pull refresh.
    private RelativeLayout mHeaderViewContent;
//    private TextView mHeaderTimeView;
    private int mHeaderViewHeight; // header view's height

    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false; // is refreashing.

    // -- footer view
    private XListViewFooter mFooterView;
    private boolean mEnablePullLoad = false;
    private boolean mPullLoading;
    private boolean mIsFooterReady = false;

    // total list items, used to detect is at the bottom of listview.
    private int mTotalItemCount;

    // for mScroller, scroll back from header or footer.
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400; // scroll back duration
    private final static int PULL_LOAD_MORE_DELTA = 20; // when pull up >= 50px
                                                        // at bottom, trigger
                                                        // load more.
    private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
                                                    // feature.

    private boolean scrollable = true;
    private boolean isAutoLoad = false;

    private View emptyView;
    private long cacheTime = 0l;
    private boolean isShowHeaders = false;// 是否 在没有数据的时候 显示header轮播图等。

    /**
     * @param context
     */
    public XListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    @Override
    public void setEmptyView(View emptyView) {
        this.addEmptyView(emptyView);
    }

    private void initWithContext(Context context) {
        setFadingEdgeLength(0);
        queue_succ_time.offer(0l);
        queue_succ_time.offer(0l);
        queue_succ_time.offer(0l);

        // android:scrollingCache="false"
        setScrollingCacheEnabled(false);
        mScroller = new Scroller(context, new DecelerateInterpolator());
        // XListView need the scroll event, and it will dispatch the event to
        // user's listener (as a proxy).
        super.setOnScrollListener(this);

        // init header view
        mHeaderView = new XListViewHeaderNew(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView
                .findViewById(R.id.xlistview_header_content);
//        mHeaderTimeView = (TextView) mHeaderView
//                .findViewById(R.id.xlistview_header_time);
        // addHeaderView(mHeaderView);
        addHeaderView(mHeaderView, null, false);

        // init footer view
        mFooterView = new XListViewFooter(context);

        // init header height
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mHeaderViewHeight = mHeaderViewContent.getHeight();
                        if (isAutoLoad) {
                            isAutoLoad = false;
                            startRefresh();
                        }
                        getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
        this.setOnScrollListener(new OnXScrollListener() {
            @Override
            public void onXScrolling(View view) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                        isBusying = false;
                        break;
                    case OnScrollListener.SCROLL_STATE_FLING://滚动状态
                        isBusying = true;
                        break;
                    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                        isBusying = false;
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

//                LogInfo.log("geny", "last item" + getLastVisiblePosition() + "----" +  mTotalItemCount);
//                LogInfo.log("geny", "last item" + isBusying + "----" +  totalItemCount);
//                if(getLastVisiblePosition() == mTotalItemCount - 1 && isBusying && mEnablePullLoad){
                if(getLastVisiblePosition() == mTotalItemCount - 1 && mEnablePullLoad){
                    isBusying = false;
                    startLoadMore();
                }
            }
        });


    }

    private boolean isBusying;

    private DataSetObserver myadapter;

    public void setAdapter(BaseAdapter adapter) {
        // make sure XListViewFooter is the last footer view, and only add once.
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(mFooterView);
        }

        if (adapter == null) {
            if (myadapter != null) {
                getAdapter().unregisterDataSetObserver(myadapter);
            }
        } else {
            myadapter = new DataSetObserver() {

                @Override
                public void onChanged() {
                    // TODO Auto-generated method stub
                    super.onChanged();
                    isEmpty();
                }

            };
            adapter.registerDataSetObserver(myadapter);
        }

        super.setAdapter(adapter);
    }

    /**
     * enable or disable pull down refresh feature.
     * 
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     * 
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        mFooterView.setVisibility(View.VISIBLE);
        if (!mEnablePullLoad) {
            mFooterView.hide();
            mFooterView.setOnClickListener(null);
        } else {
            mPullLoading = false;
            mFooterView.show();
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * 禁用底部的"加载更多"功能,相比setPullLoadEnable方法,此方法会移除相关view
     */
    public void disallowPullLoadView(){
        if(mIsFooterReady) {
            removeFooterView(mFooterView);
        }else {
            mIsFooterReady = true;
        }
        setPullLoadEnable(false);
    }

    /**
     * stop refresh, reset header view.
     */
    public void stopRefresh() {

        if (mPullRefreshing == true) {

            mPullRefreshing = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    post(new Runnable() {
                        public void run() {
                            resetHeaderHeight();
                        }
                    });
                }
            }).start();
            // setRefreshTime(StringUtils.getCurrentDateAndTime());
            // TCAgent.onPageEnd(null ,"下拉刷新页");
        }
    }

    /**
     * stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
        }
        // TCAgent.onPageEnd(null ,"上拉加载更多页");
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta
                + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                // L.i("XListView", "updateHeaderHeight,STATE_READY");
                mHeaderView.setState(XListViewHeaderNew.STATE_READY);
            } else {
                // L.i("XListView", "updateHeaderHeight,STATE_NORMAL");
                mHeaderView.setState(XListViewHeaderNew.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * reset header view's height.
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        // L.i("XListView",
        // "resetHeaderHeight,"+height+".mPullRefreshing,"+mPullRefreshing);
        if (height == 0){ // not visible.
            mHeaderView.setVisiableHeight(0);
            return;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height,
                SCROLL_DURATION);
        // trigger computeScroll
        invalidate();
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;
        if (mEnablePullLoad && !mPullLoading && !mPullRefreshing) {
            if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
                                                 // more.
                mFooterView.setState(XListViewFooter.STATE_READY);
            } else {
                mFooterView.setState(XListViewFooter.STATE_NORMAL);
            }
        }

        mFooterView.setBottomMargin(height);

        // setSelection(mTotalItemCount - 1); // scroll to bottom
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
                    SCROLL_DURATION);
            invalidate();
        }
    }

    /**
     * 需求：如果没显示完成，则等到显示完成后再load，如果已经显示出来了，则直接load
     */
    public void startRefresh() {

        // 为了避免上次的scrollto 0没结束，造成的一闪而过，结束上次遗留动画。
        mScroller.forceFinished(true);

        showListView();
//        mFooterView.setVisibility(View.GONE);
        if (mHeaderViewHeight <= 0) {
            isAutoLoad = true;
        } else {
            mPullRefreshing = true;
            updateHeaderHeight(mHeaderViewHeight
                    - mHeaderView.getVisiableHeight());
            mHeaderView.setState(XListViewHeaderNew.STATE_REFRESHING);
            if (mListViewListener != null) {
                mListViewListener.onRefresh(this);
            }
        }
        // TCAgent.onPageStart(null ,"下拉刷新页");

    }

    public void startRefreshUI() {

        // 为了避免上次的scrollto 0没结束，造成的一闪而过，结束上次遗留动画。
        mScroller.forceFinished(true);

        showListView();
        mFooterView.setVisibility(View.GONE);

        if (mHeaderViewHeight <= 0) {
            isAutoLoad = true;
        } else {
            mPullRefreshing = true;
            updateHeaderHeight(mHeaderViewHeight
                    - mHeaderView.getVisiableHeight());
            mHeaderView.setState(XListViewHeaderNew.STATE_REFRESHING);
            // if (mListViewListener != null) {
            // mListViewListener.onRefresh(this);
            // }
        }

    }

    private void startLoadMore() {
        if (mPullLoading) {
            return;
        }
        if (mPullRefreshing) {
            // Toast.makeText(getContext(), "正在刷新请稍等", 2000).show();
            resetFooterHeight();
            mPullLoading = false;
            mFooterView.setState(XListViewFooter.STATE_NORMAL);
            return;
        }
        mPullLoading = true;
        mFooterView.setState(XListViewFooter.STATE_LOADING);
        if (mListViewListener != null) {
            mListViewListener.onLoadMore(this);
        }
        // TCAgent.onPageStart(null ,"上拉加载更多页");
    }


    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
            setRefreshTime(cacheTime);
        }

        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mLastY = ev.getRawY();
            break;
        case MotionEvent.ACTION_MOVE:
            // L.i("XListView", "ACTION_MOVE"+mLastY);
            final float deltaY = ev.getRawY() - mLastY;
            mLastY = ev.getRawY();
            if (getFirstVisiblePosition() == 0
                    && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                // the first item is showing, header has shown or pull down.
                if (!mPullRefreshing && !mPullLoading) {
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                }
            } else if (mEnablePullLoad&&getLastVisiblePosition() == mTotalItemCount - 1
                    && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                // last item, already pulled up or want to pull up.
                //良师通 去掉次功能
               /* if (!mPullLoading && !mPullRefreshing) {
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }*/
//                else{
//                    startLoadMore();
//                    LogInfo.log("geny", "last item kanjian");
//                }

            }
            break;
        default:

            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mLastY = -1; // reset
                            if (getFirstVisiblePosition() == 0
                                    && (mHeaderView.getVisiableHeight() > 0)) {
                                // invoke refresh
                                if (mEnablePullRefresh
                                        && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {

                                    if (mPullLoading) {
                                        Toast.makeText(getContext(), "正在加载请稍等", Toast.LENGTH_LONG).show();
                                    } else {
                                        mPullRefreshing = true;
                                        // 限制10秒内3次成功访问，目前不做限制，所以时间设置为10*10毫秒了
                                        // if(checkTime()){
                                        if (mListViewListener != null) {
                                            mListViewListener
                                                    .onRefresh(XListView.this);
                                        }
                                        mHeaderView
                                                .setState(XListViewHeaderNew.STATE_REFRESHING);
                                        // }else{
                                        // stopRefresh();
                                        // UIHelper.toast(getContext(),
                                        // getResources().getString(R.string.xlistview_toast_10_sec));
                                        // }
                                    }
                                }
                                resetHeaderHeight();
                            } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                                // invoke load more.
                                if (mEnablePullLoad
                                        && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                                    startLoadMore();
                                }
                                resetFooterHeight();
                            } else {
                                // L.i("XListView", "default");
                                // resetHeaderHeight();
                            }
                        }
                    });
                }
            }).start();

            /*
             * mLastY = -1; // reset if (getFirstVisiblePosition() == 0&&
             * (mHeaderView.getVisiableHeight() > 0 )) { // invoke refresh if
             * (mEnablePullRefresh && mHeaderView.getVisiableHeight() >
             * mHeaderViewHeight) {
             * 
             * if(mPullLoading){ Toast.makeText(getContext(), "正在加载请稍等",
             * 2000).show(); }else{ mPullRefreshing = true; if(checkTime()){ if
             * (mListViewListener != null) { mListViewListener.onRefresh(this);
             * } mHeaderView.setState(XListViewHeaderNew.STATE_REFRESHING); }else{
             * stopRefresh(); UIHelper.toast(getContext(),
             * "哎呦，暂时没新的啦，去订阅中心看看为您精选的更多栏目吧~"); } } } resetHeaderHeight(); }
             * else if (getLastVisiblePosition() == mTotalItemCount - 1) { //
             * invoke load more. if (mEnablePullLoad &&
             * mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
             * startLoadMore(); } resetFooterHeight(); }else{ L.i("XListView",
             * "default"); resetHeaderHeight(); // new Thread(new Runnable() {
             * // // @Override // public void run() { // // TODO Auto-generated
             * method stub // try { // Thread.sleep(100); // } catch
             * (InterruptedException e) { // // TODO Auto-generated catch block
             * // e.printStackTrace(); // } // post(new Runnable() { // //
             * @Override // public void run() { // // TODO Auto-generated method
             * stub //// onTouchEvent(ev); // resetHeaderHeight(); // } // });
             * // } // }).start();
             * 
             * 
             * }
             */
            break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
                if(mScroller.getCurrY()<=0){
                    mHeaderView.setState(mHeaderView.STATE_DONE);
                }
                
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

    public void setXListViewListener(IXListViewListener l) {
        mListViewListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * implements this interface to get refresh/load more event.
     */
    public interface IXListViewListener {
        public void onRefresh(XListView view);

        public void onLoadMore(XListView view);
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_MOVE && !scrollable) {

            return true;

        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * set last refresh time
     * 
     * @param time
     */
    private void setRefreshTime(long time) {
        // L.i("setRefreshTime", time+"");
        cacheTime = time;
        if (time <= 0) {
//            mHeaderTimeView.setText(getContext().getString(
//                    R.string.xlistview_header_no_time));
        } else {
//            String strtime = getContext().getString(
//                    R.string.xlistview_header_last_time,
//                    StringUtils.convertCacheTime(time));
//            mHeaderTimeView.setText(strtime);
            // L.i("setRefreshTime", strtime);
        }
    }

    /**
     * 从cache中获取成功后，需要设置这个时间
     * 
     * @param time
     */
    public void setCacheTime(long time) {
        setRefreshTime(time);
    }

    /**
     * 查询是否是频繁刷新操作
     * 
     * @return
     */
    private boolean checkTime() {
        long currtime = System.currentTimeMillis();
        long head = queue_succ_time.element();
        if (currtime - head < 0) {
            return true;
        }
        return (currtime - head) > LimitTime * 1000 ? true : false;
    }

    /**
     * @param currtime
     *            成功获取的时间
     */
    public void setSuccRefreshTime(long currtime) {
        mHeaderView.setState(XListViewHeaderNew.STATE_SUCC);
        setRefreshTime(currtime);
        if (queue_succ_time.size() >= LimitCount) {
            // 如果超出长度,入队时,先出队
            queue_succ_time.poll();
        }
        // 入队
        queue_succ_time.offer(currtime);
        // showSuccToast();
    }

    public long getSuccRefreshTime() {

        return queue_succ_time.peek();

    }

    public void addEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    private void showListView() {
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
        setVisibility(View.VISIBLE);
    }

    private void isEmpty() {
        if (isAutoLoad)
            return;

        if (getAdapter() == null || (getAdapter().isEmpty() && !isShowHeaders)) {
            if (emptyView != null) {
                emptyView.setVisibility(View.VISIBLE);
                setVisibility(View.GONE);
            }
            mFooterView.setVisibility(View.GONE);

        } else {
            if (emptyView != null) {
                emptyView.setVisibility(View.GONE);
            }
            setVisibility(View.VISIBLE);

            if (mEnablePullLoad) {
                mFooterView.setVisibility(View.VISIBLE);
            }
        }
    }

    public boolean isRefreshing() {
        return mPullRefreshing;
    }

    public boolean isShowHeaders() {
        return isShowHeaders;
    }

    public void setShowHeaders(boolean isShowHeaders) {
        this.isShowHeaders = isShowHeaders;
    }
    
}
