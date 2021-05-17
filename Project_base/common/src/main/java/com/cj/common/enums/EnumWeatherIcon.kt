package com.cj.common.enums

import androidx.annotation.DrawableRes
import com.cj.common.R

/**
 * 天气图标
 *
 * @author CJ
 * @date 2021/4/7 10:22
 */
enum class EnumWeatherIcon(
    private val iconId: Int,
    @field:DrawableRes
    @get:DrawableRes
    @param:DrawableRes
    val iconRes: Int,
) {
    W0(0, R.mipmap.weather_0),
    W1(1, R.mipmap.weather_1),
    W2(2, R.mipmap.weather_2),
    W3(3, R.mipmap.weather_3),
    W4(4, R.mipmap.weather_4),
    W5(5, R.mipmap.weather_5),
    W6(6, R.mipmap.weather_6),
    W7(7, R.mipmap.weather_7),
    W8(8, R.mipmap.weather_8),
    W9(9, R.mipmap.weather_9),
    W10(10, R.mipmap.weather_10),
    W13(13, R.mipmap.weather_13),
    W14(14, R.mipmap.weather_14),
    W15(15, R.mipmap.weather_15),
    W16(16, R.mipmap.weather_16),
    W17(17, R.mipmap.weather_17),
    W18(18, R.mipmap.weather_18),
    W19(19, R.mipmap.weather_19),
    W20(20, R.mipmap.weather_20),
    W29(29, R.mipmap.weather_29),
    W30(30, R.mipmap.weather_30),
    W31(31, R.mipmap.weather_31),
    W32(32, R.mipmap.weather_32),
    W33(33, R.mipmap.weather_33),
    W34(34, R.mipmap.weather_34),
    W35(35, R.mipmap.weather_35),
    W36(36, R.mipmap.weather_36),
    NONA(44, R.mipmap.weather_44),
    W45(45, R.mipmap.weather_45),
    W46(46, R.mipmap.weather_46);

    companion object {
        fun findById(iconId: Int): EnumWeatherIcon {
            val icons = values()
            for (item in icons) {
                if (iconId == item.iconId) {
                    return item
                }
            }
            return NONA
        }
    }
}