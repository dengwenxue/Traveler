package com.mark.traveller.ftwy.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mark.traveller.ftwy.FTWYApplication;
import com.mark.traveller.ftwy.R;
import com.mark.traveller.ftwy.adapter.HomeAdapter;
import com.mark.traveller.ftwy.bean.TravellingNewsBean;
import com.mark.traveller.ftwy.ui.TravellingNewsUI;
import com.mark.traveller.ftwy.utils.CacheUtils;
import com.mark.traveller.ftwy.utils.DataConstants;
import com.mark.traveller.ftwy.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import static com.mark.traveller.ftwy.FTWYApplication.mContext;

/**
 * 首页
 * <p>
 * Created by Mark on 2016/11/16 0016.
 */

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final long DELAY = 2000;
    private static final String LOG_TAG = HomeFragment.class.getSimpleName();

    private View view;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private TravellingNewsBean mNewsBean;
    private List<TravellingNewsBean.ResultBean> mData = new ArrayList<>();
    private HomeAdapter mAdapter;

    @Override
    protected View createSuccessView() {
        view = View.inflate(getActivity(), R.layout.fragment_home, null);

        init();

        setupSwipeRefreshLayout();

        setupRecyclerView();

        return view;
    }

    /**
     * 设置下拉刷新属性
     */
    private void setupSwipeRefreshLayout() {
        // 设置下拉刷新属性
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        mRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * 显示RecyclerView
     */
    private void setupRecyclerView() {
        // 布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(manager);
        // 设置布局的位置
        manager.setOrientation(OrientationHelper.VERTICAL);

        // 设置adapter
        mAdapter = new HomeAdapter(mContext, mData);
        mRecyclerView.setAdapter(mAdapter);

        // 设置分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        // 设置增加或删除条目的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // 设置点击事件
        mAdapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String newsUrl = mData.get(position).url;

                Intent intent = new Intent(mContext, TravellingNewsUI.class);
                intent.putExtra(DataConstants.TRAVL_DATA, newsUrl);
                startActivity(intent);
            }
        });

    }

    /**
     * 初始化控件
     */
    private void init() {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
    }


    @Override
    protected void loadData() {

        // 加载数据

        // 获取缓存
        String cache = CacheUtils.getCache(DataConstants.TRAVEL_INTERFACE, mContext);
        // 解析缓存
        if (!TextUtils.isEmpty(cache)) {
            parseData(cache);
        }
        // 没有缓存,去服务器获取数据
        getJsonString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FTWYApplication.getMainThreadHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkData(mData);
                    }
                }, DELAY);
            }
        }).start();
    }

    /**
     * 去服务器获取json数据
     */
    private void getJsonString() {
        // Volley进行网络数据的访问
        // 1.创建一个RequestQueue
        RequestQueue queue = Volley.newRequestQueue(mContext);
        // 2.创建一个StringRequest对象
        StringRequest stringRequest = new StringRequest(DataConstants.TRAVEL_INTERFACE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // s为返回的json数据
                // 解析数据
                parseData(response);
                // 缓存
                CacheUtils.setCache(DataConstants.TRAVEL_INTERFACE, response, mContext);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(LOG_TAG, volleyError + "");
            }
        });

        queue.add(stringRequest);
    }

    /**
     * 解析json数据
     *
     * @param jsonStr
     */
    private void parseData(String jsonStr) {
        Gson gson = new Gson();
        mNewsBean = gson.fromJson(jsonStr, TravellingNewsBean.class);

        mData = mNewsBean.result;
    }

    @Override
    public void onRefresh() {
        FTWYApplication.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 刷新的逻辑
                mRefreshLayout.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
            }
        }, DELAY);
    }
}
