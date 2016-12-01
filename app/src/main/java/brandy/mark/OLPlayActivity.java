package brandy.mark;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_olplay)
public class OLPlayActivity extends BaseActivity {
    @ViewInject(R.id.ol_webView)
    private WebView mWebView;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUrl = getIntent().getStringExtra("url");
        initView();
    }

    private void initView() {
        //设置自身客户端，则不会跳转到系统浏览器
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        //获取配置
        WebSettings settings = mWebView.getSettings();
        //开启JavaScript功能
        settings.setJavaScriptEnabled(true);

        mWebView.loadUrl(mUrl);

    }
}
