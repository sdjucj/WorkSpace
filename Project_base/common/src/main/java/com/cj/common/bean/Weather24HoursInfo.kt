package com.cj.common.bean

import android.util.SparseArray

/**
 * 24小时内天气信息
 *
 * @author CJ
 * @date 2021/5/14 11:21
 */

/**
 * @param currentTime 当前时间单位小时（24h）
 * @param minTemperature 最低温
 * @param maxTemperature 最高温
 * @param hourlyWeatherInfoList 24小时天气信息
 * @param windPowerList 风力信息
 */
data class Weather24HoursInfo(
    var currentTime: Float,
    var minTemperature: Int,
    var maxTemperature: Int,
    var hourlyWeatherInfoList: ArrayList<HourlyWeatherInfo>,
    var windPowerList:SparseArray<LinkedHashMap<Int,Int>>
)