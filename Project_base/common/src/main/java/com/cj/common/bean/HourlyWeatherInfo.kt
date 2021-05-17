package com.cj.common.bean

import com.cj.common.enums.EnumAirQuality
import com.cj.common.enums.EnumWeather
import com.cj.common.enums.EnumWeatherIcon

/**
 * 每小时的天气信息
 *
 * @author CJ
 * @date 2021/5/11 16:05
 */

/**
 * @param time 时间
 * @param temperature 温度
 * @param airQuality 空气质量
 * @param windPower 风力
 * @param weather 天气类型
 * @param weatherIcon 天气类型图标
 */
data class HourlyWeatherInfo(
    var time: Int,
    var temperature: Int,
    var airQuality: EnumAirQuality,
    var windPower: Int,
    var weather: EnumWeather,
    var weatherIcon: EnumWeatherIcon,
)
