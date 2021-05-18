package com.cj.common.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import com.cj.framework.utils.getScreenWidth

/**
 * 天气信息折线图
 *
 * @author CJ
 * @date 2021/5/11 15:52
 */
class WeatherLineChartView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    HorizontalScrollView(context, attrs, defStyleAttr) {

    private var mChildView: View? = null

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        mChildView = child
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        scrollOffset()
    }

    private fun scrollOffset() {
        val scrollOffset: Int = computeHorizontalScrollOffset()
        val maxScrollOffset: Int = computeHorizontalScrollRange() - getScreenWidth()
        mChildView?.let {
            if (it is IWeatherLineChartView<*>) {
                it.setScrollOffset(scrollOffset, maxScrollOffset)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> setWeatherData(data: T) {
        mChildView?.let {
            if (it is IWeatherLineChartView<*>) {
                (it as IWeatherLineChartView<T>).setWeatherData(data)
                invalidate()
            }
        }
    }

}