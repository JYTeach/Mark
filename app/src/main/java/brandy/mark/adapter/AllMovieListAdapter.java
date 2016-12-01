package brandy.mark.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import brandy.mark.R;
import brandy.mark.model.AllMovieListData;

/**
 * Created by lenovo on 2016/12/1.
 */
public class AllMovieListAdapter extends RecyclerView.Adapter<AllMovieListAdapter.ViewHolder> implements SingleMovieListAdapter.OnItemClickListener {

    private List<AllMovieListData> data;
    private Context mContext;
    private LayoutInflater mInflater;

    public AllMovieListAdapter(List<AllMovieListData> data, Context context) {
        if (data == null) {
            this.data = new ArrayList<>();
        } else {
            this.data = data;
        }
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.all_movie_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        holder.allmovie_item_recycler.setLayoutManager(layout);
        SingleMovieListAdapter adapter = new SingleMovieListAdapter(data.get(position), mContext);
        holder.allmovie_item_recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(this, position);

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView allmovie_item_recycler;

        public ViewHolder(View itemView) {
            super(itemView);
            allmovie_item_recycler = (RecyclerView) itemView.findViewById(R.id.allmovie_item_recycler);
        }
    }

    public void updateRes(List<AllMovieListData> data) {
        if (data != null) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int childTag, int parentTag);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null) {
            mOnItemClickListener = onItemClickListener;
        }
    }

    @Override
    public void onItemClick(int childTag, int parentTag) {
        mOnItemClickListener.onItemClick(childTag, parentTag);
    }

}
