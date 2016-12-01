package brandy.mark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import brandy.mark.constants.HttpConstant;
import brandy.mark.constants.HttpParams;
import brandy.mark.custom.MoreTextView;
import brandy.mark.model.DetailMovieAllInfo;
import brandy.mark.model.DetailMovieInfo;
import brandy.mark.utils.ToastUtils;
import okhttp3.Call;

public class MovieInfoActivity extends BaseActivity implements RatingBar.OnRatingBarChangeListener, View.OnClickListener {
    private static final String TAG = MovieInfoActivity.class.getSimpleName();
    private DetailMovieInfo mDetailMovieInfo;
    private Toolbar mToolbar;
    private RatingBar mRatingBar;
    private TextView mWatchTime;
    private MoreTextView mShowing;
    private MoreTextView mLength;
    private MoreTextView mType;
    private MoreTextView mdirector;
    private MoreTextView mScreenwriter;
    private MoreTextView mStarring;
    private MoreTextView mDetailInfo;
    private ProgressDialog mDialog;
    private Button mPreview;
    private Button mPlay;
    private LinearLayout mWantSee;
    private Button mWantSeeBtn;
    private ImageButton mShowDialog;
    private Button mHaveSeeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_info);

        Bundle extras = getIntent().getExtras();
        mDetailMovieInfo = extras.getParcelable("detailMovieInfo");

        initView();
        getData();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.info_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        //设置标题
        mToolbar.setTitle(mDetailMovieInfo.getName());

        mPreview = (Button) findViewById(R.id.preview_btn);
        mPlay = (Button) findViewById(R.id.play_btn);
        mWantSee = (LinearLayout) findViewById(R.id.want_see);
        mWantSeeBtn = (Button) findViewById(R.id.want_see_btn);
        mShowDialog = (ImageButton) findViewById(R.id.show_dialog);
        mHaveSeeBtn = (Button) findViewById(R.id.have_see_btn);
        mPreview.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mWantSeeBtn.setOnClickListener(this);
        mShowDialog.setOnClickListener(this);
        mHaveSeeBtn.setOnClickListener(this);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingBar.setOnRatingBarChangeListener(this);
        mWatchTime = (TextView) findViewById(R.id.watch_time);

        ((TextView) (findViewById(R.id.showing).findViewById(R.id.info_key))).setText("上映");
        ((TextView) (findViewById(R.id.length).findViewById(R.id.info_key))).setText("片长");
        ((TextView) (findViewById(R.id.type).findViewById(R.id.info_key))).setText("类型");
        ((TextView) (findViewById(R.id.director).findViewById(R.id.info_key))).setText("导演");
        ((TextView) (findViewById(R.id.screenwriter).findViewById(R.id.info_key))).setText("编剧");
        ((TextView) (findViewById(R.id.starring).findViewById(R.id.info_key))).setText("主演");
        mShowing = ((MoreTextView) (findViewById(R.id.showing).findViewById(R.id.info_value)));
        mLength = ((MoreTextView) (findViewById(R.id.length).findViewById(R.id.info_value)));
        mType = ((MoreTextView) (findViewById(R.id.type).findViewById(R.id.info_value)));
        mdirector = ((MoreTextView) (findViewById(R.id.director).findViewById(R.id.info_value)));
        mScreenwriter = ((MoreTextView) (findViewById(R.id.screenwriter).findViewById(R.id.info_value)));
        mStarring = ((MoreTextView) (findViewById(R.id.starring).findViewById(R.id.info_value)));
        mDetailInfo = (MoreTextView) findViewById(R.id.detail_info);

        mDialog = new ProgressDialog(this);
    }

    private void getData() {
        mDialog.setMessage("加载中");
        mDialog.setCancelable(false);
        mDialog.show();

        OkHttpUtils.get()
                .url(HttpConstant.MOVIE_DETAIL_INFO + mDetailMovieInfo.getDb_num())
                .addHeader(HttpParams.CACHE_CONTROL, App.getCacheControl())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: detailInfo");
                        mDialog.dismiss();
                        ToastUtils.showToast(MovieInfoActivity.this, "加载失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: detailInfo" + response);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            DetailMovieAllInfo detailMovieAllInfo = gson.fromJson(data.toString(), DetailMovieAllInfo.class);

                            mDialog.dismiss();

                            //设置电影信息
                            mShowing.setText(detailMovieAllInfo.getPubdate());
                            mLength.setText(detailMovieAllInfo.getDuration());
                            mType.setText(detailMovieAllInfo.getGenres());
                            mdirector.setText(detailMovieAllInfo.getDirectors());
                            mScreenwriter.setText(detailMovieAllInfo.getWriters());
                            mStarring.setText(detailMovieAllInfo.getCasts());
                            mDetailInfo.setText(detailMovieAllInfo.getSummary());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //评分
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.want_see_btn:

//                mWantSee.setVisibility(View.GONE);

                break;

            case R.id.play_btn:
                Intent intent = new Intent(MovieInfoActivity.this, PlaySourceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("detailMovieInfo", mDetailMovieInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
