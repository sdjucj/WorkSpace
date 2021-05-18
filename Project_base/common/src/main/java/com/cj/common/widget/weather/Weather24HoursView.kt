package com.cj.common.widget.weather

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.util.forEach
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.cj.common.R
import com.cj.common.bean.HourlyWeatherInfo
import com.cj.common.bean.Weather24HoursInfo
import com.cj.common.enums.EnumAirQuality
import com.cj.common.enums.EnumWeather
import com.cj.common.enums.EnumWeatherIcon

import com.cj.framework.bean.Point2D
import com.cj.framework.utils.*
import kotlin.math.ceil
import kotlin.properties.Delegates

/**
 * 24小时天气图
 *
 * @author CJ
 * @date 2021/5/11 19:08
 */
class Weather24HoursView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr), IWeatherLineChartView<Weather24HoursInfo> {

    companion object {
        private const val TAG = "Weather24HoursView"

        //default size
        private val DEFAULT_X_SCALE = dp2px(30f).toFloat()
        private val DEFAULT_Y_SCALE = dp2px(10f).toFloat()
        private val DEFAULT_Y_LABEL_WIDTH = dp2px(30f).toFloat()
        private val DEFAULT_TEXT_SIZE = sp2px(10f).toFloat()

        private val DEFAULT_SEPARATOR = sp2px(10f).toFloat()
        private val DEFAULT_SPACE = dp2px(2f).toFloat()


        //default color
        private val DEFAULT_TEXT_COLOR = getColor(R.color.text_color_black)
        private val DEFAULT_HISTORY_TIME_TEXT_COLOR = getColor(R.color.text_color_message)
        private val DEFAULT_WEATHER_POPUP_WINDOW_COLOR = getColor(R.color.sky_blue)
        private const val DEFAULT_WEATHER_POPUP_WINDOW_TEXT_COLOR = Color.WHITE
        private val DEFAULT_HISTORY_TEMPERATURE_LINE_CHART_COLOR = getColor(R.color.little_blue)
        private val DEFAULT_FEATURE_TEMPERATURE_LINE_CHART_COLOR = getColor(R.color.sky_blue)

    }

    private var mXScaleSize = DEFAULT_X_SCALE//x轴刻度
    private var mYScaleSize = DEFAULT_Y_SCALE//y轴刻度
    private var mYLabelWidth = DEFAULT_Y_LABEL_WIDTH//y轴标签宽度
    private var mTimeTextSize = DEFAULT_TEXT_SIZE//时间文字大小
    private var mHistoryTimeTextColor = DEFAULT_HISTORY_TIME_TEXT_COLOR//过去时间字体颜色
    private var mFeatureTimeTextColor = DEFAULT_TEXT_COLOR//未来时间字体颜色
    private var mWindPowerLabelTextSize = DEFAULT_TEXT_SIZE//风力标签字体大小
    private var mWindPowerLabelTextColor = DEFAULT_TEXT_COLOR//风力标签字体颜色
    private var mWindPowerTextSize = DEFAULT_TEXT_SIZE//风力值字体大小
    private var mWindPowerTextColor = DEFAULT_TEXT_COLOR//风力值字体颜色
    private var mAirQualityLabelTextSize = DEFAULT_TEXT_SIZE//空气质量标签字体大小
    private var mAirQualityLabelTextColor = DEFAULT_TEXT_COLOR//空气质量标签字体颜色
    private var mAirQualityTextSize = DEFAULT_TEXT_SIZE//空气质量值字体大小
    private var mTemperatureLabelTextSize = DEFAULT_TEXT_SIZE//温度标签字体大小
    private var mTemperatureLabelTextColor = DEFAULT_TEXT_COLOR//温度标签字体颜色
    private var mWeatherPopupWindowColor = DEFAULT_WEATHER_POPUP_WINDOW_COLOR//当前天气气泡窗颜色
    private var mWeatherPopupWindowTextColor = DEFAULT_WEATHER_POPUP_WINDOW_TEXT_COLOR//当前天气气泡窗字体颜色
    private var mWeatherPopupWindowTextSize = DEFAULT_TEXT_SIZE//当前天气气泡窗字体大小
    private var mHistoryTemperatureLineChartColor =
        DEFAULT_HISTORY_TEMPERATURE_LINE_CHART_COLOR//过去天气温度曲线颜色
    private var mFeatureTemperatureLineChartColor =
        DEFAULT_FEATURE_TEMPERATURE_LINE_CHART_COLOR//未来天气温度曲线颜色


    private val mYRetFPaint: Paint by lazy { Paint() }//Y轴背景画笔
    private val mTimeTextPaint: Paint by lazy { Paint() }//时间画笔
    private val mWindPowerLabelTextPaint: Paint by lazy { Paint() }//风力标签画笔
    private val mWindPowerTextPaint: Paint by lazy { Paint() }//风力值画笔
    private val mWindPowerBoxPaint: Paint by lazy { Paint() }//风力图形画笔
    private val mAirQualityLabelTextPaint: Paint by lazy { Paint() }//空气质量标签画笔
    private val mAirQualityTextPaint: Paint by lazy { Paint() }//空气质量值画笔
    private val mAirQualityBoxPaint: Paint by lazy { Paint() }//空气质量图形画笔
    private val mTemperatureLabelTextPaint: Paint by lazy { Paint() }//当前温度点画笔
    private val mCurrentTemperaturePointPaint: Paint by lazy { Paint() }//当前温度点画笔
    private val mTemperatureLinePaint: Paint by lazy { Paint() }//温度曲线画笔
    private val mWeatherPopupWindowPaint: Paint by lazy { Paint() }//天气气泡窗画笔
    private val mWeatherPopupWindowTextPaint: Paint by lazy { Paint() }//天气气泡窗文字画笔
    private val mTemperatureGradientBoxPaint: Paint by lazy { Paint() }//温度渐变区域画笔

    private var mTimeTextHeight by Delegates.notNull<Float>()//时间文字高度
    private var mWindPowerTextHeight by Delegates.notNull<Float>()//风力文字轴高度
    private var mWindPowerBoxHeight by Delegates.notNull<Float>()//风力Y轴高度
    private var mAirQualityBoxHeight by Delegates.notNull<Float>()//空气质量Y轴高度
    private var mTemperatureBoxHeight by Delegates.notNull<Float>()//温度折线图Y轴高度
    private var mWeatherPopupWindowHeight by Delegates.notNull<Float>()//天气气泡窗高度
    private var mWeatherPopupWindowTextHeight by Delegates.notNull<Float>()//天气气泡窗文字高度

    private var mStartX by Delegates.notNull<Float>() //X起点
    private var mWindPowerStartY = 0f//风力Y七点
    private var mAirQualityStartY = 0f//空气质量Y起点
    private var mTemperatureStartY = 0f//温度Y起点

    private var mWindPowderRoundRectRadius by Delegates.notNull<Float>()//风力矩形区域圆角半径
    private var mAirQualityRoundRectRadius by Delegates.notNull<Float>()//空气质量矩形区域圆角半径
    private var mWeatherPopupWindowRoundRectRadius by Delegates.notNull<Float>()//天气气泡窗矩形区域圆角半径

    private var mTemperatureGradientUnselected by Delegates.notNull<LinearGradient>()//温度未选中区域渐变
    private var mTemperatureGradientSelected by Delegates.notNull<LinearGradient>()//温度选中区域渐变

    private var mData: Weather24HoursInfo? = null
    private var mCurrentTime = 0f

    private var mMinTemperature: Int = 0//最低温
    private var mMaxTemperature: Int = 0//最高温

    private var mWidth: Int = width
    private var mHeight: Int = height

    private var mMaxScrollOffset: Int = 0
    private var mScrollOffset: Int = 0
    private var mCurrentIndex: Int = 0

    private var mWindPowerList: SparseArray<LinkedHashMap<Int, Int>>? = null
    private val mTemperaturePointList: ArrayList<Point2D> = ArrayList()

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
        initGradient()
        Log.i(TAG, "view width = $mWidth , height = $mHeight")
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mData?.let {
            canvas?.run {
                drawTime(this)
                drawTemperature(this)
                drawAirQuality(this)
                drawWindPower(this)
                drawLabelInY(this)
            }
        }
    }

    override fun setScrollOffset(scrollOffset: Int, maxScrollOffset: Int) {
        mMaxScrollOffset = maxScrollOffset - marginStart //marginStart与滑动最大距离有个，marginEnd无影响
        mScrollOffset = scrollOffset
        mCurrentIndex = calculateItemIndex()
        Log.i(TAG, "当前位置 ： $mCurrentIndex ,滑动距离：$mScrollOffset , 最大滑动距离 ：$mMaxScrollOffset")
        invalidate()
    }

    override fun setWeatherData(data: Weather24HoursInfo?) {
        mData = data
        mData?.run {
            mMinTemperature = minTemperature
            mMaxTemperature = maxTemperature
            mCurrentTime = currentTime
            mWindPowerList = windPowerList
        }
        invalidate()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.run {
            val typedArray = context.obtainStyledAttributes(this, R.styleable.Weather24HoursView)
            mXScaleSize = typedArray.getDimension(R.styleable.Weather24HoursView_x_scale,
                DEFAULT_X_SCALE)
            mYScaleSize = typedArray.getDimension(R.styleable.Weather24HoursView_y_scale,
                DEFAULT_Y_SCALE)
            mYLabelWidth = typedArray.getDimension(R.styleable.Weather24HoursView_y_label_width,
                DEFAULT_Y_LABEL_WIDTH)
            mTimeTextSize = typedArray.getDimension(R.styleable.Weather24HoursView_time_text_size,
                DEFAULT_TEXT_SIZE)
            mHistoryTimeTextColor =
                typedArray.getColor(R.styleable.Weather24HoursView_history_time_text_color,
                    DEFAULT_HISTORY_TIME_TEXT_COLOR)
            mFeatureTimeTextColor =
                typedArray.getColor(R.styleable.Weather24HoursView_feature_time_text_color,
                    DEFAULT_TEXT_COLOR)
            mWindPowerLabelTextSize =
                typedArray.getDimension(R.styleable.Weather24HoursView_wind_power_label_text_size,
                    DEFAULT_TEXT_SIZE)
            mWindPowerLabelTextColor =
                typedArray.getColor(R.styleable.Weather24HoursView_wind_power_label_text_color,
                    DEFAULT_TEXT_COLOR)
            mWindPowerTextSize =
                typedArray.getDimension(R.styleable.Weather24HoursView_wind_power_text_size,
                    DEFAULT_TEXT_SIZE)
            mWindPowerTextColor =
                typedArray.getColor(R.styleable.Weather24HoursView_wind_power_text_color,
                    DEFAULT_TEXT_COLOR)
            mAirQualityLabelTextSize =
                typedArray.getDimension(R.styleable.Weather24HoursView_air_quality_label_text_size,
                    DEFAULT_TEXT_SIZE)
            mAirQualityLabelTextColor =
                typedArray.getColor(R.styleable.Weather24HoursView_air_quality_label_text_color,
                    DEFAULT_TEXT_COLOR)
            mAirQualityTextSize =
                typedArray.getDimension(R.styleable.Weather24HoursView_air_quality_text_size,
                    DEFAULT_TEXT_SIZE)
            mTemperatureLabelTextSize =
                typedArray.getDimension(R.styleable.Weather24HoursView_temperature_label_text_size,
                    DEFAULT_TEXT_SIZE)
            mTemperatureLabelTextColor =
                typedArray.getColor(R.styleable.Weather24HoursView_temperature_label_text_color,
                    DEFAULT_TEXT_COLOR)
            mWeatherPopupWindowColor =
                typedArray.getColor(R.styleable.Weather24HoursView_weather_popup_window_color,
                    DEFAULT_WEATHER_POPUP_WINDOW_COLOR)
            mWeatherPopupWindowTextColor =
                typedArray.getColor(R.styleable.Weather24HoursView_weather_popup_window_text_color,
                    DEFAULT_WEATHER_POPUP_WINDOW_TEXT_COLOR)
            mWeatherPopupWindowTextSize =
                typedArray.getDimension(R.styleable.Weather24HoursView_weather_popup_window_text_size,
                    DEFAULT_TEXT_SIZE)
            mHistoryTemperatureLineChartColor =
                typedArray.getColor(R.styleable.Weather24HoursView_history_temperature_line_chart_color,
                    DEFAULT_HISTORY_TEMPERATURE_LINE_CHART_COLOR)
            mFeatureTemperatureLineChartColor =
                typedArray.getColor(R.styleable.Weather24HoursView_feature_temperature_line_chart_color,
                    DEFAULT_FEATURE_TEMPERATURE_LINE_CHART_COLOR)
            typedArray.recycle()
        }
    }

    private fun initPaint() {
        mYRetFPaint.apply {
            color = if (background is ColorDrawable) {
                (background as ColorDrawable).color
            } else {
                Color.WHITE
            }

            style = Paint.Style.FILL
            isAntiAlias = true

        }
        mTimeTextPaint.apply {
            color = mFeatureTimeTextColor
            textSize = mTimeTextSize
            isAntiAlias = true

        }
        mWindPowerLabelTextPaint.apply {
            color = mWindPowerLabelTextColor
            textSize = mWindPowerLabelTextSize
            isAntiAlias = true
        }
        mWindPowerTextPaint.apply {
            color = mWindPowerTextColor
            textSize = mWindPowerTextSize
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        mWindPowerBoxPaint.apply {
            color = getColor(R.color.pale)
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        mAirQualityLabelTextPaint.apply {
            color = mAirQualityLabelTextColor
            textSize = mAirQualityLabelTextSize
            isAntiAlias = true
        }
        mAirQualityTextPaint.apply {
            color = Color.GREEN
            textSize = mAirQualityTextSize
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        mAirQualityBoxPaint.apply {
            color = getColor(R.color.orange)
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        mTemperatureLabelTextPaint.apply {
            color = mTemperatureLabelTextColor
            textSize = mTemperatureLabelTextSize
            isAntiAlias = true
        }
        mCurrentTemperaturePointPaint.apply {
            color = getColor(R.color.sky_blue)
            isAntiAlias = true
        }
        mTemperatureLinePaint.apply {
            color = mFeatureTemperatureLineChartColor
            strokeWidth = dp2px(2f).toFloat()
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        mWeatherPopupWindowPaint.apply {
            color = mWeatherPopupWindowColor
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        mWeatherPopupWindowTextPaint.apply {
            color = mWeatherPopupWindowTextColor
            textSize = mWeatherPopupWindowTextSize
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        mTemperatureGradientBoxPaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    private fun initParams() {
        var fontMetrics = mTimeTextPaint.fontMetrics
        mTimeTextHeight =
            (ceil((fontMetrics.descent - fontMetrics.ascent).toDouble()) + 2f).toFloat()

        mWindPowerBoxHeight = mYScaleSize * 2
        mAirQualityBoxHeight = mYScaleSize
        mTemperatureBoxHeight = mYScaleSize * 7

        fontMetrics = mWindPowerLabelTextPaint.fontMetrics
        mWindPowerTextHeight =
            (ceil((fontMetrics.descent - fontMetrics.ascent).toDouble()) + 2f).toFloat()

        fontMetrics = mWeatherPopupWindowTextPaint.fontMetrics
        mWeatherPopupWindowTextHeight =
            (ceil((fontMetrics.descent - fontMetrics.ascent).toDouble()) + 2f).toFloat()
        mWeatherPopupWindowHeight = DEFAULT_SEPARATOR + mWeatherPopupWindowTextHeight

        mWindPowderRoundRectRadius = mWindPowerBoxHeight * 0.25f
        mAirQualityRoundRectRadius = mAirQualityBoxHeight * 0.5f
        mWeatherPopupWindowRoundRectRadius = mWeatherPopupWindowHeight * 0.5f

        mWidth = mData?.run {
            (mYLabelWidth + mXScaleSize * hourlyWeatherInfoList.size + DEFAULT_SEPARATOR * 2
                    + paddingStart + paddingEnd + marginStart + marginEnd).toInt()
        } ?: getScreenWidth()
        mHeight = (DEFAULT_SEPARATOR + mTimeTextHeight + mWindPowerBoxHeight
                + DEFAULT_SEPARATOR + mAirQualityBoxHeight + DEFAULT_SEPARATOR
                + mTemperatureBoxHeight + mWeatherPopupWindowTextHeight + DEFAULT_SEPARATOR
                + marginTop + marginBottom + paddingTop + paddingBottom).toInt()

        mStartX = mYLabelWidth + paddingStart + marginStart + DEFAULT_SEPARATOR * 0.5f
        mWindPowerStartY =
            mHeight - DEFAULT_SEPARATOR - mTimeTextHeight - marginBottom - paddingBottom
        mAirQualityStartY = (mWindPowerStartY - mWindPowerBoxHeight - DEFAULT_SEPARATOR)
        mTemperatureStartY =
            (mAirQualityStartY - (mAirQualityBoxHeight + (mTemperatureBoxHeight - mTimeTextHeight) * 0.5f + DEFAULT_SEPARATOR))
    }

    private fun initGradient() {
        val y = mTemperatureStartY + mTemperatureBoxHeight * 0.5f
        mTemperatureGradientUnselected = LinearGradient(0f,
            y - mTemperatureBoxHeight - DEFAULT_SEPARATOR,
            0f,
            y,
            getColor(R.color.little_blue),
            Color.WHITE,
            Shader.TileMode.CLAMP)
        mTemperatureGradientSelected = LinearGradient(0f,
            y - mTemperatureBoxHeight - DEFAULT_SEPARATOR,
            0f,
            y,
            getColor(R.color.sky_blue),
            Color.WHITE,
            Shader.TileMode.CLAMP)
    }

    private fun calculateItemIndex(): Int {
        return mData?.let {
            val x: Float = getScrollBarX()
            var sum = 0f
            for (i in 0 until it.hourlyWeatherInfoList.size) {
                sum += mXScaleSize
                if (x < sum) {
                    return i
                }
            }
            it.hourlyWeatherInfoList.size - 1
        } ?: 0
    }

    private fun getScrollBarX(): Float {
        return mData?.let {
            if (mMaxScrollOffset == 0) {
                0f
            } else {
                it.hourlyWeatherInfoList.size * mXScaleSize * mScrollOffset / mMaxScrollOffset
            }
        } ?: 0f
    }

    private fun updateTemperaturePointList() {
        mData?.run {
            val temperatureScale =
                (mTemperatureBoxHeight * 0.5f) / (mMaxTemperature - mMinTemperature)
            var x = mStartX
            var y: Float
            mTemperaturePointList.clear()
            hourlyWeatherInfoList.forEachIndexed { index, value ->
                y = (mTemperatureStartY - (value.temperature - minTemperature) * temperatureScale)
                mTemperaturePointList.add(Point2D(x, y))
                x += mXScaleSize
                if ((hourlyWeatherInfoList.size - 1) == index) {
                    mTemperaturePointList.add(Point2D(x, y))
                }
            }
        }
    }

    /**
     * 绘制Y轴上的文字
     */
    private fun drawLabelInY(canvas: Canvas) {
        canvas.run {
            val x = (paddingStart + marginStart + mScrollOffset).toFloat()
            var y = mWindPowerStartY
            drawRect(RectF(0f, 0f, x + mYLabelWidth, mHeight.toFloat()), mYRetFPaint)
            y -= (mWindPowerBoxHeight * 0.5f - mTimeTextHeight * 0.5f)
            drawText("风力", x, y, mWindPowerLabelTextPaint)
            y = mAirQualityStartY
            drawText("空气", x, y, mAirQualityLabelTextPaint)
            y = mTemperatureStartY
            drawText("$mMinTemperature°", x, y, mTemperatureLabelTextPaint)
            y -= mTemperatureBoxHeight * 0.5f
            drawText("$mMaxTemperature°", x, y, mTemperatureLabelTextPaint)
        }
    }

    /**
     * 绘制x轴上的标签
     */
    private fun drawTime(canvas: Canvas) {
        canvas.run {
            val x = mStartX
            val y = mHeight - DEFAULT_SEPARATOR
            Log.i(TAG, "X 轴 底部文字起始 x = $x , y = $y")
            for (i in 0 until 24) {
                if (i % 2 == 0) {
                    if (i < mCurrentTime) {
                        mTimeTextPaint.color = mHistoryTimeTextColor
                    } else {
                        mTimeTextPaint.color = mFeatureTimeTextColor
                    }
                    drawText("$i:00", (mXScaleSize * i + x), y, mTimeTextPaint)
                }
            }
        }
    }

    /**
     * 绘制风力
     */
    private fun drawWindPower(canvas: Canvas) {
        mWindPowerList?.let {
            var left = mStartX
            val bottom = mWindPowerStartY
            var right = left
            val top = bottom - mWindPowerBoxHeight
            var rect: RectF

            canvas.run {
                it.forEach { _, value ->
                    val path = Path()
                    left = right
                    right = left + mXScaleSize * value.size
                    rect = RectF(left, top, right - DEFAULT_SPACE, bottom)
                    path.addRoundRect(rect,
                        floatArrayOf(mWindPowderRoundRectRadius, mWindPowderRoundRectRadius,
                            mWindPowderRoundRectRadius, mWindPowderRoundRectRadius,
                            0f, 0f, 0f, 0f),
                        Path.Direction.CW)
                    drawPath(path, mWindPowerBoxPaint)
                    drawText("${value.entries.first().value}级风", (left + right) * 0.5f,
                        bottom - mWindPowerBoxHeight * 0.25f,
                        mWindPowerTextPaint)

                }
            }
        }
    }

    /**
     * 绘制空气质量
     */
    private fun drawAirQuality(canvas: Canvas) {
        mData?.let {
            var left = mStartX
            var right = left
            val bottom = mAirQualityStartY
            val top = mAirQualityStartY - mAirQualityBoxHeight
            var rect: RectF

            canvas.run {
                it.hourlyWeatherInfoList.forEachIndexed { index, value ->
                    val path = Path()
                    left = right
                    right = left + mXScaleSize
                    rect = RectF(left, top, right - DEFAULT_SPACE, bottom)
                    path.addRoundRect(rect,
                        floatArrayOf(mAirQualityRoundRectRadius, mAirQualityRoundRectRadius,
                            mAirQualityRoundRectRadius, mAirQualityRoundRectRadius, 0f, 0f, 0f, 0f),
                        Path.Direction.CW)

                    mAirQualityBoxPaint.color =
                        getAirQualityColor(value.airQuality, index == mCurrentIndex)

                    if (index == mCurrentIndex) {
                        mAirQualityTextPaint.color = mAirQualityBoxPaint.color
                        drawText(context.resources.getString(value.airQuality.qualityRes),
                            (left + right) * 0.5f,
                            top - DEFAULT_SPACE,
                            mAirQualityTextPaint)
                    }
                    drawPath(path, mAirQualityBoxPaint)
                }
            }
        }
    }

    /**
     * 绘制温度
     */
    private fun drawTemperature(canvas: Canvas) {
        mData?.let {
            val infoList = it.hourlyWeatherInfoList
            canvas.run {
                if (mTemperaturePointList.isEmpty()) {
                    return
                }
                drawableWeatherGradientBox(canvas, infoList)
                drawTemperatureLineChart(canvas)
                drawWeatherPopupWindow(canvas, infoList)
            }
        }
    }

    /**
     * 绘制天气信息渐变区域
     */
    private fun drawableWeatherGradientBox(canvas: Canvas, infoList: ArrayList<HourlyWeatherInfo>) {
        canvas.run {
            if (mTemperaturePointList.isEmpty()) {
                return
            }
            val indexList = ArrayList<Int>()
            var perPoint: Point2D = mTemperaturePointList[0]
            var nextPoint: Point2D
            var startPoint: Point2D = perPoint
            var centerPointX: Float
            var centerPoint1: Point2D
            var centerPoint2: Point2D

            var weather: EnumWeather = EnumWeather.SUNNY
            var weatherIcon: EnumWeatherIcon = EnumWeatherIcon.NONA

            var weatherPath = Path()
            infoList.forEachIndexed { index, item ->
                if (index == 0) {
                    //perPoint = mTemperaturePointList[index]
                    indexList.add(index)
                    weather = item.weather
                    weatherIcon = item.weatherIcon
                    weatherPath = Path()
                    weatherPath.moveTo(perPoint.x, perPoint.y)
                    return@forEachIndexed
                }
                nextPoint = mTemperaturePointList[index]
                centerPointX = (perPoint.x + mXScaleSize * 0.5f)
                centerPoint1 = Point2D(centerPointX, perPoint.y)
                centerPoint2 = Point2D(centerPointX, nextPoint.y)


                if (weather != item.weather || index == infoList.size - 1) {
                    weatherPath.cubicTo(centerPoint1.x, centerPoint1.y,
                        centerPoint2.x, centerPoint2.y,
                        nextPoint.x - DEFAULT_SPACE, nextPoint.y)

                    if (index == infoList.size - 1) {
                        nextPoint = getEndPoint(index)
                        weatherPath.cubicTo(getCenterPoint1(index).x, getCenterPoint1(index).y,
                            getCenterPoint2(index).x, getCenterPoint2(index).y,
                            nextPoint.x, nextPoint.y)
                        indexList.add(index)
                    }
                    weatherPath.lineTo(nextPoint.x - DEFAULT_SPACE,
                        mAirQualityStartY - mAirQualityBoxHeight)
                    weatherPath.lineTo(startPoint.x, mAirQualityStartY - mAirQualityBoxHeight)
                    weatherPath.lineTo(startPoint.x, startPoint.y)
                    weatherPath.close()
                    if (mCurrentIndex in indexList) {
                        mTemperatureGradientBoxPaint.shader = mTemperatureGradientSelected
                    } else {
                        mTemperatureGradientBoxPaint.shader = mTemperatureGradientUnselected
                    }
                    drawPath(weatherPath, mTemperatureGradientBoxPaint)

                    drawableWeatherIcon(this,
                        ((nextPoint.x + startPoint.x) * 0.5).toInt(),
                        (mAirQualityStartY - mAirQualityBoxHeight - mTemperatureBoxHeight * 0.5f).toInt(),
                        weatherIcon.iconRes)

                    indexList.clear()

                    weatherPath = Path()
                    weatherPath.moveTo(nextPoint.x, nextPoint.y)
                    startPoint = nextPoint

                    weather = item.weather
                    weatherIcon = item.weatherIcon
                    indexList.add(index)
                } else {
                    weatherPath.cubicTo(centerPoint1.x, centerPoint1.y,
                        centerPoint2.x, centerPoint2.y,
                        nextPoint.x, nextPoint.y)
                    indexList.add(index)
                }
                perPoint = nextPoint
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
     * 绘制温度曲线
     */
    private fun drawTemperatureLineChart(canvas: Canvas) {
        canvas.run {
            val path = Path()
            val historyPath = Path()
            var perPoint = mTemperaturePointList[0]
            var nextPoint: Point2D
            var centerPointX: Float
            var centerPoint1: Point2D
            var centerPoint2: Point2D

            path.moveTo(perPoint.x, perPoint.y)
            historyPath.moveTo(perPoint.x, perPoint.y)
            mTemperaturePointList.forEachIndexed { index, point ->
                if (index == 0) {
                    return@forEachIndexed
                }
                nextPoint = point
                centerPointX = (perPoint.x + mXScaleSize * 0.5f)
                centerPoint1 = Point2D(centerPointX, perPoint.y)
                centerPoint2 = Point2D(centerPointX, nextPoint.y)
                path.cubicTo(centerPoint1.x, centerPoint1.y,
                    centerPoint2.x, centerPoint2.y,
                    nextPoint.x, nextPoint.y)

                if (index < mCurrentTime) {
                    historyPath.cubicTo(centerPoint1.x, centerPoint1.y,
                        centerPoint2.x, centerPoint2.y,
                        nextPoint.x, nextPoint.y)
                }
                perPoint = nextPoint
            }
            mTemperatureLinePaint.color = mFeatureTemperatureLineChartColor
            drawPath(path, mTemperatureLinePaint)
            mTemperatureLinePaint.color = mHistoryTemperatureLineChartColor
            drawPath(historyPath, mTemperatureLinePaint)
        }

    }

    /**
     * 绘制天气气泡窗
     */
    private fun drawWeatherPopupWindow(canvas: Canvas, infoList: ArrayList<HourlyWeatherInfo>) {
        canvas.run {
            val rectF = RectF()
            val path = Path()
            val textStartX: Float
            val textStartY: Float
            val temperaturePoint: Point2D
            val info = infoList[mCurrentIndex]
            val content =
                "${info.time}点 ${context.resources.getString(info.weather.weatherRes)} ${info.temperature}°"
            val t = (getScrollBarX() - mXScaleSize * (mCurrentIndex)) / (mXScaleSize * 1.0f)

            temperaturePoint =
                calculateBezierPointForCubic(t,
                    getStartPoint(mCurrentIndex),
                    getCenterPoint1(mCurrentIndex),
                    getCenterPoint2(mCurrentIndex),
                    getEndPoint(mCurrentIndex))

            if (mCurrentIndex < (infoList.size * 0.5)) {
                rectF.left = temperaturePoint.x
                rectF.bottom = temperaturePoint.y - DEFAULT_SEPARATOR
                rectF.right =
                    rectF.left + mWeatherPopupWindowTextPaint.measureText(content) + DEFAULT_SEPARATOR * 2
                rectF.top = (rectF.bottom - mWeatherPopupWindowHeight)
                textStartX = (rectF.left + rectF.right) * 0.5f
                textStartY =
                    rectF.bottom - (mWeatherPopupWindowHeight * 0.5f - mWeatherPopupWindowTextHeight * 0.333f)
                path.addRoundRect(rectF,
                    floatArrayOf(mWeatherPopupWindowRoundRectRadius,
                        mWeatherPopupWindowRoundRectRadius,
                        mWeatherPopupWindowRoundRectRadius,
                        mWeatherPopupWindowRoundRectRadius,
                        mWeatherPopupWindowRoundRectRadius,
                        mWeatherPopupWindowRoundRectRadius, 0f, 0f),
                    Path.Direction.CW)
            } else {
                rectF.right = temperaturePoint.x
                rectF.bottom = temperaturePoint.y - DEFAULT_SEPARATOR
                rectF.left =
                    rectF.right - mWeatherPopupWindowTextPaint.measureText(content) - DEFAULT_SEPARATOR * 2
                rectF.top = (rectF.bottom - mWeatherPopupWindowHeight)
                textStartX = (rectF.left + rectF.right) * 0.5f
                textStartY =
                    rectF.bottom - (mWeatherPopupWindowHeight * 0.5f - mWeatherPopupWindowTextHeight * 0.333f)
                path.addRoundRect(rectF,
                    floatArrayOf(mWeatherPopupWindowRoundRectRadius,
                        mWeatherPopupWindowRoundRectRadius,
                        mWeatherPopupWindowRoundRectRadius,
                        mWeatherPopupWindowRoundRectRadius,
                        0f,
                        0f,
                        mWeatherPopupWindowRoundRectRadius,
                        mWeatherPopupWindowRoundRectRadius),
                    Path.Direction.CW)
            }

            //当前温度点
            drawCircle(temperaturePoint.x,
                temperaturePoint.y,
                10f,
                mCurrentTemperaturePointPaint)
            //当前温度提示信息
            drawPath(path, mWeatherPopupWindowPaint)
            drawText(content, textStartX, textStartY, mWeatherPopupWindowTextPaint)
        }
    }

    private fun getAirQualityColor(quality: EnumAirQuality, isSelected: Boolean): Int {
        val color: Int
        when (quality) {
            EnumAirQuality.AIR_QUALITY_EXCELLENT -> {
                color = if (isSelected) {
                    getColor(R.color.air_quality_excellent_selected)
                } else {
                    getColor(R.color.air_quality_excellent_unselected)
                }
            }
            EnumAirQuality.AIR_QUALITY_GOOD -> {
                color = if (isSelected) {
                    getColor(R.color.air_quality_good_selected)
                } else {
                    getColor(R.color.air_quality_good_unselected)
                }
            }
            EnumAirQuality.AIR_QUALITY_SLIGHTLY -> {
                color = if (isSelected) {
                    getColor(R.color.air_quality_slightly_selected)
                } else {
                    getColor(R.color.air_quality_slightly_unselected)
                }
            }
            EnumAirQuality.AIR_QUALITY_HEAVY-> {
                color = if (isSelected) {
                    getColor(R.color.air_quality_heavy_selected)
                } else {
                    getColor(R.color.air_quality_heavy_unselected)
                }
            }
        }
        return color
    }

    private fun getCenterPoint1(index: Int): Point2D {
        val startPoint = getStartPoint(index)
        return Point2D((startPoint.x + mXScaleSize * 0.5f), startPoint.y)
    }

    private fun getCenterPoint2(index: Int): Point2D {
        val startPoint = getStartPoint(index)
        return Point2D((startPoint.x + mXScaleSize * 0.5f), getEndPoint(index).y)
    }

    private fun getStartPoint(index: Int): Point2D {
        return mTemperaturePointList[index]
    }

    private fun getEndPoint(index: Int): Point2D {
        return mTemperaturePointList[index + 1]
    }

}