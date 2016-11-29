package brandy.mark.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import brandy.mark.R;


/**
 * 自定义使用ViewPager轮播图片类，继承FrameLayout
 */
public class CyclicPageView extends FrameLayout implements Handler.Callback {
    private static final String TAG = CyclicPageView.class.getSimpleName();
    private Context mContext;
    //轮播图图片数量，默认为1
    private final static int IMAGE_COUNT = 1;
    //自动轮播时间间隔，默认3秒
    private final static int TIME_INTERVAL = 3;
    //ImageView列表
    private List<ImageView> imageViewList;
    //小圆点列表
    private List<View> dotViewList;
    //实际的ViewPager
    private ViewPager viewPager;
    //当前轮播页面
    private int currentItem = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    //实际展示的总页数
    private int pageCount = 1;
    //实际ImageView的总数量，即imageViewList.size()
    private int imageViewCount;
    //实际的布局id
    private int resId;
    //处理页面更换
    private Handler handler = new Handler(this);

    //该类可通过反射用于设置页面切换过度时间
//    ViewPagerScroller scroller;


    public CyclicPageView(Context context) {
        super(context);
    }

    public CyclicPageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CyclicPageView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        mContext = context;
    }

    /**
     * 开始轮播图切换
     */
    public void startPlay() {
        //创建一个单个线程的调度（定时）执行器
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        //设置调度任务和时间周期，最后一个参数表示前边两个参数的时间单位
        /**
         *注意：应使任务启动的初始间隔时间同执行间隔，
         * 这样手势滑动到一张图后切换下一张图的时间就同间隔时间了
         */
        scheduledExecutorService.scheduleAtFixedRate(new LoopTask(), 3, TIME_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 停止切换
     */
    private void stopPlay() {
        //关闭该线程（强制停止正在执行的任务，不论其是否执行完毕），
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
    }

    /**
     * 初始化ImageView，需要手动调用
     *
     * @param resId               要加载的布局
     * @param pageCount           实际的图片数量，这个数量也是小圆点的数量
     * @param onLoadImageListener 加载图片的监听
     */
    public void initImageView(int resId, int pageCount, OnLoadImageListener onLoadImageListener) {
        //注意先关闭正在执行的线程
        stopPlay();

        //如果小于4张
        if (pageCount < 4) {
            //则给定一个实际的ImageView数量， 乘上一个大于4的数来确保ImageView的数量
            imageViewCount = 4 * pageCount;
        } else {
            //否则ImageView数量和图片数量一致
            imageViewCount = pageCount;
        }

        //初始化添加ImageView
        this.imageViewList = new ArrayList<>();
        for (int i = 0; i < imageViewCount; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewList.add(imageView);
        }

        //初始化一些全局量
        this.resId = resId;
        this.mOnLoadImageListener = onLoadImageListener;

        /**
         * 避免重复初始化
         * this.getChildCount() == 0 即没有子View
         * this.pageCount != pageCount 说明数据更新
         */
        if (this.resId != 0 && (this.getChildCount() == 0 || this.pageCount != pageCount)) {
            this.pageCount = pageCount;
            initUI();
        }

        startPlay();//开启轮播
    }

    /**
     * 初始化其它UI ，不要手动调用，会在初始化Image时调
     */
    private void initUI() {
        /**
         * 注意这里是依附当前View
         * 该View本质上是FrameLayout，所以是用来加载真正的ViewPager+Dot到该View中
         * 并且由于依附this，所以返回和根布局都是this
         */
        LayoutInflater.from(mContext).inflate(resId, this, true);

        //初始化小圆点
        LinearLayout dots = (LinearLayout) findViewById(R.id.normal_dots);
        this.dotViewList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            View dot = new View(mContext);
            dot.setBackgroundResource(R.drawable.dot_normal_shape);
            //使用DensityUtil工具类
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    DensityUtil.dip2px(12), DensityUtil.dip2px(12));
            layoutParams.leftMargin = DensityUtil.dip2px(10);
            layoutParams.bottomMargin = DensityUtil.dip2px(5);
            layoutParams.topMargin = DensityUtil.dip2px(5);
            dot.setLayoutParams(layoutParams);
            dotViewList.add(dot);
            dots.addView(dot);
        }

        //初始化ViewPager
        viewPager = (ViewPager) findViewById(R.id.recycle_viewPager);
        viewPager.setFocusable(true);//聚焦
        viewPager.setAdapter(new MyPagerAdapter());
        //添加页面改变监听
        viewPager.addOnPageChangeListener(new MyPageChangeListener());

        //设置一个足够大的初始位置
        currentItem = imageViewCount * 100;
        viewPager.setCurrentItem(currentItem);
    }


    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            /**
             *  在每次加载ImageView时加载图片，注意需要对其结果取绝对值，否则会出现负数
             */
            mOnLoadImageListener.onLoadImage(imageViewList.get(position % imageViewCount)
                    , Math.abs((position - imageViewCount * 100) % pageCount));

            container.addView(imageViewList.get(position % imageViewCount));

            return imageViewList.get(position % imageViewCount);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position % imageViewCount));
        }

        @Override
        public int getCount() {
            return imageViewCount != 0 ? Integer.MAX_VALUE >> 2 : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public interface OnLoadImageListener {
        /**
         * 加载图片
         *
         * @param imageView 要放置的ImageView
         * @param position  对应图片在数据源中的位置
         */
        void onLoadImage(ImageView imageView, int position);
    }

    private OnLoadImageListener mOnLoadImageListener;


    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        /**
         * 当为false时表示处于手势滑动状态
         * 当为true时表示处于滑动完毕或页面切换状态
         * 有效解决页面错乱跳转问题
         */
        boolean isAutoPlay = false;

        @Override
        //当当前页面正在滚动时
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        //当页面被选中时
        @Override
        public void onPageSelected(int position) {
            currentItem = position;

            for (int i = 0; i < dotViewList.size(); i++) {
                if (i == (position - imageViewCount * 100) % pageCount) {
                    dotViewList.get(i).setBackgroundResource(R.drawable.dot_select_shape);
                } else {
                    dotViewList.get(i).setBackgroundResource(R.drawable.dot_normal_shape);
                }
            }

        }

        @Override
        /**
         * 注意，该滑动状态监听的是手势滑动
         */
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                //正在被手势滑动
                case ViewPager.SCROLL_STATE_DRAGGING:
                    isAutoPlay = false;
                    stopPlay();//先停止正在执行的自动轮播线程，之后启动新线程
                    startPlay();
                    break;
                //手势滑动完毕（此时页面才正要开始切换）
                case ViewPager.SCROLL_STATE_IDLE:
                    break;
                //页面切换过程中，
                case ViewPager.SCROLL_STATE_SETTLING:
                    isAutoPlay = true;
                    break;
            }

        }
    }

    /**
     * 在分派触碰事件时解决两个ViewPager的滑动冲突问题
     * 由于我们的框架中父类组件(TabLayout+ViewPager)也可以响应滑动事件，因此需要在此设置
     * 防止父类组件拦截响应了（当处于第一张和最后一张图时的）滑动事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //不允许其父类拦截这个触碰事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 轮播任务
     */
    private class LoopTask implements Runnable {
        @Override
        public void run() {
            //同步使viewPager对象最多同时只允许一个线程对其修改
            synchronized (viewPager) {
                currentItem++;
                //获取一个messagePool中的message对象（性能优化，省去创建message的开销）
                //并发送该信息
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    //对页面进行切换
    @Override
    public boolean handleMessage(Message msg) {
        viewPager.setCurrentItem(currentItem);
        return true;
    }

    private void destoryBitmaps() {
        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null)
                //使该图片绑定的组件为空
                drawable.setCallback(null);
        }
    }
}

