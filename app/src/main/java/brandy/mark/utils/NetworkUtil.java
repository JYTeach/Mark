package brandy.mark.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lenovo on 2016/11/25.
 */
public class NetworkUtil {

    public static boolean isConnected(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }
}
