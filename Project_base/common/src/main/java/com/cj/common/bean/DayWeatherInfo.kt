package com.cj.common.bean

import com.cj.common.enums.EnumAirQuality
import com.cj.common.enums.EnumWeather
import com.cj.common.enums.EnumWeatherIcon

/**
 * 每天的天气信息
 * @author CJ
 * @date 2021/5/17 14:44
 */

/**
 * @param date 日期
 * @param week 星期
 * @param minTemperature 最低温
 * @param maxTemperature 最高温
 * @param weatherFrom 天气变化
 * @param weatherTo 天气变化
 * @param weatherIconFrom 天气图标变化
 * @param weatherIconTo 天气图标变化
 * @param windDirection 风向
 * @param windPower 风力
 * @param airQuality 空气质量
 */
data class DayWeatherInfo(
    var date: String,
    var week: String,
    var minTemperature: Int,
    var maxTemperature: Int,
    var weatherFrom: EnumWeather,
    var weatherTo: EnumWeather,
    var weatherIconFrom: EnumWeatherIcon,
    var weatherIconTo: EnumWeatherIcon,
    var windDirection: String,
    var windPower: String,
    var airQuality: EnumAirQuality?,
)
