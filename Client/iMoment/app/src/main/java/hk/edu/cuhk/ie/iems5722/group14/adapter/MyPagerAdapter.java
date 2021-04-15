package hk.edu.cuhk.ie.iems5722.group14.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {

    List<View> lsViews;//存储ViewPager需要的View

    //构造方法，用来传递View列表
    public MyPagerAdapter(List<View> lsViews, AppCompatActivity mActivity) {
        this.lsViews = lsViews;
    }

    //更新视图数据
    public void Update(List<View> lsViews) {
        this.lsViews = lsViews;
    }

    //获得视图数量
    @Override
    public int getCount() {
        return lsViews.size();
    }

    //用来判断当前视图是否需要缓存
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //翻页的时候移除之前的视图
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(lsViews.get(position));
    }

    //翻页的时候加载新的视图
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // init circles
        switch (position) {
            case 0: // TODO : chat
                break;
            case 1: // TODO : contact
                break;
            case 2: // TODO : find
                break;
            case 3: // TODO ： me
                break;
            default:
                break;


        }
        container.addView(lsViews.get(position));
        return lsViews.get(position);
    }

}
