package brandy.advancevideo.utils;

import android.text.format.DateFormat;

/**
 * Created by lenovo on 2016/11/10.
 */

public class TimeUtil {
    public static CharSequence format(int time) {
        return DateFormat.format("mm:ss", time);
    }
}
