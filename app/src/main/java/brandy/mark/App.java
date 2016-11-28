package brandy.mark;

import android.app.Application;

import org.xutils.x;

/**
 * Created by lenovo on 2016/11/28.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
    }
}
