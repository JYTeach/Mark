package brandy.mark.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

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

import brandy.mark.AllMovieListActivity;
import brandy.mark.App;
import brandy.mark.MovieDetailActivity;
import brandy.mark.R;
import brandy.mark.adapter.DisRecyclerAdapter;
import brandy.mark.constants.HttpConstant;
import brandy.mark.constants.HttpParams;
import brandy.mark.model.DisBanner;
import brandy.mark.model.DisMovie;
import brandy.mark.utils.ToastUtils;
import okhttp3.Call;

@ContentView(R.layout.fragment_discover)
public class DiscoverFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ViewTreeObserver.OnGlobalLayoutListener, DisRecyclerAdapter.OnItemClickListener {
    public static final String TAG = DiscoverFragment.class.getSimpleName();

    @ViewInject(R.id.discover_recyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.discover_refresh)
    private SwipeRefreshLayout mRefreshLayout;
    private DisRecyclerAdapter mAdapter;

    private int start = 0;//movies列表的开始位置，初始默认为0
    private int count = 10;//movies列表一次加载的数据量，默认为10
    private boolean mBannerFlag;//Banner数据是否加载成功
    private boolean mMoviesFlag;//Movies数据是否加载成功
    private boolean isLoading;//是否正在加载数据
    private List<DisMovie> mMovies;
    private List<DisBanner> mBanners;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DisRecyclerAdapter(null, null, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        //滑动监听，用于上拉加载
        mRecyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //获取总item数
                int totalItemCount = layoutManager.getItemCount();
                //获取当前可见的最后一个item的position
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= lastVisibleItem + 1) {
                    loadMore();
                    isLoading = true;
                }

            }
        });
        mAdapter.setOnItemClickListener(this);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mRefreshLayout.setOnRefreshListener(this);

        mRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    //刷新数据
    private void getData() {
        //刷新重置起始位置
        start = 0;

        OkHttpUtils.get()
                .url(HttpConstant.DIS_BANNERS)
                //添加缓存策略
                .addHeader(HttpParams.CACHE_CONTROL, App.getCacheControl())
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: banners");
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: Banner:" + response);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data = jsonObject.getJSONArray("data");
                            mBanners = gson.fromJson(data.toString(), new TypeToken<List<DisBanner>>() {
                            }.getType());

                            mAdapter.updateRes(mBanners, null);

                            //防止冲突
                            mBannerFlag = true;
                            if (mMoviesFlag) {
                                mRefreshLayout.setRefreshing(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        OkHttpUtils.post()
                .url(HttpConstant.DIS_MOVIES)
                .addHeader(HttpParams.CACHE_CONTROL, App.getCacheControl())
                .addParams("start", String.valueOf(start))
                .addParams("count", String.valueOf(count))
                .addParams("uid", HttpConstant.UID)
                .addParams("muid", HttpConstant.MUID)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: movies");
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: movies" + response);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data = jsonObject.getJSONArray("data");
                            mMovies = gson.fromJson(data.toString(), new TypeToken<List<DisMovie>>() {
                            }.getType());

                            //插入两个占位
                            mMovies.add(0, new DisMovie());
                            mMovies.add(1, new DisMovie());
                            mAdapter.updateRes(null, mMovies);

                            //设置下一次起始位
                            start += count;

                            mMoviesFlag = true;
                            if (mBannerFlag) {
                                mRefreshLayout.setRefreshing(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void loadMore() {
        OkHttpUtils.post()
                .url(HttpConstant.DIS_MOVIES)
                .addHeader(HttpParams.CACHE_CONTROL, App.getCacheControl())
                .addParams("start", String.valueOf(start))
                .addParams("count", String.valueOf(count))
                .addParams("uid", HttpConstant.UID)
                .addParams("muid", HttpConstant.MUID)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: movies");
                        isLoading = false;
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: movies" + response);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data = jsonObject.getJSONArray("data");
                            List<DisMovie> movies = gson.fromJson(data.toString(), new TypeToken<List<DisMovie>>() {
                            }.getType());

                            mAdapter.addRes(movies);
                            mMovies.addAll(movies);

                            start += count;
                            isLoading = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        getData();
    }


    //确保在根布局初始化完成后执行，这样setRefreshing(true)才能正确显示
    @Override
    public void onGlobalLayout() {
        mRefreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        mRefreshLayout.setRefreshing(true);
        getData();
    }

    @Override
    public void onItemClick(int tag) {
        if (tag == -1) {
            //分类查找按钮
            ToastUtils.showToast(getActivity(), "分类查找");
            startActivity(new Intent(getActivity(), AllMovieListActivity.class));
        } else if (tag == -2) {
            //每日电影卡片按钮
            ToastUtils.showToast(getActivity(), "每日电影卡片");

        } else if (tag == -3) {
            //影院热映
            ToastUtils.showToast(getActivity(), "影院热映");

        } else {
            if (tag > 0) {
                //movies
//                ToastUtils.showToast(getActivity(), "movies " + tag);

                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                //注意这里要将id转为String
                intent.putExtra("id", mMovies.get(tag).getId() + "");
                startActivity(intent);
            } else {
                //banners
                if (mBanners.get(tag + mBanners.size() + 3).getType() == 1) {
                    //启动影单详情(webview)
                    Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra("id", mBanners.get(tag + mBanners.size() + 3).getId() + "");
                    startActivity(intent);

                    ToastUtils.showToast(getActivity(), "启动影单详情 " + (tag + mBanners.size() + 3));
                } else if (mBanners.get(tag + mBanners.size() + 3).getType() == 2) {
                    //启动热门影单(recyclerView)
                    ToastUtils.showToast(getActivity(), "启动热门电影 " + (tag + mBanners.size() + 3));
                }
            }
        }
    }
}
