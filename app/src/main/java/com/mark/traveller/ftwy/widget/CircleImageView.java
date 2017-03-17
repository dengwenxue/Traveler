package com.mark.traveller.ftwy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.mark.traveller.ftwy.R;

/**
 * Created by mark on 2017/2/24.
 */

class CircleImageView_mytest extends ImageView {

    private Matrix mMatrix;//图片变换处理器-用来缩放图片以适应view控件的大小
    private Paint mPaintCircle;//画圆形图像的笔
    private int mCircleBorderWidth;
    private int mCircleBorderColor;
    private int mCircleBackgroundColor;
    private Paint mPaintBorder;//画圆形边界的笔
    private Paint mPaintBackground;
    private BitmapShader mBitmapShader;
    private int mWidth;
    private int mHeight;
    private int mRadius;

    // 重写3个基本构造方法
    public CircleImageView_mytest(Context context) {
        super(context);
    }

    public CircleImageView_mytest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView_mytest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义样式属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0);

        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                // 边界宽度
                case R.styleable.CircleImageView_circleImageViewWidth:
                    mCircleBorderWidth = (int) array.getDimension(attr, 0);

                    break;

                // 边界的颜色
                case R.styleable.CircleImageView_circleColor:
                    mCircleBorderColor = array.getColor(attr, Color.WHITE);
                    break;

                // 圆形头像北京颜色
                case R.styleable.CircleImageView_backgroundColor:
                    mCircleBackgroundColor = array.getColor(attr, Color.GREEN);
                    break;
            }
        }

        // 回收
        array.recycle();

        // 初始化画笔
        init();
    }

    /**
     * 初始化画笔
     */
    private void init() {
        //初始化图片变换处理器
        mMatrix = new Matrix();

        // 画圆的笔
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);//抗锯齿,没有消除锯齿的话，图片变换会很难看的，因为有些像素点会失真
        mPaintCircle.setStrokeWidth(12);//设置圆边界宽度
        //附加效果设置阴影
        this.setLayerType(LAYER_TYPE_SOFTWARE, mPaintCircle);
        mPaintCircle.setShadowLayer(13.0f, 5.0f, 5.0f, Color.GRAY);

        // 绘制图形边框的笔
        mPaintBorder = new Paint();
        mPaintBorder.setAntiAlias(true);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setStrokeWidth(mCircleBorderWidth);
        mPaintBorder.setColor(mCircleBorderColor);

        // 绘制背景的笔
        mPaintBackground = new Paint();
        mPaintBackground.setAntiAlias(true);
        mPaintBackground.setColor(mCircleBackgroundColor);
        mPaintBackground.setStyle(Paint.Style.FILL);
    }

    /**
     * 重写onMeasure
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 在这里设置高度宽度，以设置的较小值为准，防止不成圆
        mWidth = getWidth();
        mHeight = getHeight();
        int circleSize = Math.min(mWidth, mHeight);

        //圆的半径短的二分之一作为半径
        mRadius = circleSize / 2 - mCircleBorderWidth;
    }


    /**
     * 我们可以知道，如果我们直接用imageview然后引用shape弄成圆形的话，
     * 意味着我们在这个imageview的逻辑只能写在fragment等等里面了，而很难去进行逻辑的分层.。
     * 而且！！只能用矢量图并且美工要配合你。因此我们重写imageview就是为了更好地封装好点击imageview的逻辑
     * 一、而怎么重新定义画布呢，就是重写onDraw然后在他继承父类方法属性前重新定义画布，也就是在super方法前面啦！！
     * 可是，这个方法涉及到渲染多层，很容易oom
     * 二、然而我们将用另一种方法，使用BitmapShader画圆形的，只要把bitmap传进去，然后把Matrix也传进去作为图片缩放的工具
     * <p>
     * Canvas理解成系统提供给我们的一块内存区域(但实际上它只是一套画图的API，真正的内存是下面的Bitmap)，
     * 而且它还提供了一整套对这个内存区域进行操作的方法，所有的这些操作都是画图API。
     * 也就是说在这种方式下我们已经能一笔一划或者使用Graphic来画我们所需要的东西了，要画什么要显示什么都由我们自己控制。
     * <p>
     * 重写onDraw
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {


        if (getDrawable() != null) {
            setBitmapShader();
            canvas.drawRect(0, 0, mWidth, mHeight, mPaintBackground);//直接构造，画背景的，为什么画背景？因为画布是方的，市面上所有圆形头像都是没有直接处理边角的，而是用Framelayout来进去覆盖，所以这里定义个背景色告诉大家，当然也封装好给大家使用
            canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaintCircle);
            //画边框
            canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius + mCircleBorderWidth / 2, mPaintBorder);
        } else {
            //如果在xml中这个继承ImageView的类没有被set图片就用默认的ImageView方案咯
            super.onDraw(canvas);
        }
    }

    //使用BitmapShader画圆图形
    private void setBitmapShader() {
        //将图片转换成Bitmap
        Drawable drawable = getDrawable();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        //将bitmap放进图像着色器，后面两个模式是x，y轴的缩放模式，CLAMP表示拉伸
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //初始化图片与view之间伸缩比例，因为比例一般非整数，所以用float，免得精度丢失
        float scale = 1.0f;
//        float scaleX = 1.0f;
        //将图片的宽度高度的最小者作为图片的边长，用来和view来计算伸缩比例
//        int bitmapSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int bitmapSize = Math.min(bitmap.getHeight(), bitmap.getWidth());


        //        int bitmapSizeX = bitmap.getWidth();
        //      scaleX = mWidth * 1.0f / bitmapSizeX;
        /**注意这里，我使用的是图片最长的（就是宽度）来伸缩，那么用这个的话，
         * 我们就会发现，较短的那边（就是高度）在经过Matrix的拉伸后会发现失真，强行地被拉长，
         * 一、因为图片为了适应最长的那边可以完全在view上展示，把长的给压缩了，而短的比长的那边短，所以要强行拉伸，那么就会导致短的这边被拉伸时候失真
         *二、因为图像的变换是针对每一个像素点的，所以有些变换可能发生像素点的丢失，
         * 这里需要使用Paint.setAnitiAlias(boolean)设置来消除锯齿，这样图片变换后的效果会好很多。

         */

        //计算缩放比例，view的大小和图片的大小比例
        scale = mWidth * 1.0f / bitmapSize;
        //利用这个图像变换处理器，设置伸缩比例，长宽以相同比例伸缩
        mMatrix.setScale(scale, scale);
        //给那个图像着色器设置变换矩阵，绘制时就根据view的size，设置图片的size
        //使图片的较小的一边缩放到view的大小一致，这样就可以避免图片过小导致CLAMP拉伸模式或过大导致显示不全
        mBitmapShader.setLocalMatrix(mMatrix);
        //为画笔套上一个Shader的笔套
        mPaintCircle.setShader(mBitmapShader);
    }
}
