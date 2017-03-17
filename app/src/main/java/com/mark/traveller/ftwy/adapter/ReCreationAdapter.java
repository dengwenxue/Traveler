package com.mark.traveller.ftwy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mark.traveller.ftwy.R;
import com.mark.traveller.ftwy.bean.FilmBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 娱乐页面Adapter
 * Created by Mark on 2016/11/19 0019.
 */

public class ReCreationAdapter extends RecyclerView.Adapter<ReCreationAdapter.RecreationViewHolder> {

    private Context mContext;
    private List<FilmBean.DataBean.MoviesBean> mData;
    private LayoutInflater mInflater;

    /**
     * 构造方法
     *
     * @param context    上下文
     * @param moviesData 电影信息数据
     */
    public ReCreationAdapter(Context context, List<FilmBean.DataBean.MoviesBean> moviesData) {
        this.mContext = context;
        this.mData = moviesData;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ReCreationAdapter.RecreationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View viewBody = mInflater.inflate(R.layout.list_item_recreation, parent, false);
        RecreationViewHolder holder = new RecreationViewHolder(viewBody);
        return holder;

    }

    @Override
    public void onBindViewHolder(ReCreationAdapter.RecreationViewHolder holder, int position) {
        //其他条目中的逻辑在此
        // 设置标题
        holder.getTitleTextView().setText(mData.get(position).nm);
        // 设置图片
        Picasso.with(mContext).load(mData.get(position).img).fit().priority(Picasso.Priority.HIGH).into(holder.getImageView());
        // 主演
        holder.getActorTextView().setText("主演:" + mData.get(position).star);
    }

    /**
     * 总条目数量是数据源数量+
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class RecreationViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private ImageView mImageView;
        private TextView mActorTextView;

        public RecreationViewHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.card_title);
            mImageView = (ImageView) itemView.findViewById(R.id.item_recreation_iamge);
            mActorTextView = (TextView) itemView.findViewById(R.id.card_actor);
        }

        public TextView getTitleTextView() {
            return mTitleTextView;
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public TextView getActorTextView() {
            return mActorTextView;
        }
    }

}
