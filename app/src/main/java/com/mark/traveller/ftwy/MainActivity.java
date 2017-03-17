package com.mark.traveller.ftwy;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.baidu.mapapi.map.SupportMapFragment;
import com.mark.traveller.ftwy.fragment.HomeFragment;
import com.mark.traveller.ftwy.fragment.ProfileFragment;
import com.mark.traveller.ftwy.fragment.RecreationFragment;
import com.mark.traveller.ftwy.fragment.SearchFragment;
import com.mark.traveller.ftwy.utils.ToastUtils;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Toolbar mToolbar;
    private RadioGroup mRadioGroup;
    private List<Fragment> mFragments;
    private long mOPTime;

    //SupportMapFragment类, 管理地图生命周期
    SupportMapFragment map;

    // 广告图片
    // String img1 = "http://a1.att.hudong.com/30/32/19300001024098134992322076458.jpg";
    String img1 = "https://timgsa.baidu.com/timg?" +
            "image&quality=80&size=b9999_10000&sec=1488333168992&di=56f727db77013a8295cee8ff858609dc&imgtype=0&src=http%3A%2F%2Ffile25.mafengwo.net%2FM00%2F56%2F28%2FwKgB4lJ96lOALdyLAA4RRpNZVDk47.jpeg";
    // String img2 = "http://twimg.edgesuite.net/images/ReNews/20150819/640_a48504cd68fa5fc7519dadfa9c886d42.jpg";
    String img2 = "https://timgsa.baidu.com/" +
            "timg?image&quality=80&size=b9999_10000&sec=1488333781549&di=4b177c37c35a010646c7178838d35dc5&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fitbbs%2F1308%2F14%2Fc38%2F24484306_1376485424236_mthumb.jpg";
    // String img3 = "http://ww2.sinaimg.cn/mw690/ee25a406tw1epb02cyg33j20i20azdhc.jpg";
    String img3 = "https://timgsa.baidu.com/" +
            "timg?image&quality=80&size=b9999_10000&sec=1488333941883&di=7eba3b8490ac5d30404d06fcf8113e24&imgtype=0&src=http%3A%2F%2Fwww.shenzhenu.com%2Fuploads%2Fallimg%2F100623%2F1_100623113634_1.jpg";
    // String img4 = "http://nicepic2u.com/wp-content/uploads/2015/01/wonder-girls-1-510x300.jpg";
    String img4 = "https://timgsa.baidu.com/" +
            "timg?image&quality=80&size=b9999_10000&sec=1488928778&di=610b9b5004822123ca5a28357ecc7f3e&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.baike.soso.com%2Fp%2F20131221%2F20131221024727-1091387659.jpg";
    List<String> imagesUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();

        setupBanner();

        setupFragment();

        setupCollapsingToolbar();

        setupDrawer();

        setupFloatingActionButton();

        setupNavigationView();


    }

    /**
     * 设置广告轮播
     */
    private void setupBanner() {

        // 添加图片
        imagesUrl.add(img1);
        imagesUrl.add(img2);
        imagesUrl.add(img3);
        imagesUrl.add(img4);

        Banner banner = (Banner) findViewById(R.id.banner);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imagesUrl);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        // banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

    }

    /**
     * 设置Fragment与RadioGroup的关联
     */
    private void setupFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomeFragment());
        mFragments.add(new RecreationFragment());
        mFragments.add(new SearchFragment());
        mFragments.add(new ProfileFragment());

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);

        // 底部设置默认的选项
        mRadioGroup.check(R.id.bottom_home);

        // 设置默认显示的fragment

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragments, new HomeFragment());
        transaction.addToBackStack(null);
        transaction.commit();

        /**
         * MapStatus:定义地图状态
         * MapStatus.Builder:地图状态构造器
         * MapStatus.Builder.build():创建地图状态对象
         * overlook:地图俯仰角度
         * zoom:地图缩放级别 3~20
         * */
        // MapStatus ms = new MapStatus.Builder().overlook(-20).zoom(15).build();
        /**
         * BaiduMapOptions:MapView 初始化选项
         * mapStatus(MapStatus status):设置地图初始化时的地图状态， 默认地图中心点为北京天安门
         * compassEnabled(boolean enabled):设置是否允许指南针，默认允许。
         * zoomControlsEnabled(boolean enabled):设置是否显示缩放控件
         * */
        //BaiduMapOptions bo = new BaiduMapOptions().mapStatus(ms).compassEnabled(false).zoomControlsEnabled(false);

        /**
         * newInstance(BaiduMapOptions options):
         *                 根据给定的百度地图选项创建一个SupportMapFragment实例
         * */
        //map = SupportMapFragment.newInstance(bo);

        // RadioGroup与fragment相关联
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                if (checkedId == R.id.bottom_home) {
                    ft.replace(R.id.fragments, mFragments.get(0));
                } else if (checkedId == R.id.bottom_recreation) {
                    ft.replace(R.id.fragments, mFragments.get(1));
                } else if (checkedId == R.id.bottom_search) {
                    ft.replace(R.id.fragments, mFragments.get(2));
                    //ft.add(R.id.fragments, map, "map_fragment");
                } else if (checkedId == R.id.bottom_profile) {
                    ft.replace(R.id.fragments, mFragments.get(3));
                }

                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    /**
     * 设置NavigationView
     */
    private void setupNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * 设置FloatingActionButton
     */
    private void setupFloatingActionButton() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    /**
     * 设置抽屉
     */
    private void setupDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }

    /**
     * 设置图片与ToolBar的效果
     */
    private void setupCollapsingToolbar() {
        CollapsingToolbarLayout collapsingToolBar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        collapsingToolBar.setTitleEnabled(false);
    }

    /**
     * 设置Toolbar
     */
    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("驴 友");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Picasso 加载图片简单用法
            Picasso.with(context).load((String) path).fit().priority(Picasso.Priority.HIGH).into(imageView);
        }

        //提供createImageView 方法，如果不用可以不重写这个方法，方便fresco自定义ImageView
        @Override
        public ImageView createImageView(Context context) {
            return super.createImageView(context);
        }
    }

    /**
     * 覆盖返回键,按2次返回键退出应用
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mOPTime) > 2000) {
                ToastUtils.showToast(MainActivity.this, "再按一次返回桌面");
                mOPTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
