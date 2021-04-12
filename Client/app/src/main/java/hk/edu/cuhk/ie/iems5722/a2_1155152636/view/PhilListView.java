package hk.edu.cuhk.ie.iems5722.a2_1155152636.view;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 重新listView实现上拉刷新 下拉加载
 */
public class PhilListView extends ListView {

    private Context context;

    private int firstVisibleItem = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;

    // 一个简单的圆球形进度滚动球，向用户表明正在加载
    private ProgressDialog progressDialog = null;

    private OnPullToRefreshListener mOnPullToRefreshListener = null;

    public PhilListView(Context context) {
        super(context);
        this.context = context;
    }

    public PhilListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    // 此处对外开放的回调接口，让用户可以使用上拉见底刷新或者下拉见顶刷新。
    public interface OnPullToRefreshListener {

        // 当用户的手指在屏幕上往上拉见到ListView的底部最后一个元素时候回调。
        public void onBottom();

        // 当用户的手指在屏幕上往下拉见到ListView的顶部第一个元素时候回调。
        public void onTop();
    }

    public void setOnPullToRefreshListener(OnPullToRefreshListener listener) {
        mOnPullToRefreshListener = listener;

        this.setOnScrollListener(new ListView.OnScrollListener() {

            // 把最新值赋给firstVisibleItem , visibleItemCount , totalItemCount.
            @Override
            public void onScroll(AbsListView view, int arg0, int arg1, int arg2) {
                firstVisibleItem = arg0;
                visibleItemCount = arg1;
                totalItemCount = arg2;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });

        // mGestureDetector用于监测用户在手机屏幕上的上滑和下滑事件。
        // 之所以用GestureDetector而不完全依赖ListView.OnScrollListener，主要是因为当ListView在0个元素，或者当数据元素不多不足以多屏幕滚动显示时候（换句话说，正常情况假设一屏可以显示15个，但ListView只有3个元素，那么ListView下方就会剩余空出很多空白空间，在此空间上的事件不触发ListView.OnScrollListener）。
        final GestureDetector mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {

                        // velocityY是矢量，velocityY表明手指在竖直方向（y坐标轴）移动的矢量距离。
                        // 当velocityY >0时，表明用户的手指在屏幕上往下移动。
                        // 即e2事件发生点在e1事件发生点的下方。
                        // 我们可以据此认为用户在下拉：用户下拉希望看到位于顶部的数据。
                        // 然后在这个代码块中，判断ListView中的第一个条目firstVisibleItem是否等于0 ,
                        // 等于0则意味着此时的ListView已经滑到顶部了。
                        // 然后开始回调mOnPullToRefreshListener.onTop()触发onTop()事件。
                        if (velocityY > 0) {
                            if (firstVisibleItem == 0) {
                                mOnPullToRefreshListener.onTop();
                            }
                        }

                        // 与上面的道理相同，velocityY < 0，此时的e1在e2的下方。
                        // 表明用户的手指在屏幕上往上移动，希望看到底部的数据。
                        // firstVisibleItem表明屏幕当前可见视野上第一个item的值，
                        // visibleItemCount是可见视野中的数目。
                        // totalItemCount是ListView全部的item数目
                        // 如果 firstVisibleItem + visibleItemCount ==
                        // totalItemCount，则说明此时的ListView已经见底。
                        if (velocityY < 0) {
                            int cnt = firstVisibleItem + visibleItemCount;
                            if (cnt == totalItemCount) {
                                mOnPullToRefreshListener.onBottom();
                            }
                        }

                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        this.setOnTouchListener(new View.OnTouchListener() {

            // 用mGestureDetector监测Touch事件。
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }


    /**
     * @param refreshing
     *
     * 控制在加载过程中的动画显示
     * ture：显示.
     * false：关闭
     */
    public void onRefresh(boolean refreshing) {
        if (refreshing)
            showProgress();
        else
            closeProgress();
    }

    // 显示ProgressDialog，表明正在加载...
    private void showProgress() {
        progressDialog = ProgressDialog.show(context, "PhilListView",
                "加载中,请稍候...", true, true);
    }

    // 关闭加载显示
    private void closeProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}