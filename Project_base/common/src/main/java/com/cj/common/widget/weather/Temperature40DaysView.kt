package com.cj.common.widget.weather

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import com.cj.common.R
import com.cj.common.bean.WeatherDaysInfo
import com.cj.framework.bean.Point2D
import com.cj.framework.utils.*
import kotlin.math.ceil
import kotlin.math.max

/**
 * 40天温度变化曲线表
 *
 * @author CJ
 * @date 2021/5/18 11:17
 */
class Temperature40DaysView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    HorizontalScrollView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    companion object {
        private const val TAG = "Temperature40DaysView"

        private const val DEFAULT_POPUP_WINDOW_TEXT_COLOR = Color.WHITE
        private val DEFAULT_TEMPERATURE_TEXT_COLOR = getColor(R.color.text_color_black)
        private val DEFAULT_DATE_TEXT_COLOR = getColor(R.color.text_color_gry)
        private val DEFAULT_TEMPERATURE_LINE_CHART_COLOR = getColor(R.color.sky_blue)
        private val DEFAULT_TEMPERATURE_POPUP_WINDOW_COLOR = getColor(R.color.sky_blue)
        private val DEFAULT_TIME_LINE_COLOR = getColor(R.color.time_line_color)
        private val DEFAULT_CURRENT_TIME_LINE_COLOR = getColor(R.color.current_time_line_color)
        private val DEFAULT_CURRENT_TEMPERATURE_POINT_COLOR =
            getColor(R.color.current_temperature_point_color)

        private val DEFAULT_TEXT_SIZE = sp2px(14f).toFloat()
        private val DEFAULT_TEMPERATURE_LABEL_WIDTH = dp2px(30f).toFloat()
        private val DEFAULT_TEMPERATURE_SCALE = dp2px(4f).toFloat()
        private val DEFAULT_SEPARATOR = dp2px(5f).toFloat()
    }

    private val mDateLabelPaint: Paint by lazy { Paint() }//日期标签画笔
    private val mTemperatureLabelPaint: Paint by lazy { Paint() }//温度标签画笔
    private val mTemperatureLineChartPaint: Paint by lazy { Paint() }//温度曲线画笔
    private val mPopupWindowPaint: Paint by lazy { Paint() }//温度气泡窗画笔
    private val mPopupWindowTextPaint: Paint by lazy { Paint() }//温度气泡窗文字画笔
    private val mTimeLinePaint: Paint by lazy { Paint() }//时间标志线画笔
    private val mCurrentTimeLinePaint: Paint by lazy { Paint() }//当时间标志线画笔
    private val mCurrentTemperaturePointPaint: Paint by lazy { Paint() }//当温度点画笔

    private var mTemperatureScale = DEFAULT_TEMPERATURE_SCALE//温度刻度
    private var mTemperatureLabelWidth = DEFAULT_TEMPERATURE_LABEL_WIDTH//温度标识宽度
    private var mPopupWindowTextColor = DEFAULT_POPUP_WINDOW_TEXT_COLOR//气泡窗文字颜色
    private var mTemperatureTextColor = DEFAULT_TEMPERATURE_TEXT_COLOR//温度标识文字颜色
    private var mDateTextColor = DEFAULT_DATE_TEXT_COLOR//日期标识文字颜色
    private var mTemperatureLineChartColor = DEFAULT_TEMPERATURE_LINE_CHART_COLOR//温度曲线颜色
    private var mPopupWindowColor = DEFAULT_TEMPERATURE_POPUP_WINDOW_COLOR//气泡窗颜色
    private var mTimeLineColor = DEFAULT_TIME_LINE_COLOR//时间线颜色
    private var mCurrentTimeLineColor = DEFAULT_CURRENT_TIME_LINE_COLOR//当前时间线颜色
    private var mCurrentTemperaturePointColor = DEFAULT_CURRENT_TEMPERATURE_POINT_COLOR//当前温度点颜色
    private var mTextSize = DEFAULT_TEXT_SIZE//文字大小

    private var mData: WeatherDaysInfo? = null
    private var mTemperaturePointList: ArrayList<Point2D> = ArrayList()

    private var mWidth = 0
    private var mHeight = 0
    private var mMinTemperature = 0
    private var mMaxTemperature = 0
    private var mCurrentIndex: Int = 0
    private var mXScale = 0f
    private var mStartX = 0f
    private var mEndX = 0f
    private var mTimeLineStartY = 0f
    private var mTimeLineEndY = 0f
    private var mTemperatureStartY = 0f
    private var mPopupWindowHeight = 0f
    private var mTemperatureBoxHeight = 0f

    init {
        initAttrs(attrs)
        initPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        initParams()
        updateTemperaturePointList()
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            drawTemperatureLabel(this)
            drawDateLabel(this)
            drawTemperatureLineChart(this)
            drawPopupWindow(this)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            mCurrentIndex = calculateItemIndex(it.x)
            invalidate()
        }
        Log.i(TAG, "current index = $mCurrentIndex")
        return true
    }

    fun updateData(data: WeatherDaysInfo?) {
        mData = data
        mData?.run {
            mMinTemperature = minTemperature
            mMaxTemperature = maxTemperature
        }
        invalidate()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.run {
            val typedArray = context.obtainStyledAttributes(this, R.styleable.Temperature40DaysView)
            mTemperatureLabelWidth =
                typedArray.getDimension(R.styleable.Temperature40DaysView_y_label_width,
                    DEFAULT_TEMPERATURE_LABEL_WIDTH)
            mPopupWindowTextColor =
                typedArray.getColor(R.styleable.Temperature40DaysView_popup_window_text_color,
                    DEFAULT_POPUP_WINDOW_TEXT_COLOR)
            mTemperatureTextColor =
                typedArray.getColor(R.styleable.Temperature40DaysView_temperature_text_color,
                    DEFAULT_TEMPERATURE_TEXT_COLOR)
            mDateTextColor =
                typedArray.getColor(R.styleable.Temperature40DaysView_date_text_color,
                    DEFAULT_DATE_TEXT_COLOR)
            mTemperatureLineChartColor =
                typedArray.getColor(R.styleable.Temperature40DaysView_temperature_line_chart_color,
                    DEFAULT_TEMPERATURE_LINE_CHART_COLOR)
            mPopupWindowColor =
                typedArray.getColor(R.styleable.Temperature40DaysView_popup_window_color,
                    DEFAULT_TEMPERATURE_POPUP_WINDOW_COLOR)
            mTimeLineColor =
                typedArray.getColor(R.styleable.Temperature40DaysView_time_line_color,
                    DEFAULT_TIME_LINE_COLOR)
            mCurrentTimeLineColor =
                typedArray.getColor(R.styleable.Temperature40DaysView_current_time_line_color,
                    DEFAULT_CURRENT_TIME_LINE_COLOR)
            mCurrentTemperaturePointColor =
                typedArray.getColor(R.styleable.Temperature40DaysView_current_temperature_point_color,
                    DEFAULT_CURRENT_TEMPERATURE_POINT_COLOR)
            mTextSize = typedArray.getDimension(R.styleable.Temperature40DaysView_text_size,
                DEFAULT_TEXT_SIZE)
            typedArray.recycle()
        }
    }

    private fun initPaint() {
        mDateLabelPaint.apply {
            isAntiAlias = true
            color = mDateTextColor
            textSize = mTextSize
        }
        mTemperatureLabelPaint.apply {
            isAntiAlias = true
            color = mTemperatureTextColor
            textSize = mTextSize
        }
        mTemperatureLineChartPaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = dp2px(2f).toFloat()
            color = mTemperatureLineChartColor
        }
        mPopupWindowPaint.apply {
            isAntiAlias = true
            color = mPopupWindowColor
        }
        mPopupWindowTextPaint.apply {
            isAntiAlias = true
            color = mPopupWindowTextColor
            textSize = mTextSize
            textAlign = Paint.Align.CENTER
        }
        mTimeLinePaint.apply {
            isAntiAlias = true
            strokeWidth = dp2px(1f).toFloat()
            color = mTimeLineColor
        }
        mCurrentTimeLinePaint.apply {
            isAntiAlias = true
            strokeWidth = dp2px(2f).toFloat()
            style = Paint.Style.STROKE
            color = mCurrentTimeLineColor
        }
        mCurrentTemperaturePointPaint.apply {
            isAntiAlias = true
            color = mCurrentTemperaturePointColor
        }
    }

    private fun initParams() {
        val fontMetrics = mTemperatureLabelPaint.fontMetrics
        val textHeight =
            (ceil((fontMetrics.descent - fontMetrics.ascent).toDouble()) + 2f).toFloat()

        mTemperatureBoxHeight = mTemperatureScale * (mMaxTemperature - mMinTemperature)
        mPopupWindowHeight = textHeight + dp2px(4f)

        mTimeLineStartY = paddingTop.toFloat()
        mTemperatureStartY = mTimeLineStartY + mTemperatureBoxHeight + mPopupWindowHeight

        mTimeLineEndY = mTemperatureStartY + DEFAULT_SEPARATOR

        mWidth = getScreenWidth() - marginStart - marginEnd

        mHeight = (mPopupWindowHeight + mTemperatureBoxHeight + textHeight
                + DEFAULT_SEPARATOR + paddingTop + paddingBottom).toInt()

        mStartX = (mTemperatureLabelWidth + paddingStart)
        mEndX = (mWidth - paddingEnd - mXScale)
        mXScale = mData?.run {
            (mWidth - mTemperatureLabelWidth - paddingStart - paddingEnd) / dayWeatherInfoList.size
        } ?: 0f
    }

    private fun updateTemperaturePointList() {
        mData?.run {
            var x = mStartX
            var y: Float
            mTemperaturePointList.clear()
            dayWeatherInfoList.forEach {
                y =
                    mTemperatureStartY - (it.maxTemperature - mMinTemperature) * mTemperatureScale
                mTemperaturePointList.add(Point2D(x, y))
                x += mXScale
            }
        }
    }

    private fun calculateItemIndex(x: Float): Int {
        if (x < mStartX || x > mWidth - paddingEnd) {
            return mCurrentIndex
        }
        mCurrentIndex = mData?.let {
            max(ceil(((x - mStartX) / mXScale).toDouble()).toInt() - 1, 0)
        } ?: 0
        Log.i(TAG, " index = $mCurrentIndex")
        return mCurrentIndex
    }

    /**
     * 绘制温度标签
     */
    private fun drawTemperatureLabel(canvas: Canvas) {
        canvas.run {
            drawText("${mMinTemperature}°",
                paddingStart.toFloat(),
                mTemperatureStartY,
                mTemperatureLabelPaint)
            drawText("${mMaxTemperature}°",
                paddingStart.toFloat(),
                mTemperatureStartY - (mMaxTemperature - mMinTemperature) * mTemperatureScale,
                mTemperatureLabelPaint)
        }
    }

    /**
     * 绘制时间标签
     */
    private fun drawDateLabel(canvas: Canvas) {
        canvas.run {
            mData?.run {
                var x = mStartX
                val s: Int = dayWeatherInfoList.size / 3
                dayWeatherInfoList.forEachIndexed { index, item ->
                    if (index % s == 0) {
                        when (index) {
                            0 -> {
                                mDateLabelPaint.textAlign = Paint.Align.LEFT
                            }
                            dayWeatherInfoList.size - 1 -> {
                                mDateLabelPaint.textAlign = Paint.Align.RIGHT
                            }
                            else -> {
                                mDateLabelPaint.textAlign = Paint.Align.CENTER
                            }
                        }
                        drawText(item.date,
                            mStartX + index * mXScale,
                            mHeight - DEFAULT_SEPARATOR,
                            mDateLabelPaint)
                    }
                    drawLine(x, mTimeLineStartY, x, mTimeLineEndY, mTimeLinePaint)
                    x += mXScale
                }
            }
        }
    }

    /**
     * 绘制温度曲线
     */
    private fun drawTemperatureLineChart(canvas: Canvas) {
        canvas.run {

            if (mTemperaturePointList.size >= 2) {
                val path = Path()
                var perPoint = mTemperaturePointList[0]
                var nextPoint: Point2D
                var centerPointX: Float
                var centerPoint1: Point2D
                var centerPoint2: Point2D

                path.moveTo(perPoint.x, perPoint.y)
                mTemperaturePointList.forEachIndexed { index, point ->
                    if (index == 0) {
                        return@forEachIndexed
                    }
                    nextPoint = point
                    centerPointX = (perPoint.x + mXScale * 0.5f)
                    centerPoint1 = Point2D(centerPointX, perPoint.y)
                    centerPoint2 = Point2D(centerPointX, nextPoint.y)
                    path.cubicTo(centerPoint1.x, centerPoint1.y,
                        centerPoint2.x, centerPoint2.y,
                        nextPoint.x, nextPoint.y)
                    perPoint = nextPoint
                }
                drawPath(path, mTemperatureLineChartPaint)
            }
        }
    }

    /**
     * 绘制气泡窗
     */
    private fun drawPopupWindow(canvas: Canvas) {
        canvas.run {
            mData?.run {
                if (mCurrentIndex < 0 || mCurrentIndex >= dayWeatherInfoList.size) {
                    return
                }
                val temperaturePoint = mTemperaturePointList[mCurrentIndex]
                val info = dayWeatherInfoList[mCurrentIndex]
                val content = "${info.date} ${info.maxTemperature}°"


                val popupWindowWidth =
                    mPopupWindowTextPaint.measureText(content) + DEFAULT_SEPARATOR * 4
                val popupWindowCenterX = when {
                    mCurrentIndex * mXScale < popupWindowWidth * 0.5f -> {
                        popupWindowWidth * 0.5f + mStartX
                    }
                    (mEndX - mCurrentIndex * mXScale - mStartX) < popupWindowWidth * 0.5f -> {
                        mEndX - popupWindowWidth * 0.5f
                    }
                    else -> {
                        temperaturePoint.x
                    }
                }

                drawLine(temperaturePoint.x,
                    mTimeLineStartY,
                    temperaturePoint.x,
                    mTimeLineEndY,
                    mCurrentTimeLinePaint)
                drawCircle(temperaturePoint.x,
                    temperaturePoint.y,
                    10f,
                    mCurrentTemperaturePointPaint)
                drawRoundRect(
                    RectF(popupWindowCenterX - popupWindowWidth * 0.5f,
                        mTimeLineStartY - DEFAULT_SEPARATOR,
                        popupWindowCenterX + popupWindowWidth * 0.5f,
                        mTimeLineStartY - DEFAULT_SEPARATOR + mPopupWindowHeight),
                    mPopupWindowHeight * 0.5f,
                    mPopupWindowHeight * 0.5f,
                    mPopupWindowPaint)
                drawText(content,
                    popupWindowCenterX,
                    mTimeLineStartY - DEFAULT_SEPARATOR + mPopupWindowHeight * 0.75F,
                    mPopupWindowTextPaint)
            }
        }
    }
}