package com.mark.traveller.ftwy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mark.traveller.ftwy.R;
import com.mark.traveller.ftwy.bean.TravellingNewsBean;
import com.mark.traveller.ftwy.utils.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页旅游新闻的adapter
 * Created by Mark on 2016/11/17 0017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private Context mContext;
    private List<TravellingNewsBean.ResultBean> datas = new ArrayList<>();
    private LayoutInflater mInflater;

    public HomeAdapter(Context context, List<TravellingNewsBean.ResultBean> datas) {
        this.mContext = context;
        this.datas = datas;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public HomeAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.list_item_news_base, parent, false);
        HomeViewHolder holder = new HomeViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(HomeAdapter.HomeViewHolder holder, int position) {
        // 设置图片
        Picasso.with(mContext).load(datas.get(position).picUrl).fit().priority(Picasso.Priority.HIGH).into(holder.mImg);
        // 设置标题
        holder.mTitle.setText(datas.get(position).title);
        // 设置来源
        holder.mDesc.setText(datas.get(position).description);
        // 设置时间
        holder.mTime.setText(datas.get(position).ctime);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mImg;// 图片
        private final TextView mTitle;// 标题
        private final TextView mDesc;// 新闻来源
        private final TextView mTime;// 时间

        RelativeLayout mRootTravelling;

        public HomeViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.img_travelling);
            mTitle = (TextView) itemView.findViewById(R.id.tv_title_travelling);
            mDesc = (TextView) itemView.findViewById(R.id.tv_description_travelling);
            mTime = (TextView) itemView.findViewById(R.id.tv_date_travelling);

            mRootTravelling = (RelativeLayout) itemView.findViewById(R.id.root_layout_travelling);

            mRootTravelling.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }

    // 声明一个接口变量
    private OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
