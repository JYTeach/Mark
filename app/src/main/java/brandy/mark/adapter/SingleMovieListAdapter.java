package brandy.mark.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import brandy.mark.R;
import brandy.mark.model.AllMovieListData;

/**
 * Created by lenovo on 2016/12/1.
 */
public class SingleMovieListAdapter extends RecyclerView.Adapter<SingleMovieListAdapter.ViewHolder> implements View.OnClickListener {
    public static final int SINGLE_TYPE1 = 1;
    public static final int SINGLE_TYPE2 = 2;
    private AllMovieListData data;
    private Context mContext;
    private LayoutInflater mInflater;

    public SingleMovieListAdapter(AllMovieListData data, Context context) {
        this.data = data;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case SINGLE_TYPE1:
                itemView = mInflater.inflate(R.layout.single_movie_item_type1, parent, false);
                break;
            case SINGLE_TYPE2:
                itemView = mInflater.inflate(R.layout.single_movie_item_type2, parent, false);
                break;
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            Picasso.with(mContext)
                    .load(data.getImg_url())
                    .into(holder.singlemovie_item_type1_img);
            holder.singlemovie_item_type1_tv.setText(data.getName());
        } else {
            holder.singlemovie_item_type2_tv.setText(data.getCat().get(position - 1).getName());
            holder.singlemovie_item_type2_tv.setTag(position - 1);
            holder.singlemovie_item_type2_tv.setOnClickListener(this);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return SINGLE_TYPE1;
        } else {
            return SINGLE_TYPE2;
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? (data.getCat().size() + 1) : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView singlemovie_item_type1_img;
        TextView singlemovie_item_type1_tv;
        TextView singlemovie_item_type2_tv;

        public ViewHolder(View itemView) {
            super(itemView);
            singlemovie_item_type1_img = (ImageView) itemView.findViewById(R.id.singlemovie_item_type1_img);
            singlemovie_item_type1_tv = (TextView) itemView.findViewById(R.id.singlemovie_item_type1_tv);
            singlemovie_item_type2_tv = (TextView) itemView.findViewById(R.id.singlemovie_item_type2_tv);
        }
    }


    @Override
    public void onClick(View v) {
        mOnItemClickListener.onItemClick(((Integer) v.getTag()), mParentTag);
    }

    public interface OnItemClickListener {
        void onItemClick(int childTag, int parentTag);
    }

    private OnItemClickListener mOnItemClickListener;

    private int mParentTag;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener, int parentTag) {
        if (onItemClickListener != null) {
            mOnItemClickListener = onItemClickListener;
        }
        mParentTag = parentTag;
    }
}


