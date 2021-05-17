package com.cj.common.enums

import androidx.annotation.StringRes
import com.cj.common.R

/**
 * 天气种类
 *
 * @author CJ
 * @date 2021/4/2 15:38
 */
enum class EnumWeather(
//小到中雪
    var mConditionId: Int,
    @field:StringRes @get:StringRes
    @param:StringRes var weatherRes: Int,
) {
    SUNNY(1, R.string.weather_sunny),  //晴
    CLEAR(5, R.string.weather_sunny),  //晴
    MOSTLY_SUNNY(6, R.string.weather_mostly_sunny),  //大部晴朗
    MOSTLY_CLEAR(7, R.string.weather_mostly_sunny),  //大部晴朗
    CLOUDY(8, R.string.weather_cloudy),  //多云
    PARTLY_CLOUDY(12, R.string.weather_partly_cloudy),  //少云
    OVERCAST(13, R.string.weather_overcast),  //阴
    SHOWERS(15, R.string.weather_shower),  //阵雨
    SCATTERED_SHOWERS(20, R.string.weather_scattered_shower),  //局部阵雨
    LIGHT_SHOWERS(22, R.string.weather_light_shower),  //小阵雨
    HEAVY_SHOWERS(23, R.string.weather_heavy_shower),  //强阵雨
    SNOW_SHOWERS(24, R.string.weather_snow_shower),  //阵雪
    LIGHT_SNOW_SHOWERS(25, R.string.weather_light_snow_shower),  //小阵雪
    FOG(26, R.string.weather_fog),  //雾
    FREEZING_FOG(28, R.string.weather_freezing_fog),  //冻雾
    SANDSTORM(29, R.string.weather_sandstorm),  //沙尘暴
    DUST(30, R.string.weather_dust),  //浮尘
    DUST_STORM(31, R.string.weather_dust_storm),  //尘卷风
    SAND(32, R.string.weather_sand_blowing),  //扬沙
    HEAVY_SANDSTORM(33, R.string.weather_severe_sandstorm),  //强沙尘暴
    HAZE(34, R.string.weather_haze),  //霾
    THUNDERSHOWERS(37, R.string.weather_thundershowers),  //雷阵雨
    LIGHTNING(42, R.string.weather_lightning),  //雷电
    THUNDERSTORMS(43, R.string.weather_thunderstorms),  //雷暴
    THUNDERSHOWERS_WITH_HAIL(44, R.string.weather_thunderstorms_with_hail),  //雷阵雨伴有冰雹
    HAIL(46, R.string.weather_hail),  //冰雹
    NEEDLE_ICE(47, R.string.weather_needle_ice),  //冰针
    ICY(48, R.string.weather_icy),  //冰粒
    SLEET(49, R.string.weather_sleet),  //雨夹雪
    LIGHT_RAIN(51, R.string.weather_light_rain),  //小雨
    MODERATE_RAIN(53, R.string.weather_moderate_rain),  //中雨
    HEAVY_RAIN(54, R.string.weather_heavy_rain),  //大雨
    RAINSTORM(55, R.string.weather_rainstorm),  //暴雨
    HEAVY_RAINSTORM(56, R.string.weather_downpour),  //大暴雨
    EXTREME_RAINSTORM(57, R.string.weather_torrential_rain),  //特大暴雨
    LIGHT_SNOW(58, R.string.weather_light_snow),  //小雪
    MODERATE_SNOW(60, R.string.weather_moderate_snow),  //中雪
    HEAVY_SNOW(62, R.string.weather_heavy_snow),  //大雪
    BLIZZARD(63, R.string.weather_snowstorm),  //暴雪
    FREEZING_RAIN(64, R.string.weather_ice_rain),  //冻雨
    SNOW(77, R.string.weather_snow),  //雪
    RAIN(78, R.string.weather_rain),  //雨
    MOSTLY_CLOUDY(85, R.string.weather_overcast),  //阴
    LIGHT_TO_MODERATE_RAIN(91, R.string.weather_light_to_moderate_rain),  //小到中雨
    MODERATE_TO_HEAVY_RAIN(92, R.string.weather_moderate_to_heavy_rain),  //中到大雨
    HEAVY_RAIN_TO_RAINSTORM(93, R.string.weather_heavy_rain_to_rainstorm),  //大到暴雨
    LIGHT_TO_MODERATE_SNOW(94, R.string.weather_light_to_moderate_snow);

    companion object {
        /**
         * 根据id找对应天气
         *
         * @param conditionId conditionId
         * @return 天气
         */
        fun findWidById(conditionId: Int): EnumWeather {
            val widList = values()
            for (wid in widList) {
                if (conditionId == wid.mConditionId) {
                    return wid
                }
            }
            return SUNNY
        }
    }
}