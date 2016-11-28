package brandy.mark;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import brandy.mark.custom.TopBar;
import brandy.mark.fragments.BaseFragment;
import brandy.mark.fragments.DiscoverFragment;
import brandy.mark.fragments.MyMovieFragment;
import brandy.mark.fragments.UserFragment;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @ViewInject(R.id.topbar)
    private TopBar mTopBar;
    @ViewInject(R.id.main_container)
    private FrameLayout mContainer;
    @ViewInject(R.id.main_radioGroup)
    private RadioGroup mRadioGroup;

    private Fragment mShowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_btn_discover:
                changeFragment(DiscoverFragment.class, DiscoverFragment.TAG);
                break;
            case R.id.radio_btn_my_movie:
                changeFragment(MyMovieFragment.class, MyMovieFragment.TAG);
                break;
            case R.id.radio_btn_user:
                changeFragment(UserFragment.class, UserFragment.TAG);
                break;
        }
    }

    //切换Fragment
    private void changeFragment(Class<? extends BaseFragment> cls, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //设置切换动画
        transaction.setCustomAnimations(R.anim.fragment_in_anim, R.anim.fragment_out_anim);
        if (mShowFragment != null) {
            transaction.hide(mShowFragment);

            mShowFragment = fragmentManager.findFragmentByTag(tag);
        }

        if (mShowFragment == null) {
            try {
                mShowFragment = cls.newInstance();
                transaction.add(R.id.main_container, mShowFragment, tag);
                transaction.show(mShowFragment);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        transaction.commit();
    }
}
