package brandy.mark.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import brandy.mark.R;
import brandy.mark.custom.CyclicPageView;
import brandy.mark.model.DisBanner;
import brandy.mark.model.DisMovie;


public class DisRecyclerAdapter extends RecyclerView.Adapter<DisRecyclerAdapter.ViewHolder> implements CyclicPageView.OnLoadImageListener, View.OnClickListener {
    private static final int TYPE_1 = 1;
    private static final int TYPE_2 = 2;
    private static final int TYPE_3 = 3;
    private static final int TYPE_4 = 4;
    private static final int TYPE_5 = 5;
    private static final String TAG = DisRecyclerAdapter.class.getSimpleName();

    private List<DisBanner> mBanners;
    private List<DisMovie> mMovies;
    private LayoutInflater mInflater;
    private Context mContext;

    public DisRecyclerAdapter(List<DisBanner> banners, List<DisMovie> movies, Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        if (banners == null && movies == null) {
            mBanners = new ArrayList<>();
            mMovies = new ArrayList<>();
        } else {
            mBanners = banners;
            mMovies = movies;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType) {
            case TYPE_1:
                itemView = mInflater.inflate(R.layout.dis_item_type1, parent, false);
                break;
            case TYPE_2:
                itemView = mInflater.inflate(R.layout.dis_item_type2, parent, false);
                break;
            case TYPE_3:
                itemView = mInflater.inflate(R.layout.dis_item_type3, parent, false);
                break;
            case TYPE_4:
                itemView = mInflater.inflate(R.layout.dis_item_type4, parent, false);
                break;
            case TYPE_5:
                itemView = mInflater.inflate(R.layout.dis_item_type5, parent, false);
                break;
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            holder.dis_item_type1_viewPager.initImageView(R.layout.recycler_viewpager,
                    mBanners.size(), this);
        } else if (position == 1) {
            holder.dis_item_type2_left.setTag(-1);
            holder.dis_item_type2_center.setTag(-2);
            holder.dis_item_type2_right.setTag(-3);
            holder.dis_item_type2_left.setOnClickListener(this);
            holder.dis_item_type2_center.setOnClickListener(this);
            holder.dis_item_type2_right.setOnClickListener(this);
        } else if (position == getItemCount() - 1) {


        } else {
            if (mMovies.get(position).getCat_name().equals("")) {
                holder.dis_item_type3_relative.setTag(position);
                holder.dis_item_type3_relative.setOnClickListener(this);
                holder.dis_item_type3_title.setText(mMovies.get(position).getName());
                holder.dis_item_type3_like_count.setText(mMovies.get(position).getLikes() + "");
                if (mMovies.get(position).getIs_liked().equals("0")) {
                    holder.dis_item_type3_like.setImageResource(R.mipmap.discover_like_unchecked);
                } else if (mMovies.get(position).getIs_liked().equals("1")) {
                    holder.dis_item_type3_like.setImageResource(R.mipmap.discover_like_checked);
                }
                Picasso.with(holder.dis_item_type3_image.getContext())
                        .load(mMovies.get(position).getImg_url())
                        .fit()
                        .placeholder(R.mipmap.movie_empty_icon)
                        .into(holder.dis_item_type3_image);
            } else {
                holder.dis_item_type4_relative.setTag(position);
                holder.dis_item_type4_relative.setOnClickListener(this);
                holder.dis_item_type4_title.setText(mMovies.get(position).getName());
                holder.dis_item_type4_like_count.setText(mMovies.get(position).getLikes() + "");
                if (mMovies.get(position).getIs_liked().equals("0")) {
                    holder.dis_item_type4_like.setImageResource(R.mipmap.discover_like_unchecked);
                } else if (mMovies.get(position).getIs_liked().equals("1")) {
                    holder.dis_item_type4_like.setImageResource(R.mipmap.discover_like_checked);
                }
                Picasso.with(holder.dis_item_type4_image.getContext())
                        .load(mMovies.get(position).getImg_url())
                        .fit()
                        .placeholder(R.mipmap.movie_empty_icon)
                        .into(holder.dis_item_type4_image);
                holder.dis_item_type4_catname.setText("# " + mMovies.get(position).getCat_name());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_1;
        } else if (position == 1) {
            return TYPE_2;
        } else if (position == getItemCount() - 1) {
            return TYPE_5;
        } else {
            if (mMovies.get(position).getCat_name().equals("")) {
                return TYPE_3;
            } else {
                return TYPE_4;
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mBanners != null && mMovies != null) {
            count = 1 + 1 + mMovies.size() - 2 + 1;
        }
        return count;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CyclicPageView dis_item_type1_viewPager;
        private Button dis_item_type2_left;
        private Button dis_item_type2_center;
        private Button dis_item_type2_right;
        private RelativeLayout dis_item_type3_relative;
        private ImageView dis_item_type3_image;
        private ImageView dis_item_type3_new;
        private TextView dis_item_type3_title;
        private ImageView dis_item_type3_like;
        private TextView dis_item_type3_like_count;
        private RelativeLayout dis_item_type4_relative;
        private ImageView dis_item_type4_image;
        private ImageView dis_item_type4_new;
        private TextView dis_item_type4_title;
        private TextView dis_item_type4_catname;
        private ImageView dis_item_type4_like;
        private TextView dis_item_type4_like_count;
        private LinearLayout load;

        public ViewHolder(View itemView) {
            super(itemView);

            dis_item_type1_viewPager = (CyclicPageView) itemView.findViewById(R.id.dis_item_type1_viewPager);
            dis_item_type2_left = (Button) itemView.findViewById(R.id.dis_item_type2_left);
            dis_item_type2_center = (Button) itemView.findViewById(R.id.dis_item_type2_center);
            dis_item_type2_right = (Button) itemView.findViewById(R.id.dis_item_type2_right);
            dis_item_type3_relative = (RelativeLayout) itemView.findViewById(R.id.dis_item_type3_relative);
            dis_item_type3_image = (ImageView) itemView.findViewById(R.id.dis_item_type3_image);
            dis_item_type3_new = (ImageView) itemView.findViewById(R.id.dis_item_type3_new);
            dis_item_type3_title = (TextView) itemView.findViewById(R.id.dis_item_type3_title);
            dis_item_type3_like = (ImageView) itemView.findViewById(R.id.dis_item_type3_like);
            dis_item_type3_like_count = (TextView) itemView.findViewById(R.id.dis_item_type3_like_count);
            dis_item_type4_relative = (RelativeLayout) itemView.findViewById(R.id.dis_item_type4_relative);
            dis_item_type4_image = (ImageView) itemView.findViewById(R.id.dis_item_type4_image);
            dis_item_type4_new = (ImageView) itemView.findViewById(R.id.dis_item_type4_new);
            dis_item_type4_title = (TextView) itemView.findViewById(R.id.dis_item_type4_title);
            dis_item_type4_catname = (TextView) itemView.findViewById(R.id.dis_item_type4_catname);
            dis_item_type4_like = (ImageView) itemView.findViewById(R.id.dis_item_type4_like);
            dis_item_type4_like_count = (TextView) itemView.findViewById(R.id.dis_item_type4_like_count);
            load = (LinearLayout) itemView.findViewById(R.id.load);
        }

    }

    public void updateRes(List<DisBanner> banners, List<DisMovie> movies) {
        if (banners != null) {
            mBanners.clear();
            mBanners.addAll(banners);
        }
        if (movies != null) {
            mMovies.clear();
            mMovies.addAll(movies);
        }
        //注意这里应该判断size
        if (mBanners.size() != 0 && mMovies.size() != 0) {
            notifyDataSetChanged();
        }
    }

    public void addRes(List<DisMovie> movies) {
        if (movies != null) {
            mMovies.addAll(movies);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadImage(ImageView imageView, int position) {
        Picasso.with(imageView.getContext())
                .load(mBanners.get(position).getImg_url())
                .into(imageView);
        //imageView的tag从-4开始
        imageView.setTag(-3 - mBanners.size() + position);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Integer tag = (Integer) v.getTag();
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(tag);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int tag);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null) {
            mOnItemClickListener = onItemClickListener;
        }
    }
}
