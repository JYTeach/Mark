package brandy.mark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import brandy.advancevideo.VideoPlayActivity;
import brandy.mark.adapter.PlaySourceAdapter;
import brandy.mark.constants.HttpConstant;
import brandy.mark.model.DetailMovieInfo;
import brandy.mark.model.PlayData;
import brandy.mark.model.PlaySources;
import brandy.mark.model.PlaySourcesFrom;
import brandy.mark.utils.ToastUtils;
import okhttp3.Call;

@ContentView(R.layout.activity_play_source)
public class PlaySourceActivity extends BaseActivity implements ExpandableListView.OnChildClickListener {
    private static final String TAG = PlaySourceActivity.class.getSimpleName();
    @ViewInject(R.id.playSource_elv)
    private ExpandableListView mExpandableListView;
    private PlaySourceAdapter mAdapter;
    private DetailMovieInfo mDetailMovieInfo;
    private List<PlaySourcesFrom> mData;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        mDetailMovieInfo = extras.getParcelable("detailMovieInfo");

        initView();
        getData();
    }

    private void initView() {
        mDialog = new ProgressDialog(this);

        mAdapter = new PlaySourceAdapter(this, null);
        mExpandableListView.setAdapter(mAdapter);

        mExpandableListView.setOnChildClickListener(this);
    }

    private void getData() {
        mDialog.setMessage("加载中");
        mDialog.setCancelable(false);
        mDialog.show();

        OkHttpUtils.get()
                .url(HttpConstant.MOVIE_PLAY_SOURCE_1 + mDetailMovieInfo.getMovie_id() + HttpConstant.MOVIE_PLAY_SOURCE_2)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mDialog.dismiss();
                        ToastUtils.showToast(PlaySourceActivity.this, "加载失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            PlayData playData = gson.fromJson(data.toString(), PlayData.class);

                            mData = new ArrayList<PlaySourcesFrom>();
                            if (playData.getVideos() != null) {
                                PlaySourcesFrom sourcesFrom = new PlaySourcesFrom();
                                sourcesFrom.setName("预告片/花絮");
                                sourcesFrom.setPlaySources(playData.getVideos());
                                mData.add(sourcesFrom);
                            }
                            if (playData.getSourses() != null) {
                                PlaySourcesFrom sourcesFrom = new PlaySourcesFrom();
                                sourcesFrom.setName("在线观看全片");
                                sourcesFrom.setPlaySources(playData.getSourses());
                                mData.add(sourcesFrom);
                            }
                            if (playData.getOthers() != null) {
                                PlaySourcesFrom sourcesFrom = new PlaySourcesFrom();
                                sourcesFrom.setName("这些平台可能会有免费的播放源");
                                sourcesFrom.setPlaySources(playData.getOthers());
                                mData.add(sourcesFrom);
                            }

                            mDialog.dismiss();
                            mAdapter.updateRes(mData);

                            //展开
                            for (int i = 0; i < mData.size(); i++) {
                                mExpandableListView.expandGroup(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        PlaySources playSources = mData.get(groupPosition).getPlaySources().get(childPosition);
        if (playSources.getType().equals("")) {
            Intent intent = new Intent(this, VideoPlayActivity.class);
            intent.putExtra("mp4", playSources.getUrl());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, OLPlayActivity.class);
            intent.putExtra("url", playSources.getUrl());
            startActivity(intent);
        }

        return true;
    }


}
