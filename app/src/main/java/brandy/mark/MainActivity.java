package brandy.mark;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
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
    @ViewInject(R.id.main_radioGroup)
    private RadioGroup mRadioGroup;
    @ViewInject(R.id.radio_btn_discover)
    private RadioButton mDiscover;

    private Fragment mShowFragment;
    private Resources mResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResources = getResources();

        initView();
    }

    private void initView() {
        mRadioGroup.setOnCheckedChangeListener(this);
        mDiscover.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_btn_discover:
                changeFragment(DiscoverFragment.class, DiscoverFragment.TAG);
                changeTopBar(R.string.discover, 0, false, R.mipmap.movie_search_icon, true, 0, false);
                break;
            case R.id.radio_btn_my_movie:
                changeFragment(MyMovieFragment.class, MyMovieFragment.TAG);
                break;
            case R.id.radio_btn_user:
                changeFragment(UserFragment.class, UserFragment.TAG);
                changeTopBar(R.string.user, 0, false, R.mipmap.setting_icon, true, 0, false);
                break;
        }
    }

    private void changeTopBar(int titleResId, int imageLeftResId, boolean isImageLeftShow
            , int imageRightResId, boolean isImageRightShow, int textRightResId, boolean isTextRightShow) {
        if (titleResId != 0) {
            mTopBar.setTitle(mResources.getString(titleResId), true);
        }
        mTopBar.setImageLeft(imageLeftResId, isImageLeftShow);
        mTopBar.setImageRight(imageRightResId, isImageRightShow);
        if (textRightResId != 0) {
            mTopBar.setTextRight(mResources.getString(textRightResId), isTextRightShow);
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
        } else {
            transaction.show(mShowFragment);
        }

        transaction.commit();
    }
}
