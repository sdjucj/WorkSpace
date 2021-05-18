package com.cj.common.bean

/**
 * 15天天气信息
 * @author CJ
 * @date 2021/5/17 14:42
 */

/**
 * @param minTemperature 15天中最低温
 * @param maxTemperature 15天中最高温
 * @param dayWeatherInfoList 天气信息列表
 */
data class Weather15DaysInfo(
    var minTemperature:Int,
    var maxTemperature:Int,
    var dayWeatherInfoList: ArrayList<DayWeatherInfo>,
)
