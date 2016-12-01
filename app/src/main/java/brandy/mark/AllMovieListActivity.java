package brandy.mark;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import brandy.mark.adapter.AllMovieListAdapter;
import brandy.mark.constants.HttpConstant;
import brandy.mark.constants.HttpParams;
import brandy.mark.model.AllMovieListData;
import brandy.mark.utils.ToastUtils;
import okhttp3.Call;

@ContentView(R.layout.activity_all_movie_list)
public class AllMovieListActivity extends BaseActivity implements AllMovieListAdapter.OnItemClickListener {

    private static final String TAG = AllMovieListActivity.class.getSimpleName();
    @ViewInject(R.id.allmovie_recycler)
    private RecyclerView mRecyclerView;
    private AllMovieListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        getData();
    }

    private void initView() {
        LinearLayoutManager layout = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layout);
        mAdapter = new AllMovieListAdapter(null, this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
    }

    private void getData() {
        OkHttpUtils.get()
                .url(HttpConstant.DIS_ALL_CAT)
                .addHeader(HttpParams.CACHE_CONTROL, App.getCacheControl())
                .build()
                .execute(new StringCallback() {
                    private List<AllMovieListData> mAllMovieListDatas;

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: 全部分类");
                        ToastUtils.showToast(AllMovieListActivity.this, "加载失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: 全部分类：" + response);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data = jsonObject.getJSONArray("data");
                            mAllMovieListDatas = gson.fromJson(data.toString(), new TypeToken<List<AllMovieListData>>() {
                            }.getType());

                            mAdapter.updateRes(mAllMovieListDatas);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(int childTag, int parentTag) {
        ToastUtils.showToast(AllMovieListActivity.this, "parentTag: " + parentTag + " childTag: " + childTag);
    }
}
