package com.mark.traveller.ftwy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mark.traveller.ftwy.widget.LoadPager;

import java.util.List;

/**
 * Created by Mark on 2016/11/17 0009.
 */
public abstract class BaseFragment extends Fragment {

    protected LoadPager mLoadPager = null;
    protected Context mActivity;

    protected ViewGroup mContainer;
    protected LayoutInflater mInflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContainer = container;
        mInflater = inflater;

        if (mLoadPager == null) {
            mLoadPager = new LoadPager(mActivity) {
                @Override
                public View createSuccessView() {
                    return BaseFragment.this.createSuccessView();
                }

                @Override
                protected void load() {
                    BaseFragment.this.loadData();
                }
            };
        }

        return mLoadPager;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        show();
    }

    public void show() {
        if (mLoadPager != null) {
            mLoadPager.show();
        }
    }

    /**
     * 必须在获取到数据后调用checkData方法
     * 该方法一般为耗时的操作,需要在子线程进行处理
     */
    protected abstract void loadData();

    protected abstract View createSuccessView();


    /**
     * 校验数据
     */
    protected void checkData(Object datas) {
        if (datas == null) {
            mLoadPager.showPage(LoadPager.STATE_ERROR);//  请求服务器失败
        } else {
            try {
                @SuppressWarnings("unchecked") List<Object> ds = (List<Object>) datas;
                if (ds.size() == 0) {
                    mLoadPager.showPage(LoadPager.STATE_EMPTY);
                } else {
                    mLoadPager.showPage(LoadPager.STATE_SUCCESS);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if ("".equals(datas)) {
                    mLoadPager.showPage(LoadPager.STATE_EMPTY);
                } else {
                    mLoadPager.showPage(LoadPager.STATE_SUCCESS);
                }
            }
        }

    }
}
