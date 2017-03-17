package com.mark.traveller.ftwy.fragment;

import android.view.View;

import com.mark.traveller.ftwy.FTWYApplication;
import com.mark.traveller.ftwy.R;

/**
 * Created by Mark on 2016/11/16 0016.
 */

public class ProfileFragment extends BaseFragment {
    private static final long DELAY = 3000;
    private View view;
    private String str = "失败后加载成功啦";
    boolean mBoolean = true;

    @Override
    protected View createSuccessView() {
        view = View.inflate(getActivity(), R.layout.fragment_profile, null);

        return view;
    }

    @Override
    protected void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    FTWYApplication.getMainThreadHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (mBoolean) {
                                checkData(null);
                                mBoolean = false;
                            } else {
                                checkData(str);
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
