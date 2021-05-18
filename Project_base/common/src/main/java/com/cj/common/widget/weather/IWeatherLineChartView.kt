package com.cj.common.widget.weather

/**
 * 温度折线图接口
 *
 * @author CJ
 * @date 2021/5/17 15:59
 */
interface IWeatherLineChartView<T> {

    /**
     * 设置scrollerView的滚动条的位置，通过位置计算当前的时段
     */
    fun setScrollOffset(scrollOffset: Int, maxScrollOffset: Int)

    /**
     * 设置天气数据
     */
    fun setWeatherData(data: T?)
}