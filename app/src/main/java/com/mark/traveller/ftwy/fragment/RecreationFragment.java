package com.mark.traveller.ftwy.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mark.traveller.ftwy.FTWYApplication;
import com.mark.traveller.ftwy.R;
import com.mark.traveller.ftwy.adapter.FilmAdapter;
import com.mark.traveller.ftwy.bean.FilmBean;
import com.mark.traveller.ftwy.utils.CacheUtils;
import com.mark.traveller.ftwy.utils.DataConstants;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * 娱乐页面
 * <p>
 * Created by Mark on 2016/11/16 0016.
 */

public class RecreationFragment extends BaseFragment implements MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, SwipeRefreshLayout.OnRefreshListener {
    private static final long DELAY = 2000;
    private String str = "";
    private View mView;
    private VideoView mVideoView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mDownloadRate;
    private TextView mLoadRate;
    private String mVideoPath;
    private List<FilmBean.DataBean.MoviesBean> moviesData = new ArrayList<>();
    private FilmAdapter mAdapter;

    /**
     * 加载网络数据
     */
    @Override
    protected void loadData() {
        // 三级缓存
        // 1.获取缓存
        String cache = CacheUtils.getCache(DataConstants.MAOYAN_API, mActivity);

        // 2.缓存不为空，处理缓存数据
        if (!TextUtils.isEmpty(cache)) {
            parseJsonData(cache);
        }

        // 3.缓存不存在,直接获取数据
        getNetData();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(DELAY);
                    FTWYApplication.getMainThreadHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            checkData(mVideoPath);
                            checkData(moviesData);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * okHttp获取网络数据
     * 获取网络数据
     */
    private void getNetData() {
        // 创建okHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        // 创建一个Request
        Request request = new Request.Builder().url(DataConstants.MAOYAN_API)
                .build();
        // new call
        Call call = okHttpClient.newCall(request);

        // 请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // 请求失败
                Toast.makeText(mActivity, "网络请求失败,请检查网络连接...", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // 请求成功
                String result = response.body().string();

                // 缓存数据
                CacheUtils.setCache(DataConstants.MAOYAN_API, result, mActivity);

                // 业务逻辑
                parseJsonData(result);

                // 刷新adapter
                // mAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 处理json数据
     *
     * @param jsonStr
     */
    private void parseJsonData(String jsonStr) {
        Gson gson = new Gson();
        FilmBean filmBean = gson.fromJson(jsonStr, FilmBean.class);
        moviesData = filmBean.data.movies;

        System.out.println("data:" + moviesData.get(0).img);

//        // 设置数据
//        mAdapter = new FilmAdapter(mActivity, moviesData);
//        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected View createSuccessView() {
        mView = View.inflate(mActivity, R.layout.fragment_recreation, null);
        mVideoPath = DataConstants.VIDEO;

        init();

        setupVideoView();

        setupContent();

        return mView;
    }

    /**
     * 猫眼电影咨询
     */
    private void setupContent() {
        // SwipRefreshLayout
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        mRefreshLayout.setOnRefreshListener(this);

        // RecyclerView
        GridLayoutManager manager = new GridLayoutManager(mActivity, 2);
        mRecyclerView.setLayoutManager(manager);

        // 设置数据
        mAdapter = new FilmAdapter(mActivity, moviesData);
        mRecyclerView.setAdapter(mAdapter);
    }

    // 播放视频
    private void setupVideoView() {
        if (Vitamio.isInitialized(mActivity)) {
            if (TextUtils.isEmpty(mVideoPath)) {
                // Tell the user to provide a media file URL/path.
                Toast.makeText(getActivity(), "Please edit VideoBuffer Activity, and set path" + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
                return;
            } else {
                /*
                 * Alternatively,for streaming media you can use
                 * mVideoView.setVideoURI(Uri.parse(URLstring));
                 */

                Uri uri = Uri.parse(mVideoPath);
                mVideoView.setVideoURI(uri);
                mVideoView.setMediaController(new MediaController(mActivity));
                mVideoView.requestFocus();

                mVideoView.setOnInfoListener(this);
                mVideoView.setOnBufferingUpdateListener(this);

                mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.pause();
                    }
                });
            }
        }
    }

    /**
     * 初始化控件
     */
    private void init() {
        mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.fragment_recreation_refresh);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.fragment_recreation_recyclerview);

        mVideoView = (VideoView) mView.findViewById(R.id.fragment_recreation_videoview);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.probar);// 进度条
        mDownloadRate = (TextView) mView.findViewById(R.id.download_rate);//显示缓冲百分比的TextView
        mLoadRate = (TextView) mView.findViewById(R.id.load_rate);//显示下载网速的TextView
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mProgressBar.setVisibility(View.VISIBLE);
                    mDownloadRate.setText("");
                    mLoadRate.setText("");
                    mDownloadRate.setVisibility(View.VISIBLE);
                    mLoadRate.setVisibility(View.VISIBLE);
                }
                break;

            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                mVideoView.start();
                mProgressBar.setVisibility(View.GONE);
                mDownloadRate.setVisibility(View.GONE);
                mLoadRate.setVisibility(View.GONE);
                break;

            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                mDownloadRate.setText("" + extra + "kb/s" + "  ");
                break;
        }
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mDownloadRate.setText(percent + "%");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //
        // mVideoView.stopPlayback();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);

                // 业务逻辑
                mAdapter.notifyDataSetChanged();
            }
        }, DELAY);
    }
}