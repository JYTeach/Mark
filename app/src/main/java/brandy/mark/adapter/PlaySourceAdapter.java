package brandy.mark.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import brandy.mark.R;
import brandy.mark.model.PlaySources;
import brandy.mark.model.PlaySourcesFrom;

/**
 * Created by Rock on 2016/8/24.
 */
public class PlaySourceAdapter extends BaseExpandableListAdapter {
    private List<PlaySourcesFrom> data;
    private Context mContext;
    private LayoutInflater mInflater;

    public PlaySourceAdapter(Context context, List<PlaySourcesFrom> data) {
        mContext = context;
        if (data == null) {
            this.data = new ArrayList<>();
        } else {
            this.data = data;
        }
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 组的个数
     *
     * @return
     */
    @Override
    public int getGroupCount() {
        return data != null ? data.size() : 0;
    }

    /**
     * 根据groupPosition返回对应的组的数据
     *
     * @param groupPosition
     * @return
     */
    @Override
    public PlaySourcesFrom getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    /**
     * position对应的组中有多个 child
     *
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).getPlaySources() != null ? getGroup(groupPosition).getPlaySources().size() : 0;
    }

    /**
     * 根据组的位置和子的位置去返回 child数据
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public PlaySources getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).getPlaySources().get(childPosition);
    }

    /**
     * 返回组的id
     *
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 返回 child 的id
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 是否拥有固定的id
     *
     * @return false 和 true 没有实际影响
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 获取组的View
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.playsource_item_group, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TextView groupName = (TextView) holder.getView(R.id.item_group_name);
        groupName.setText(getGroup(groupPosition).getName());

        return convertView;
    }

    /**
     * 获取子的View
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.playsource_item_child, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PlaySources child = getChild(groupPosition, childPosition);

        TextView childName = (TextView) holder.getView(R.id.item_child_name);
        childName.setText(child.getName());
        TextView childIsFree = (TextView) holder.getView(R.id.item_child_isFree);
        if (child.getIs_free() == 1) {
            childIsFree.setText("免费");
        } else if (child.getIs_free() == 0) {
            childIsFree.setText("收费");
        }
        if (!TextUtils.isEmpty(child.getIcon_url())) {
            Picasso.with(mContext)
                    .load(child.getIcon_url())
                    .into(((ImageView) holder.getView(R.id.item_child_icon)));
        }
        return convertView;
    }

    /**
     * child 是否可以被选择(可选择才可以被点击)
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static class ViewHolder {

        private View convertView;
        private Map<Integer, View> mCacheViews;

        public ViewHolder(View convertView) {
            this.convertView = convertView;
            mCacheViews = new HashMap<>();
        }

        public View getView(int resId) {
            View view = null;
            if (mCacheViews.containsKey(resId)) {
                view = mCacheViews.get(resId);
            } else {
                view = convertView.findViewById(resId);
                mCacheViews.put(resId, view);
            }
            return view;
        }
    }


    public void updateRes(List<PlaySourcesFrom> data) {
        if (data != null) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }
}
