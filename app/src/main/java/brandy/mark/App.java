package brandy.mark;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;

import org.xutils.x;

import java.io.IOException;

import brandy.mark.constants.HttpParams;
import brandy.mark.utils.NetworkUtil;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = App.this;
        /**
         * 网络请求的缓存本身是需要服务器支持的，需要缓存参数，客户端才可以进行缓存
         *  http传输时缓存参数就是存在于header即头文件中，
         *  Cache-Control
         *  Param
         *
         * 而Okhttp可以在请求前及请求后对数据进行包装
         * 因此可以自己去添加一些请求信息(缓存参数)来使我们的工具支持缓存
         *
         */
        Cache cache = new Cache(getCacheDir(), 10 * 1024 * 1024);

        /**
         * 当没有缓存时，
         */
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                //添加一个自定义网络拦截器
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        /**
                         * 一次完整的http请求包括request和response两部分
                         * 拦截器会拦截这次请求，将这两部分包含在chain中
                         */
                        //--------对request的处理-------------//
                        //拦截获取到这次的请求
                        Request request = chain.request();

                        //当没有网络时
                        if (!NetworkUtil.isConnected(App.this)) {
                            //重构request
                            request = request.newBuilder()
                                    //将这次请求的缓存规则设置为强制从缓存获取
                                    .cacheControl(CacheControl.FORCE_CACHE)
                                    .build();
                        }

                        //----------对response的处理---------------//
                        //拦截获取请求返回的response
                        Response response = chain.proceed(request);

                        if (NetworkUtil.isConnected(App.this)) {
                            //获取request中缓存策略
                            String cacheControl = request.cacheControl().toString();
                            response = response.newBuilder()
                                    //修改Response的结构，使其支持和request相同的缓存策略
                                    .removeHeader(HttpParams.CACHE_CONTROL)
                                    .removeHeader(HttpParams.PARAM)
                                    .addHeader(HttpParams.CACHE_CONTROL, cacheControl)
                                    .build();
                        } else {
                            //没有网络时，直接从缓存中获取(即使缓存过期)
                            response = response.newBuilder()
                                    .removeHeader(HttpParams.CACHE_CONTROL)
                                    .removeHeader(HttpParams.PARAM)
                                    //配置缓存策略为  共有，仅从缓存获取，最大过期时间
                                    .addHeader(HttpParams.CACHE_CONTROL, "public, only-if-cache, max-stale=" + 2 * 60 * 60)
                                    .build();
                        }

                        return response;
                    }
                })
                .build();

        OkHttpUtils.initClient(client);

        x.Ext.init(this);

        Picasso picasso = new Picasso.Builder(this)
                //日志
//                .loggingEnabled(true)
                //BitmapConfig，一般使用RGB_565
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                //调试标记
//                .indicatorsEnabled(true)
                .build();
        Picasso.setSingletonInstance(picasso);
    }


    /**
     * 获取缓存策略
     * 有网络时,在设置的最长时间(一般设置的比较短，用来避免频繁的网络请求)内再次请求都是从缓存中获取
     * 没有网路时，仅从缓存获取，并设置缓存的过期时间
     *
     * @return
     */
    public static String getCacheControl() {
        return NetworkUtil.isConnected(context) ? "max-age=5" : ("only-if-cache,max-stale=" + 2 * 60 * 60);
    }
}
