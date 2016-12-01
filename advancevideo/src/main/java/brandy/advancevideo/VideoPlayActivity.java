package brandy.advancevideo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import brandy.advancevideo.utils.LightnessControl;
import brandy.advancevideo.utils.TimeUtil;
import brandy.advancevideo.utils.VolumeControl;

/**
 * Android提供了VideoView对MediaPlayer和SurfaceView进行了包装
 * 继承自SurfaceView
 * 优点在于不需要进行SurfaceHolder的维护了
 */
public class VideoPlayActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, Handler.Callback, MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {

    private static final int UPDATE_PROGRESS = 0x111;
    private static final String TAG = VideoPlayActivity.class.getSimpleName();
    private FullSizeVideoView mVideoView;
    private CheckBox mPlay;
    private TextView mCurrentProgress;
    private Handler mHandler;
    private SeekBar mSeekProgress;
    private TextView mTotalProgress;
    private CheckBox mFullScreen;
    private int mHeight;//竖屏时VideoView的高度
    private boolean isLandscape;//是否横屏
    private boolean isExit;//双击退出

    private float mXPos;//动作开始时记录的X位置
    private float mYPos;//动作开始时记录的Y位置
    private float mXLastPos;//在整个动作过程中，最近的上一个回调中X的位置
    private float mYLastPos;//在整个动作过程中，最近的上一个回调中Y的位置
    private float criticalValue = 20;//判定是否为手势滑动的临界值
    private int mScreenHeight;
    private int mScreenWidth;
    private LinearLayout mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //释放资源
        mVideoView.suspend();
    }

    /**
     * 当屏幕配置信息发生改变时
     * Activity第一次初始化时不会调用
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //横屏时
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandscape = true;
        }
        //竖屏时
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            isLandscape = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isLandscape) {
                //直接设置checked状态，回调方法会自动调用
                mFullScreen.setChecked(false);
                //注意返回true来消费事件
                return true;
            }
            //双击退出
            if (!isExit) {
                isExit = true;
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                //通过定时器来延迟修改
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        String mp4 = getIntent().getStringExtra("mp4");

        mVideoView = (FullSizeVideoView) findViewById(R.id.videoView);
        //设置播放地址（如果是本地或网络地址，注意添加权限）
        if (mp4 == null) {
            mVideoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.dance);
        } else {
            mVideoView.setVideoURI(Uri.parse(mp4));
        }
        //添加一个媒体控制器（系统默认提供的）
//        mVideoView.setMediaController(new MediaController(this));
        //播放
//        mVideoView.start();

        //准备完成的监听
        mVideoView.setOnPreparedListener(this);
        //触摸监听
        mVideoView.setOnTouchListener(this);

        mPlay = (CheckBox) findViewById(R.id.play);
        mPlay.setOnCheckedChangeListener(this);
        mCurrentProgress = (TextView) findViewById(R.id.current_progress);
        mSeekProgress = (SeekBar) findViewById(R.id.seek_progress);
        mTotalProgress = (TextView) findViewById(R.id.total_progress);
        mSeekProgress.setOnSeekBarChangeListener(this);
        mFullScreen = (CheckBox) findViewById(R.id.full_screen);
        mFullScreen.setOnCheckedChangeListener(this);
        mController = (LinearLayout) findViewById(R.id.madia_controller);

        mHandler = new Handler(this);

        //获取屏幕宽高
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 播放/暂停和全屏/自身
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int i = buttonView.getId();
        if (i == R.id.play) {
            if (isChecked) {
                mVideoView.start();
                mHandler.sendEmptyMessage(UPDATE_PROGRESS);
            } else {
                mVideoView.pause();
                //当暂停时，移除MessageQueue中的指定消息
                mHandler.removeMessages(UPDATE_PROGRESS);
            }

        } else if (i == R.id.full_screen) {
            if (isChecked) {
                //记录原高度
                mHeight = mVideoView.getHeight();

                //切换全屏显示
                //添加全屏flag
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                //请求横屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                //改变VideoView的大小
                ViewGroup.LayoutParams layoutParams = mVideoView.getLayoutParams();
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                mVideoView.setLayoutParams(layoutParams);
            } else {
                //取消全屏
                //清除全屏 flag
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                //请求竖屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                //修改为原来高度
                ViewGroup.LayoutParams layoutParams = mVideoView.getLayoutParams();
                layoutParams.height = mHeight;
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                mVideoView.setLayoutParams(layoutParams);
            }
        }
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case UPDATE_PROGRESS:
                //获取当前播放位置
                int currentPosition = mVideoView.getCurrentPosition();
                //设置给当前播放时间
                mCurrentProgress.setText(TimeUtil.format(currentPosition));
                //设置给当前播放进度
                mSeekProgress.setProgress(currentPosition);
                //延迟一秒发送消息，重复修改进度
                //注意这里，当视频加载处于缓冲时，该消息也是在不断发送的，但是获取到的当前播放位置不变
                mHandler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 1000);
                break;
        }

        return true;
    }

    /**
     * 当播放源准备完成时
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        //获取总时长
        int duration = mVideoView.getDuration();
        //设置为进度最大值
        mSeekProgress.setMax(duration);
        //设置总时长
        mTotalProgress.setText(TimeUtil.format(duration));

    }

    /**
     * 拖动进度条的监听
     *
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //如果是人为操作的进度更新
        if (fromUser) {
            mHandler.removeMessages(UPDATE_PROGRESS);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //使视频播放跳转到指定位置(进度)
        mVideoView.seekTo(seekBar.getProgress());
        //延迟发送开始更新进度
        mHandler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 500);
    }

    /**
     * VideoView的横屏触摸手势处理
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //当横屏的时候对屏幕手势进行处理
        if (isLandscape) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mXPos = event.getX();
                    mYPos = event.getY();
                    mXLastPos = event.getX();
                    mYLastPos = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //X、Y的变化量
                    float xDelta = event.getX() - mXLastPos;
                    float yDelta = event.getY() - mYLastPos;
                    //横向滑动
                    if (Math.abs(xDelta) > Math.abs(yDelta) && Math.abs(xDelta) > criticalValue) {
                        if (xDelta > 0) {
                            //快进
                            goForward(xDelta);
                        } else {
                            //快退
                            goBack(xDelta);
                        }
                    }
                    //纵向滑动
                    else if (Math.abs(xDelta) < Math.abs(yDelta) && Math.abs(yDelta) > criticalValue) {
                        //注意这里是横屏，所以是屏幕高度/2来区分左右,
                        //判断当前位置是在屏幕的哪一侧
                        if (event.getX() > mScreenHeight / 2) {
                            //改变音量
                            if (yDelta > 0) {
                                //音量减
                                VolumeControl.turnDown(this, yDelta, mScreenWidth);
                            } else {
                                //音量加
                                VolumeControl.turnUp(this, yDelta, mScreenWidth);
                            }
                        } else {
                            if (yDelta > 0) {
                                //亮度减
                                LightnessControl.turnDown(this, yDelta, mScreenWidth);
                            } else {
                                //亮度加
                                LightnessControl.turnUp(this, yDelta, mScreenWidth);
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(event.getX() - mXPos) < criticalValue && Math.abs(event.getY() - mYPos) < criticalValue) {
                        showOrHideController();
                    }
                    break;
            }
            return true;
        }
        return false;
    }

    //显示或隐藏控制条
    private void showOrHideController() {
        if (mController.getVisibility() == View.GONE) {
            mController.setVisibility(View.VISIBLE);
            mController.startAnimation(AnimationUtils.loadAnimation(this, R.anim.controller_in));
        } else if (mController.getVisibility() == View.VISIBLE) {
            mController.setVisibility(View.GONE);
            mController.startAnimation(AnimationUtils.loadAnimation(this, R.anim.controller_out));
        }
    }

    //快退
    private void goBack(float xDelta) {
        int currentPosition = mVideoView.getCurrentPosition();
        int duration = mVideoView.getDuration();
        //计算出快退的值,注意此时xDelta是负值
        int change = (int) (0.05 * duration * xDelta / mScreenHeight);
        //避免小于0，获取两者中较大值作为最终值
        int finalPostion = Math.max(0, currentPosition + change);
        mVideoView.seekTo(finalPostion);
    }

    //快进
    private void goForward(float xDelta) {
        int currentPosition = mVideoView.getCurrentPosition();
        int duration = mVideoView.getDuration();
        //计算出快进的值
        int change = (int) (0.05 * duration * xDelta / mScreenHeight);
        //避免超出总时长，获取两者中较小值作为最终值
        int finalPostion = Math.min(duration, currentPosition + change);
        mVideoView.seekTo(finalPostion);
    }
}
