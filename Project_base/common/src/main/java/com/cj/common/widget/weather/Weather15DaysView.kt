package com.cj.common.widget.weather

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.HorizontalScrollView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.cj.common.R
import com.cj.common.bean.DayWeatherInfo
import com.cj.common.bean.Weather15DaysInfo
import com.cj.common.enums.EnumAirQuality
import com.cj.framework.bean.Point2D
import com.cj.framework.utils.dp2px
import com.cj.framework.utils.getColor
import com.cj.framework.utils.getScreenWidth
import com.cj.framework.utils.sp2px
import kotlin.math.ceil

/**
 * 15天天气信息view
 *
 * @author CJ
 * @date 2021/5/17 13:47
 */
class Weather15DaysView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    HorizontalScrollView(context, attrs, defStyleAttr),
    IWeatherLineChartView<Weather15DaysInfo> {

    companion object {
        private const val TAG = "Weather15DaysView"

        private val DEFAULT_ITEM_WIDTH = sp2px(50f).toFloat()
        private val DEFAULT_WEATHER_ICON_HEIGHT = dp2px(25f).toFloat()
        private val DEFAULT_TEMPERATURE_LINE_CHART_HEIGHT = dp2px(60f).toFloat()
        private val DEFAULT_TEXT_SIZE = sp2px(14f).toFloat()
        private val DEFAULT_WIND_POWER_TEXT_SIZE = sp2px(12f).toFloat()
        private val DEFAULT_AIR_QUALITY_TEXT_SIZE = sp2px(12f).toFloat()
        private val DEFAULT_SEPARATOR = sp2px(10f).toFloat()

        private val DEFAULT_TEXT_COLOR = getColor(R.color.text_color_black)
        private val DEFAULT_MIN_TEMPERATURE_LINE_CHART_COLOR =
            getColor(R.color.min_temperature_line_chart_color)
        private val DEFAULT_MAX_TEMPERATURE_LINE_CHART_COLOR =
            getColor(R.color.max_temperature_line_chart_color)
        private val DEFAULT_MIN_TEMPERATURE_POINT_COLOR =
            getColor(R.color.min_temperature_point_color)
        private val DEFAULT_MAX_TEMPERATURE_POINT_COLOR =
            getColor(R.color.max_temperature_point_color)
        private val DEFAULT_WIND_POWER_TEXT_COLOR = getColor(R.color.text_color_gry)
        private const val DEFAULT_AIR_QUALITY_TEXT_COLOR = Color.WHITE
    }

    private val mTextPaint: Paint by lazy { Paint() }//文字画笔
    private val mMinTemperatureLineChartPaint: Paint by lazy { Paint() }//最低温曲线画笔
    private val mYesterdayMinTemperatureLineChartPaint: Paint by lazy { Paint() }//昨天最低温曲线画笔
    private val mMaxTemperatureLineChartPaint: Paint by lazy { Paint() }//最高温曲线画笔
    private val mYesterdayMaxTemperatureLineChartPaint: Paint by lazy { Paint() }//昨天最高温曲线画笔
    private val mMinTemperaturePointPaint: Paint by lazy { Paint() }//最低温温度节点画笔
    private val mMaxTemperaturePointPaint: Paint by lazy { Paint() }//最高温温度节点画笔
    private val mWindDirectionTextPaint: Paint by lazy { Paint() }//风向文字画笔
    private val mWindPowerTextPaint: Paint by lazy { Paint() }//风力文字画笔
    private val mAirQualityTextPaint: Paint by lazy { Paint() }//空气质量文字画笔
    private val mAirQualityBoxPaint: Paint by lazy { Paint() }//空气质量区域画笔
    private val mSeparatorLinePaint: Paint by lazy { Paint() }//分割线画笔
    private val mSelectedBackgroundPaint: Paint by lazy { Paint() }//选中背景画笔


    private var mItemWidth = DEFAULT_ITEM_WIDTH//item宽度
    private var mItemTextSize = DEFAULT_TEXT_SIZE//文字大小
    private var mItemTextColor = DEFAULT_TEXT_COLOR//文字颜色
    private var mWeatherIconHeight = DEFAULT_WEATHER_ICON_HEIGHT//天气图标高度
    private var mTemperatureLineChartHeight = DEFAULT_TEMPERATURE_LINE_CHART_HEIGHT//温度曲线区域高度
    private var mMinTemperatureLineChartColor = DEFAULT_MIN_TEMPERATURE_LINE_CHART_COLOR//最低温曲线颜色
    private var mMaxTemperatureLineChartColor = DEFAULT_MAX_TEMPERATURE_LINE_CHART_COLOR//最高温曲线颜色
    private var mMinTemperaturePointColor = DEFAULT_MIN_TEMPERATURE_POINT_COLOR//最低温温度节点颜色
    private var mMaxTemperaturePointColor = DEFAULT_MAX_TEMPERATURE_POINT_COLOR//最高温温度节点颜色
    private var mWindDirectionTextColor = DEFAULT_TEXT_COLOR//风向文字颜色
    private var mWindDirectionTextSize = DEFAULT_TEXT_SIZE//风向文字大小
    private var mWindPowerTextColor = DEFAULT_WIND_POWER_TEXT_COLOR//风力文字颜色
    private var mWindPowerTextSize = DEFAULT_WIND_POWER_TEXT_SIZE//风力文字大小
    private var mAirQualityTextColor = DEFAULT_AIR_QUALITY_TEXT_COLOR//空气质量文字颜色
    private var mAirQualityTextSize = DEFAULT_AIR_QUALITY_TEXT_SIZE//空气质量文字大小

    private var mData: Weather15DaysInfo? = null
    private val mMinTemperaturePointList = ArrayList<Point2D>()
    private val mMaxTemperaturePointList = ArrayList<Point2D>()
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mStartX = 0f
    private var mWeekStartY = 0f
    private var mDateStartY = 0f
    private var mWeatherFromStartY = 0f
    private var mWeatherIconFromStartY = 0f
    private var mMaxTemperatureStartY = 0f
    private var mTemperatureLineChartStartY = 0f
    private var mMinTemperatureStartY = 0f
    private var mWeatherIconToStartY = 0f
    private var mWeatherToStartY = 0f
    private var mWindDirectionStartY = 0f
    private var mWindPowerStartY = 0f
    private var mAirQualityStartY = 0f
    private var mAirQualityBoxHeight = 0f

    private var mCurrentIndex: Int = -1
    private var mOnItemClickListener: OnItemClickListener? = null

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

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
            drawData(canvas)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            mCurrentIndex = when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    calculateItemIndex(it.x)
                }
                MotionEvent.ACTION_UP -> {
                    val index = calculateItemIndex(it.x)
                    mOnItemClickListener?.onItemClick(index, getItem(index))
                    -1
                }
                else -> {
                    -1
                }
            }
            invalidate()
        }
        Log.i(TAG, "current index = $mCurrentIndex")
        return true
    }

    override fun setScrollOffset(scrollOffset: Int, maxScrollOffset: Int) {
        //do nothing
    }

    override fun setWeatherData(data: Weather15DaysInfo?) {
        mData = data
        invalidate()
    }

    /**
     * 设置item点击监听
     */
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.run {
            val typedArray = context.obtainStyledAttributes(this, R.styleable.Weather15DaysView)
            mItemWidth = typedArray.getDimension(R.styleable.Weather15DaysView_item_width,
                DEFAULT_ITEM_WIDTH)
            mItemTextSize = typedArray.getDimension(R.styleable.Weather15DaysView_item_text_size,
                DEFAULT_TEXT_SIZE)
            mItemTextColor =
                typedArray.getColor(R.styleable.Weather15DaysView_item_text_color,
                    DEFAULT_TEXT_COLOR)
            mWeatherIconHeight =
                typedArray.getDimension(R.styleable.Weather15DaysView_weather_icon_height,
                    DEFAULT_WEATHER_ICON_HEIGHT)
            mTemperatureLineChartHeight =
                typedArray.getDimension(R.styleable.Weather15DaysView_temperature_line_chart_height,
                    DEFAULT_TEMPERATURE_LINE_CHART_HEIGHT)
            mMinTemperatureLineChartColor =
                typedArray.getColor(R.styleable.Weather15DaysView_min_temperature_line_chart_color,
                    DEFAULT_MIN_TEMPERATURE_LINE_CHART_COLOR)
            mMaxTemperatureLineChartColor =
                typedArray.getColor(R.styleable.Weather15DaysView_max_temperature_line_chart_color,
                    DEFAULT_MAX_TEMPERATURE_LINE_CHART_COLOR)
            mMinTemperatureLineChartColor =
                typedArray.getColor(R.styleable.Weather15DaysView_min_temperature_point_color,
                    DEFAULT_MIN_TEMPERATURE_POINT_COLOR)
            mMaxTemperatureLineChartColor =
                typedArray.getColor(R.styleable.Weather15DaysView_max_temperature_point_color,
                    DEFAULT_MAX_TEMPERATURE_POINT_COLOR)
            mWindDirectionTextColor =
                typedArray.getColor(R.styleable.Weather15DaysView_wind_direction_text_color,
                    DEFAULT_TEXT_COLOR)
            mWindDirectionTextSize =
                typedArray.getDimension(R.styleable.Weather15DaysView_wind_direction_text_size,
                    DEFAULT_TEXT_SIZE)
            mWindPowerTextColor =
                typedArray.getColor(R.styleable.Weather15DaysView_wind_power_text_color,
                    DEFAULT_WIND_POWER_TEXT_COLOR)
            mWindPowerTextSize =
                typedArray.getDimension(R.styleable.Weather15DaysView_wind_power_text_size,
                    DEFAULT_WIND_POWER_TEXT_SIZE)
            mAirQualityTextColor =
                typedArray.getColor(R.styleable.Weather15DaysView_air_quality_text_color,
                    DEFAULT_AIR_QUALITY_TEXT_COLOR)
            mAirQualityTextSize =
                typedArray.getDimension(R.styleable.Weather15DaysView_air_quality_text_size,
                    DEFAULT_AIR_QUALITY_TEXT_SIZE)
            typedArray.recycle()
        }
    }

    private fun initPaint() {
        mTextPaint.apply {
            isAntiAlias = true
            color = mItemTextColor
            textSize = mItemTextSize
            textAlign = Paint.Align.CENTER
        }
        mMinTemperatureLineChartPaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = dp2px(2f).toFloat()
            color = mMinTemperatureLineChartColor
        }
        mYesterdayMinTemperatureLineChartPaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = dp2px(2f).toFloat()
            color = mMinTemperatureLineChartColor
            //绘制长度为4的实线后再绘制长度为4的空白区域，0位间隔
            pathEffect = DashPathEffect(floatArrayOf(dp2px(4f).toFloat(), dp2px(4f).toFloat()), 0f)
        }
        mMaxTemperatureLineChartPaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = dp2px(2f).toFloat()
            color = mMaxTemperatureLineChartColor
        }
        mYesterdayMaxTemperatureLineChartPaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = dp2px(2f).toFloat()
            color = mMaxTemperatureLineChartColor
            //绘制长度为4的实线后再绘制长度为4的空白区域，0位间隔
            pathEffect = DashPathEffect(floatArrayOf(dp2px(4f).toFloat(), dp2px(4f).toFloat()), 0f)
        }
        mMinTemperaturePointPaint.apply {
            isAntiAlias = true
            color = mMinTemperaturePointColor
            style = Paint.Style.FILL
        }
        mMaxTemperaturePointPaint.apply {
            isAntiAlias = true
            color = mMaxTemperaturePointColor
            style = Paint.Style.FILL
        }
        mWindDirectionTextPaint.apply {
            isAntiAlias = true
            color = mWindDirectionTextColor
            textSize = mWindDirectionTextSize
            textAlign = Paint.Align.CENTER
        }
        mWindPowerTextPaint.apply {
            isAntiAlias = true
            color = mWindPowerTextColor
            textSize = mWindPowerTextSize
            textAlign = Paint.Align.CENTER
        }
        mAirQualityTextPaint.apply {
            isAntiAlias = true
            color = mAirQualityTextColor
            textSize = mAirQualityTextSize
            textAlign = Paint.Align.CENTER
        }
        mAirQualityBoxPaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = Color.GREEN
        }
        mSeparatorLinePaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = getColor(R.color.line_separator_color)
            strokeWidth = dp2px(0.5f).toFloat()
        }
        mSelectedBackgroundPaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = getColor(R.color.item_selected_background)
        }
    }

    private fun initParams() {
        mStartX = paddingStart.toFloat()

        var fontMetrics = mTextPaint.fontMetrics
        var textHeight =
            (ceil((fontMetrics.descent - fontMetrics.ascent).toDouble()) + 2f).toFloat()

        mWeekStartY = textHeight + DEFAULT_SEPARATOR + paddingTop
        mDateStartY = mWeekStartY + textHeight + DEFAULT_SEPARATOR
        mWeatherFromStartY = mDateStartY + textHeight + DEFAULT_SEPARATOR
        mWeatherIconFromStartY = mWeatherFromStartY + DEFAULT_SEPARATOR

        mMaxTemperatureStartY =
            mWeatherIconFromStartY + mWeatherIconHeight + textHeight + DEFAULT_SEPARATOR

        mTemperatureLineChartStartY =
            mMaxTemperatureStartY + mTemperatureLineChartHeight + DEFAULT_SEPARATOR * 2

        mMinTemperatureStartY =
            mMaxTemperatureStartY + mTemperatureLineChartHeight + textHeight + DEFAULT_SEPARATOR * 3

        mWeatherIconToStartY = mMinTemperatureStartY + DEFAULT_SEPARATOR
        mWeatherToStartY =
            mWeatherIconToStartY + mWeatherIconHeight + textHeight + DEFAULT_SEPARATOR

        fontMetrics = mWindDirectionTextPaint.fontMetrics
        textHeight = (ceil((fontMetrics.descent - fontMetrics.ascent).toDouble()) + 2f).toFloat()
        mWindDirectionStartY = mWeatherToStartY + textHeight + DEFAULT_SEPARATOR

        fontMetrics = mWindPowerTextPaint.fontMetrics
        textHeight = (ceil((fontMetrics.descent - fontMetrics.ascent).toDouble()) + 2f).toFloat()
        mWindPowerStartY = mWindDirectionStartY + textHeight + DEFAULT_SEPARATOR

        fontMetrics = mAirQualityTextPaint.fontMetrics
        textHeight = (ceil((fontMetrics.descent - fontMetrics.ascent).toDouble()) + 2f).toFloat()
        mAirQualityStartY = mWindPowerStartY + textHeight + DEFAULT_SEPARATOR
        mAirQualityBoxHeight = textHeight

        val endY = mAirQualityStartY + DEFAULT_SEPARATOR

        mWidth = mData?.run {
            (mItemWidth * dayWeatherInfoList.size + DEFAULT_SEPARATOR * 2 + paddingStart + paddingEnd).toInt()
        } ?: getScreenWidth()
        mHeight = (endY + paddingTop + paddingBottom).toInt()
    }

    private fun drawData(canvas: Canvas) {
        mData?.let {
            canvas.run {
                var x = mStartX + mItemWidth * 0.5f
                var y: Float

                it.dayWeatherInfoList.forEachIndexed { index, value ->

                    if (mCurrentIndex == index) {
                        drawRoundRect(RectF(
                            x - mItemWidth * 0.5f,
                            0f,
                            x + mItemWidth * 0.5f,
                            mHeight.toFloat(),
                        ), mItemWidth * 0.1f, mItemWidth * 0.1f, mSelectedBackgroundPaint)
                    }

                    y = mWeekStartY
                    //星期
                    drawText(value.week, x, y, mTextPaint)
                    y = mDateStartY
                    //日期
                    drawText(value.date, x, y, mTextPaint)
                    y = mWeatherFromStartY
                    //天气状况from
                    drawText(context.resources.getString(value.weatherFrom.weatherRes),
                        x,
                        y,
                        mTextPaint)
                    //天气状况图标From
                    y = mWeatherIconFromStartY
                    drawableWeatherIcon(this, x.toInt(), y.toInt(), value.weatherIconFrom.iconRes)
                    y = mMaxTemperatureStartY
                    //最高温
                    drawText("${value.maxTemperature}°", x, y, mTextPaint)
                    y = mMinTemperatureStartY
                    //最低温
                    drawText("${value.minTemperature}°", x, y, mTextPaint)
                    //天气状况图标To
                    y = mWeatherIconToStartY
                    drawableWeatherIcon(this, x.toInt(), y.toInt(), value.weatherIconTo.iconRes)
                    y = mWeatherToStartY
                    //天气状况To
                    drawText(context.resources.getString(value.weatherTo.weatherRes),
                        x,
                        y,
                        mTextPaint)
                    y = mWindDirectionStartY
                    //风向
                    drawText(value.windDirection, x, y, mWindDirectionTextPaint)
                    y = mWindPowerStartY
                    //风力
                    drawText(value.windPower, x, y, mWindPowerTextPaint)
                    //空气质量
                    value.airQuality?.run {
                        val radius = mAirQualityBoxHeight * 0.5f + dp2px(2f)
                        y = mAirQualityStartY
                        mAirQualityBoxPaint.color = getAirQualityColor(this)
                        drawRoundRect(RectF(x - mItemWidth * 0.4f, y - mAirQualityBoxHeight * 0.8f - dp2px(2f),
                            x + mItemWidth * 0.4f, y + mAirQualityBoxHeight * 0.2f + dp2px(2f)),
                            radius, radius, mAirQualityBoxPaint)
                        drawText(context.resources.getString(this.qualityRes),
                            x,
                            y,
                            mAirQualityTextPaint)
                    }
                    //item分割线
                    drawLine(x + mItemWidth * 0.5f, 0f,
                        x + mItemWidth * 0.5f, mHeight.toFloat(), mSeparatorLinePaint)
                    x += mItemWidth

                }
                drawTemperatureLineChart(canvas)
            }
        }
    }

    /**
     * 绘制天气图标
     */
    private fun drawableWeatherIcon(
        canvas: Canvas,
        x: Int,
        y: Int,
        @DrawableRes iconResId: Int,
    ) {
        ContextCompat.getDrawable(context, iconResId)?.run {
            if (this is BitmapDrawable) {
                val left = (x - bitmap.width * 0.5).toInt()
                val right = left + bitmap.width
                val bottom = y + bitmap.height
                val rectImage = Rect(left, y, right, bottom)
                bounds = rectImage
                draw(canvas)
            }
        }
    }

    /**
     * 绘制温度线
     */
    private fun drawTemperatureLineChart(canvas: Canvas) {
        canvas.run {
            var perPoint: Point2D
            var nextPoint: Point2D
            var centerPointX: Float
            var centerPoint1: Point2D
            var centerPoint2: Point2D
            if (mMinTemperaturePointList.size >= 2) {
                val minTemperaturePath = Path()
                val mYesterdayMinTemperaturePath = Path()
                perPoint = mMinTemperaturePointList[0]
                nextPoint = mMinTemperaturePointList[1]

                mYesterdayMinTemperaturePath.moveTo(perPoint.x, perPoint.y)
                mYesterdayMinTemperaturePath.quadTo(perPoint.x,
                    nextPoint.y,
                    nextPoint.x,
                    nextPoint.y)
                drawPath(mYesterdayMinTemperaturePath, mYesterdayMinTemperatureLineChartPaint)
                mMinTemperaturePointList.forEachIndexed { index, point ->
                    if (index == 0 || index == 1) {
                        perPoint = point
                        minTemperaturePath.moveTo(perPoint.x, perPoint.y)
                        return@forEachIndexed
                    }
                    nextPoint = point
                    centerPointX = (perPoint.x + mItemWidth * 0.5f)
                    centerPoint1 = Point2D(centerPointX, perPoint.y)
                    centerPoint2 = Point2D(centerPointX, nextPoint.y)

                    minTemperaturePath.cubicTo(centerPoint1.x, centerPoint1.y,
                        centerPoint2.x, centerPoint2.y,
                        nextPoint.x, nextPoint.y)
                    perPoint = nextPoint
                }
                drawPath(minTemperaturePath, mMinTemperatureLineChartPaint)
            }

            if (mMaxTemperaturePointList.size >= 2) {
                perPoint = mMaxTemperaturePointList[0]
                nextPoint = mMaxTemperaturePointList[1]
                val maxTemperaturePath = Path()
                val mYesterdayMaxTemperaturePath = Path()
                mYesterdayMaxTemperaturePath.moveTo(perPoint.x, perPoint.y)
                mYesterdayMaxTemperaturePath.quadTo(perPoint.x,
                    nextPoint.y,
                    nextPoint.x,
                    nextPoint.y)
                drawPath(mYesterdayMaxTemperaturePath, mYesterdayMaxTemperatureLineChartPaint)

                mMaxTemperaturePointList.forEachIndexed { index, point ->
                    if (index == 0 || index == 1) {
                        perPoint = point
                        maxTemperaturePath.moveTo(perPoint.x, perPoint.y)
                        return@forEachIndexed
                    }
                    nextPoint = point
                    centerPointX = (perPoint.x + mItemWidth * 0.5f)
                    centerPoint1 = Point2D(centerPointX, perPoint.y)
                    centerPoint2 = Point2D(centerPointX, nextPoint.y)
                    maxTemperaturePath.cubicTo(centerPoint1.x, centerPoint1.y,
                        centerPoint2.x, centerPoint2.y,
                        nextPoint.x, nextPoint.y)
                    perPoint = nextPoint

                }
                drawPath(maxTemperaturePath, mMaxTemperatureLineChartPaint)
            }
        }
        drawTemperaturePoint(canvas)
    }

    /**
     * 绘制温度点
     */
    private fun drawTemperaturePoint(canvas: Canvas) {
        canvas.run {
            mMinTemperaturePointList.forEach {
                mMinTemperaturePointPaint.style = Paint.Style.STROKE
                mMinTemperaturePointPaint.strokeWidth = dp2px(4f).toFloat()
                mMinTemperaturePointPaint.color = mMinTemperaturePointColor
                drawCircle(it.x, it.y, dp2px(2f).toFloat(), mMinTemperaturePointPaint)
                mMinTemperaturePointPaint.style = Paint.Style.FILL
                mMinTemperaturePointPaint.color = Color.WHITE
                drawCircle(it.x, it.y, dp2px(2f).toFloat(), mMinTemperaturePointPaint)
            }
            mMaxTemperaturePointList.forEach {
                mMaxTemperaturePointPaint.style = Paint.Style.STROKE
                mMaxTemperaturePointPaint.strokeWidth = dp2px(4f).toFloat()
                mMaxTemperaturePointPaint.color = mMaxTemperaturePointColor
                drawCircle(it.x, it.y, dp2px(2f).toFloat(), mMaxTemperaturePointPaint)
                mMaxTemperaturePointPaint.style = Paint.Style.FILL
                mMaxTemperaturePointPaint.color = Color.WHITE
                drawCircle(it.x, it.y, dp2px(2f).toFloat(), mMaxTemperaturePointPaint)
            }
        }
    }

    private fun updateTemperaturePointList() {
        mData?.run {
            val temperatureScale =
                (mTemperatureLineChartHeight) / (maxTemperature - minTemperature)
            var x = mStartX + mItemWidth * 0.5f
            var y: Float
            mMinTemperaturePointList.clear()
            mMaxTemperaturePointList.clear()
            dayWeatherInfoList.forEach {
                y =
                    mTemperatureLineChartStartY - (it.minTemperature - minTemperature) * temperatureScale
                mMinTemperaturePointList.add(Point2D(x, y))

                y =
                    mTemperatureLineChartStartY - (it.maxTemperature - minTemperature) * temperatureScale
                mMaxTemperaturePointList.add(Point2D(x, y))
                x += mItemWidth
            }
        }
    }

    private fun calculateItemIndex(x: Float): Int {
        mCurrentIndex = mData?.let {
            ceil((x / mItemWidth).toDouble()).toInt() - 1
        } ?: -1
        return mCurrentIndex
    }

    private fun getAirQualityColor(quality: EnumAirQuality): Int {
        return when (quality) {
            EnumAirQuality.AIR_QUALITY_EXCELLENT -> {
                getColor(R.color.air_quality_excellent_unselected)
            }
            EnumAirQuality.AIR_QUALITY_GOOD -> {
                getColor(R.color.air_quality_good_unselected)
            }
            EnumAirQuality.AIR_QUALITY_SLIGHTLY -> {
                getColor(R.color.air_quality_slightly_unselected)
            }
            EnumAirQuality.AIR_QUALITY_HEAVY -> {
                getColor(R.color.air_quality_heavy_unselected)
            }
        }
    }

    private fun getItem(position: Int): DayWeatherInfo? {
        return mData?.run {
            if (position >= dayWeatherInfoList.size || position < 0) {
                null
            } else {
                dayWeatherInfoList[position]
            }
        }
    }

    interface OnItemClickListener {
        /**
         * item点击
         */
        fun onItemClick(position: Int, item: DayWeatherInfo?)
    }

}