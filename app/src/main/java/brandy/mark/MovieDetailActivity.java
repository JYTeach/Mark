package brandy.mark;

import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import brandy.mark.constants.HttpConstant;
import brandy.mark.constants.HttpParams;
import brandy.mark.model.DetailMovie;
import brandy.mark.model.DetailMovieInfo;
import brandy.mark.utils.ToastUtils;
import okhttp3.Call;

@ContentView(R.layout.activity_movie_detail)
public class MovieDetailActivity extends BaseActivity {
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @ViewInject(R.id.detail_webview)
    private WebView mWebView;
    private String mId;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mId = getIntent().getStringExtra("id");

        initView();
        getData();
    }

    private void initView() {
        //设置自身客户端，则不会跳转到系统浏览器
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        //获取配置
        WebSettings settings = mWebView.getSettings();
        //开启JavaScript功能
        settings.setJavaScriptEnabled(true);

        JSInterface jsInterface = new JSInterface();
        mWebView.addJavascriptInterface(jsInterface, "JSInterface");
        mDialog = new ProgressDialog(this);
    }

    private void getData() {
        mDialog.setMessage("加载中");
        mDialog.setCancelable(false);
        mDialog.show();

        OkHttpUtils.post()
                .url(HttpConstant.MOVIE_DETAIL)
                .addHeader(HttpParams.CACHE_CONTROL, App.getCacheControl())
                .addParams("uid", HttpConstant.UID)
                .addParams("muid", HttpConstant.MUID)
                .addParams("id", mId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError: ");
                        mDialog.dismiss();
                        ToastUtils.showToast(MovieDetailActivity.this, "加载失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "onResponse: detail: " + response);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            DetailMovie detailMovie = gson.fromJson(data.toString(), DetailMovie.class);

                            mDialog.dismiss();

                            //网页原始内容
                            String content = detailMovie.getContent();
                            //替换的JS内容
                            String js = null;
                            AssetManager assets = getAssets();
                            InputStream inputStream = null;
                            ByteArrayOutputStream baos = null;
                            try {
                                inputStream = assets.open("myJS.js");
                                baos = new ByteArrayOutputStream();

                                byte[] buffer = new byte[1024];
                                int len = 0;
                                while ((len = inputStream.read(buffer)) > 0) {
                                    baos.write(buffer, 0, len);
                                    baos.flush();
                                }

                                //将baos中存放的字节数组转换为字符串
                                js = baos.toString();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (baos != null) {
                                    try {
                                        baos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                            content = content.replace("<script type=\"text/javascript\" src=\"http://api.markapp.cn/mark_web/Public/js/singledetail.js?v=26\"></script>",
                                    "<script type=\"text/javascript\">" + js + "</script>");

                            //加载网页
                            mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //JS接口
    public class JSInterface {
        @JavascriptInterface
        public void click(String text) {
            Log.e(TAG, "click: " + text);
            Gson gson = new Gson();
            try {
                JSONObject jsonObject = new JSONObject(text);
                DetailMovieInfo detailMovieInfo = gson.fromJson(jsonObject.toString(), DetailMovieInfo.class);
                if (detailMovieInfo.getClicktype().equals("leftbtn")) {

                } else if (detailMovieInfo.getClicktype().equals("rightbtn")) {

                } else if (detailMovieInfo.getClicktype().equals("img")) {
//startActivity(new );
                } else if (detailMovieInfo.getClicktype().equals("stage_photo")) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
