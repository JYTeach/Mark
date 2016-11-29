package brandy.mark.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import brandy.mark.R;

/**
 * Created by lenovo on 2016/11/28.
 */
public class TopBar extends Toolbar {
    private ImageView mImageLeft;
    private ImageView mImageRight;
    private TextView mTitle;
    private TextView mTextRight;

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化
        LayoutInflater.from(context).inflate(R.layout.topbar_layout, this, true);
        initView();
    }

    private void initView() {
        mImageLeft = (ImageView) findViewById(R.id.image_left);
        mImageRight = (ImageView) findViewById(R.id.image_right);
        mTitle = (TextView) findViewById(R.id.title);
        mTextRight = (TextView) findViewById(R.id.text_right);
    }


    public void setImageLeft(int resId, boolean isVisibility) {
        if (isVisibility) {
            mImageLeft.setVisibility(VISIBLE);
            mImageLeft.setImageResource(resId);
        } else {
            mImageLeft.setVisibility(GONE);
        }
    }

    public void setImageLeftListener(OnClickListener listener) {
        mImageLeft.setOnClickListener(listener);
    }

    public void setImageRight(int resId, boolean isVisibility) {
        if (isVisibility) {
            mImageRight.setVisibility(VISIBLE);
            mImageRight.setImageResource(resId);
        } else {
            mImageRight.setVisibility(GONE);
        }
    }

    public void setImageRightListener(OnClickListener listener) {
        mImageRight.setOnClickListener(listener);
    }

    public void setTextRight(CharSequence text, boolean isVisibility) {
        if (isVisibility) {
            mTextRight.setVisibility(VISIBLE);
            mTextRight.setText(text);
        } else {
            mTextRight.setVisibility(GONE);
            mTextRight.setText("");
        }
    }

    public void setTextRightListener(OnClickListener listener) {
        mTextRight.setOnClickListener(listener);
    }

    public void setTitle(CharSequence text, boolean isVisibility) {
        if (isVisibility) {
            mTitle.setVisibility(VISIBLE);
            mTitle.setText(text);
        } else {
            mTitle.setVisibility(GONE);
            mTitle.setText("");
        }
    }


}
