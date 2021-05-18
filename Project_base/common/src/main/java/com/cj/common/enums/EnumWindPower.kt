package com.cj.common.enums

import androidx.annotation.StringRes
import com.cj.common.R

/**
 * 空气质量Enum
 *
 * @author CJ
 * @date 2021/5/17 10:42
 */
enum class EnumAirQuality(
    private val quality: Int,
    @field:StringRes @get:StringRes
    @param:StringRes var qualityRes: Int,
) {

    AIR_QUALITY_EXCELLENT(0, R.string.weather_air_quality_excellent),//优
    AIR_QUALITY_GOOD(1, R.string.weather_air_quality_good),//良
    AIR_QUALITY_SLIGHTLY(2, R.string.weather_air_quality_slightly),//轻度
    AIR_QUALITY_HEAVY(3, R.string.weather_air_quality_heavy)//重度
}