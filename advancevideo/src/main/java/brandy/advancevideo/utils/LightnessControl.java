package brandy.advancevideo.utils;


import android.app.Activity;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 系统亮度是系统中的配置文件，通过ContentProvider提供统一修改
 * 需要使用ContentReslover来对其进行获取
 * 需要添加权限
 * <p>
 * 修改亮度时，
 * 1.从系统亮度中获取亮度值(0~255)
 * 2.修改页面窗口的属性中的亮度值(0~1)，这需要传入Activity
 * 3.修改系统亮度值
 */
public class LightnessControl {
    public static void turnUp(Activity context, float yDelta, float screenWidth) {
        //避免crash
        try {
            //获取当前系统中的亮度值
            int currentLight = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
            //系统亮度最大值，范围0~255
            //计算变化值
            float change = 1.5f * 255 * yDelta / screenWidth;
            //注意：需要获取页面窗口的亮度属性
            WindowManager.LayoutParams attributes = context.getWindow().getAttributes();
            //窗口亮度值0~1，需要将0~255的系统亮度转换过来
            attributes.screenBrightness = Math.min(255, currentLight - change) / 255;
            //将属性设置回窗口
            context.getWindow().setAttributes(attributes);
            //将亮度信息设置给系统
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,
                    (int) Math.min(255, currentLight - change));

        } catch (Exception e) {
            Toast.makeText(context, "系统不允许修改亮度", Toast.LENGTH_SHORT).show();
        }
    }

    public static void turnDown(Activity context, float yDelta, float screenWidth) {
        try {



            //获取当前系统中的亮度值
            int currentLight = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
            //系统亮度最大值，范围0~255
            //计算变化值
            float change = 1.5f * 255 * yDelta / screenWidth;
            //注意：需要获取页面窗口的亮度属性
            WindowManager.LayoutParams attributes = context.getWindow().getAttributes();
            //窗口亮度值0~1，需要将0~255的系统亮度转换过来
            attributes.screenBrightness = Math.max(0, currentLight - change) / 255;
            //将属性设置回窗口
            context.getWindow().setAttributes(attributes);
            //将亮度信息设置给系统
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,
                    (int) Math.max(0, currentLight - change));

        } catch (Exception e) {
            Toast.makeText(context, "系统不允许修改亮度", Toast.LENGTH_SHORT).show();
        }
    }
}
