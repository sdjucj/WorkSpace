package com.cj.common.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import com.cj.common.bean.Weather24HoursInfo
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
    private var mData: Any? = null

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        mChildView = child
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        draw()
    }

    private fun draw() {
        val scrollOffset: Int = computeHorizontalScrollOffset()
        val maxScrollOffset: Int = computeHorizontalScrollRange() - getScreenWidth()
        mChildView?.let {
            when (it) {
                is Weather24HoursView -> {
                    it.setScrollOffset(scrollOffset, maxScrollOffset)
                }
            }
        }
    }

    fun setWeather24HoursData(data: Weather24HoursInfo) {
        mData = data
        mChildView?.let {
            if (it is Weather24HoursView) {
                it.updateData(data)
                invalidate()
            }
        }
    }
}