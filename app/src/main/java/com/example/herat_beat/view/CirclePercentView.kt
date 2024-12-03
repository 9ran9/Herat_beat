package com.example.herat_beat.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.herat_beat.R
import com.example.herat_beat.tool.DensityUtils
import kotlin.math.min



class CirclePercentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private val mCircleColor: Int
    private val mArcColor: Int
    private val mArcWidth: Int
    private val mPercentTextColor: Int
    private val mPercentTextSize: Int
    private val mRadius: Int
    private var mCurPercent = 0.0f
    private val mCirclePaint: Paint
    private val mArcPaint: Paint
    private val mPercentTextPaint: Paint
    private val mTextBound: Rect
    private val mArcRectF: RectF  //弧外界矩形的宽度
    private var taidongCount: String = "0"
    //这个可以变，只是为了做一个全局的它出来。先将就一下。

    //画针：
    private var mHandDegree = 0f //角度
    private val mHandPath: Path//路径
    private var mHandPadding = 0f
    private val mHandPaint: Paint
    private val mHandColor: Int
    private val mHandCircleRectF: RectF
    private var isStart:Boolean = false


    //重来；
    fun replaceCurPercent(){
        mCurPercent = 0.0f
        mHandDegree = 0.0f
        isStart = false
        invalidate()
    }

    //直接把协程数据给到外面，岂不美哉
    fun setPercentTime(percentage: Float =0f) {
        isStart = true
        mCurPercent = percentage
        mHandDegree = 360* mCurPercent / 100
        invalidate()
    }

    fun setTaiDongCount(count:Int){
        taidongCount = count.toString()
        invalidate()
    }
    fun setTaiDongCount(string: String){
        taidongCount = string
        invalidate()
    }


    init {
        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView, defStyleAttr, 0)
        mCircleColor = ta.getColor(R.styleable.CirclePercentView_circleBg, Color.parseColor("#3F51B5"))
        mArcColor = ta.getColor(R.styleable.CirclePercentView_arcColor, Color.parseColor("#000000"))
        mArcWidth = ta.getDimensionPixelSize(
            R.styleable.CirclePercentView_arcWidth,
            DensityUtils.dp2px(context, 16f)
        )
        mPercentTextColor = ta.getColor(R.styleable.CirclePercentView_percentTextColor, Color.parseColor("#000000"))
        mPercentTextSize = ta.getDimensionPixelSize(
            R.styleable.CirclePercentView_percentTextSize,
            DensityUtils.sp2px(context, 16f).toInt()
        )
        mRadius = ta.getDimensionPixelSize(
            R.styleable.CirclePercentView_radius,
            DensityUtils.dp2px(context, 100f)
        )
        mHandColor = ta.getColor(R.styleable.CirclePercentView_HandColor, Color.parseColor("#ffffff"))
        ta.recycle()
        //圆
        mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCirclePaint.style = Paint.Style.FILL
        mCirclePaint.color = mCircleColor

        //圆弧
        mArcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mArcPaint.style = Paint.Style.STROKE
        mArcPaint.strokeWidth = mArcWidth.toFloat()
        mArcPaint.color = mArcColor
        mArcPaint.strokeCap = Paint.Cap.ROUND //使圆弧两头圆滑
        mArcRectF = RectF() //圆弧的外接矩形

        //文字
        mPercentTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPercentTextPaint.style = Paint.Style.FILL_AND_STROKE
        mPercentTextPaint.color = mPercentTextColor
        mPercentTextPaint.textSize = mPercentTextSize.toFloat()
        mTextBound = Rect() //文本的范围矩形

        //指针
        mHandPath = Path()
        mHandPadding = 0.12f * mRadius //根据比例确定HandPadding大小
        mHandPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHandPaint.color = mHandColor
        mHandCircleRectF = RectF()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            measureDimension(widthMeasureSpec),
            measureDimension(heightMeasureSpec)
        )
    }

    private fun measureDimension(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = 2 * mRadius
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        //画圆
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            mRadius.toFloat(),
            mCirclePaint
        )

        //画圆弧
        //矩形边界左，上，右，下 的坐标。
        mArcRectF.set(
            (width / 2 - mRadius + mArcWidth / 2).toFloat(),
            (height / 2 - mRadius + mArcWidth / 2).toFloat(),
            (width / 2 + mRadius - mArcWidth / 2).toFloat(),
            (height / 2 + mRadius - mArcWidth / 2).toFloat()
        )
        //绘制
        canvas.drawArc(mArcRectF, 270f, 360 * mCurPercent / 100, false, mArcPaint)

        //处理文本：
        val text = "有效胎动"
        val textCount = "${taidongCount}次"

        mPercentTextPaint.getTextBounds(textCount,0,textCount.length,mTextBound)
        canvas.drawText(textCount,
            (width / 2 -mTextBound.width() / 2 ).toFloat(),
            (height/2+mTextBound.height()/2 - mPercentTextSize/2 ).toFloat(),
            mPercentTextPaint)//文本框左下角坐标值

        mPercentTextPaint.getTextBounds(text, 0, text.length, mTextBound)
        canvas.drawText(
            text, (width / 2 - mTextBound.width() / 2).toFloat(),
            (height / 2 + mTextBound.height() / 2 + mPercentTextSize).toFloat(),
            mPercentTextPaint
        )
        //画针
        drawHand(canvas)

    }


    //画指针：
    private fun drawHand(canvas: Canvas) {
        canvas.save() //保存画布状态，绘制完成后不影响其他图形的绘制。
        //旋转画布，注：视觉效果是互相的，我也可以通过旋转画布来实现指针转动的效果。

        //用与背景同色来处理指针的显示与隐藏
        if (isStart){
            mHandPaint.color = mHandColor
        }else{
            mHandPaint.color = mCircleColor
        }

        canvas.rotate(mHandDegree, (width / 2).toFloat(), (height / 2).toFloat())
        if (mHandPath.isEmpty) {
            mHandPath.reset()
            val offset = mHandPadding  //可以看请况修改
            mHandPath.moveTo(width / 2 - 0.01f * mRadius, height / 2 - 0.03f * mRadius)
            mHandPath.lineTo(width / 2 - 0.008f * mRadius, offset + 0.365f * mRadius)
            mHandPath.quadTo(
                (width / 2).toFloat(), offset + 0.345f * mRadius,
                width / 2 + 0.008f * mRadius, offset + 0.365f * mRadius
            )
            mHandPath.lineTo(width / 2 + 0.01f * mRadius, height / 2 - 0.03f * mRadius)
            mHandPath.close()  //关闭路径，用直线回到起点。
        }
        mHandPaint.style = Paint.Style.FILL //内部填充。
        canvas.drawPath(mHandPath, mHandPaint)
        mHandCircleRectF.set(
            width / 2 - 0.03f * mRadius,
            height / 2 - 0.03f * mRadius,
            width / 2 + 0.03f * mRadius,
        height / 2 + 0.03f * mRadius
        )
        mHandPaint.style = Paint.Style.STROKE  //边缘填充
        mHandPaint.strokeWidth = 0.02f * mRadius //设置边缘宽度
        canvas.drawArc(mHandCircleRectF, 0f, 360f, false, mHandPaint)
        canvas.restore() //使得画布恢复原来的状态，不影响接下来的绘制
    }

}
