package brandy.advancevideo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * 处理部分机型全屏时底部会出现一段白色长条的问题
 */

public class FullSizeVideoView extends VideoView {
    public FullSizeVideoView(Context context) {
        super(context);
    }

    public FullSizeVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullSizeVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //直接保存测量值，不要让VideoView进行测量，正是其内部测量导致的这个问题
        setMeasuredDimension(width, height);

    }
}
