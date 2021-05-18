package com.cj.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import com.cj.common.bean.DayWeatherInfo
import com.cj.common.bean.HourlyWeatherInfo
import com.cj.common.bean.WeatherDaysInfo
import com.cj.common.bean.Weather24HoursInfo
import com.cj.common.enums.EnumAirQuality
import com.cj.common.enums.EnumWeather
import com.cj.common.enums.EnumWeatherIcon
import com.cj.common.widget.weather.Temperature40DaysView
import com.cj.common.widget.weather.Weather15DaysView
import com.cj.common.widget.weather.WeatherLineChartView
import com.cj.framework.http.HttpClient
import com.cj.framework.http.response.HttpObserver
import com.cj.framework.http.response.IResponseCallback

import com.cj.project.api.IApi
import com.cj.project.response.DayDetailInfo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weather24HoursView()
        weather15DaysView()
        temperature40DaysView()
        //httpClient()
    }

    private fun httpClient() {
        HttpClient.sInstance.getService(IApi::class.java)
            .getDayDetailInfo("2021-05-11")
            .compose(HttpClient.sInstance.applySchedulers())
            .subscribe(HttpObserver(
                object : IResponseCallback<DayDetailInfo> {
                    override fun onSucceed(result: DayDetailInfo?) {
                        Log.i("chj", "onSucceed")
                    }

                    override fun onError(message: String?) {
                        Log.i("chj", "onError : ${message}")
                    }

                })
            )
    }

    private fun weather24HoursView() {
        val weatherView = findViewById<WeatherLineChartView>(R.id.weather_view)
        val data = ArrayList<HourlyWeatherInfo>()
        data.add(HourlyWeatherInfo(0,
            22,
            EnumAirQuality.AIR_QUALITY_EXCELLENT,
            2,
            EnumWeather.SUNNY,
            EnumWeatherIcon.W0))
        data.add(HourlyWeatherInfo(1,
            23,
            EnumAirQuality.AIR_QUALITY_EXCELLENT,
            2,
            EnumWeather.SUNNY,
            EnumWeatherIcon.W0))
        data.add(HourlyWeatherInfo(2,
            23,
            EnumAirQuality.AIR_QUALITY_EXCELLENT,
            3,
            EnumWeather.SUNNY,
            EnumWeatherIcon.W0))
        data.add(HourlyWeatherInfo(3,
            -1,
            EnumAirQuality.AIR_QUALITY_EXCELLENT,
            3,
            EnumWeather.SUNNY,
            EnumWeatherIcon.W0))
        data.add(HourlyWeatherInfo(4,
            -1,
            EnumAirQuality.AIR_QUALITY_EXCELLENT,
            3,
            EnumWeather.SUNNY,
            EnumWeatherIcon.W0))
        data.add(HourlyWeatherInfo(5,
            -1,
            EnumAirQuality.AIR_QUALITY_GOOD,
            4,
            EnumWeather.CLOUDY,
            EnumWeatherIcon.W1))
        data.add(HourlyWeatherInfo(6,
            23,
            EnumAirQuality.AIR_QUALITY_GOOD,
            4,
            EnumWeather.CLOUDY,
            EnumWeatherIcon.W1))
        data.add(HourlyWeatherInfo(7,
            23,
            EnumAirQuality.AIR_QUALITY_GOOD,
            4,
            EnumWeather.CLOUDY,
            EnumWeatherIcon.W1))
        data.add(HourlyWeatherInfo(8,
            23,
            EnumAirQuality.AIR_QUALITY_GOOD,
            3,
            EnumWeather.CLOUDY,
            EnumWeatherIcon.W1))
        data.add(HourlyWeatherInfo(9,
            22,
            EnumAirQuality.AIR_QUALITY_GOOD,
            3,
            EnumWeather.CLOUDY,
            EnumWeatherIcon.W1))
        data.add(HourlyWeatherInfo(10,
            21,
            EnumAirQuality.AIR_QUALITY_GOOD,
            3,
            EnumWeather.CLOUDY,
            EnumWeatherIcon.W1))
        data.add(HourlyWeatherInfo(11,
            21,
            EnumAirQuality.AIR_QUALITY_SLIGHTLY,
            4,
            EnumWeather.OVERCAST,
            EnumWeatherIcon.W2))
        data.add(HourlyWeatherInfo(12,
            22,
            EnumAirQuality.AIR_QUALITY_SLIGHTLY,
            4,
            EnumWeather.OVERCAST,
            EnumWeatherIcon.W2))
        data.add(HourlyWeatherInfo(13,
            22,
            EnumAirQuality.AIR_QUALITY_SLIGHTLY,
            4,
            EnumWeather.OVERCAST,
            EnumWeatherIcon.W2))
        data.add(HourlyWeatherInfo(14,
            23,
            EnumAirQuality.AIR_QUALITY_SLIGHTLY,
            4,
            EnumWeather.OVERCAST,
            EnumWeatherIcon.W2))
        data.add(HourlyWeatherInfo(15,
            23,
            EnumAirQuality.AIR_QUALITY_SLIGHTLY,
            2,
            EnumWeather.OVERCAST,
            EnumWeatherIcon.W2))
        data.add(HourlyWeatherInfo(16,
            24,
            EnumAirQuality.AIR_QUALITY_HEAVY,
            2,
            EnumWeather.THUNDERSHOWERS,
            EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(17,
            24,
            EnumAirQuality.AIR_QUALITY_HEAVY,
            2,
            EnumWeather.THUNDERSHOWERS,
            EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(18,
            25,
            EnumAirQuality.AIR_QUALITY_HEAVY,
            3,
            EnumWeather.THUNDERSHOWERS,
            EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(19,
            25,
            EnumAirQuality.AIR_QUALITY_HEAVY,
            3,
            EnumWeather.THUNDERSHOWERS,
            EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(20,
            25,
            EnumAirQuality.AIR_QUALITY_HEAVY,
            3,
            EnumWeather.THUNDERSHOWERS,
            EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(21,
            25,
            EnumAirQuality.AIR_QUALITY_HEAVY,
            5,
            EnumWeather.THUNDERSHOWERS,
            EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(22,
            25,
            EnumAirQuality.AIR_QUALITY_HEAVY,
            5,
            EnumWeather.THUNDERSHOWERS,
            EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(23,
            24,
            EnumAirQuality.AIR_QUALITY_HEAVY,
            5,
            EnumWeather.THUNDERSHOWERS,
            EnumWeatherIcon.W5))


        val list = SparseArray<LinkedHashMap<Int, Int>>()
        val map = LinkedHashMap<Int, Int>()
        var start = data[0]
        data.forEachIndexed { index, item ->
            if (index == 0) {
                map.put(item.time, item.windPower)
                return@forEachIndexed
            }

            if (start.windPower == item.windPower) {
                map.put(item.time, item.windPower)
            } else {
                list.put(index, LinkedHashMap(map))
                map.clear()
                map.put(item.time, item.windPower)
                start = item
            }
            if (index == data.size - 1 && map.isNotEmpty()) {
                list.put(index, LinkedHashMap(map))
                map.clear()
            }
        }

        weatherView.setWeatherData(Weather24HoursInfo(12f, -1, 25, data, list))
    }

    private fun weather15DaysView() {
        val weatherView = findViewById<WeatherLineChartView>(R.id.weather_15_days_view)
        val data = ArrayList<DayWeatherInfo>()
        data.add(DayWeatherInfo("05/16", "昨天", 15, 23,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "西北风", "4-5级",
            EnumAirQuality.AIR_QUALITY_EXCELLENT))
        data.add(DayWeatherInfo("05/17", "今天", 17, 23,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "西北风", "1级",
            EnumAirQuality.AIR_QUALITY_EXCELLENT))
        data.add(DayWeatherInfo("05/18", "星期二", 17, 25,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "1级",
            EnumAirQuality.AIR_QUALITY_GOOD))
        data.add(DayWeatherInfo("05/19", "星期三", 18, 26,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "3-4级",
            EnumAirQuality.AIR_QUALITY_GOOD))
        data.add(DayWeatherInfo("05/20", "星期四", 20, 26,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东北风", "1级",
            EnumAirQuality.AIR_QUALITY_HEAVY))
        data.add(DayWeatherInfo("05/21", "星期五", 20, 30,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "1级",
            EnumAirQuality.AIR_QUALITY_HEAVY))
        data.add(DayWeatherInfo("05/22", "星期六", 21, 31,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "2级",
            null))
        data.add(DayWeatherInfo("05/23", "星期日", 20, 24,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "西风", "2级",
            null))
        data.add(DayWeatherInfo("05/24", "星期一", 18, 28,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "2级",
            null))
        data.add(DayWeatherInfo("05/25", "星期二", 20, 26,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "3-4级",
            null))
        data.add(DayWeatherInfo("05/26", "星期三", 20, 24,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东北风", "1级",
            null))
        data.add(DayWeatherInfo("05/27", "星期四", 24, 27,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "南风", "1级",
            null))
        data.add(DayWeatherInfo("05/28", "星期无", 17, 27,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "1级",
            null))
        data.add(DayWeatherInfo("05/29", "星期六", 18, 25,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "1级",
            null))
        data.add(DayWeatherInfo("05/30", "星期日", 18, 22,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "北风", "2级",
            null))
        data.add(DayWeatherInfo("05/31", "星期一", 20, 28,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "2级",
            null))

        val info = WeatherDaysInfo(15,31,data)
        weatherView.setWeatherData(info)
        findViewById<Weather15DaysView>(R.id.weather_15_info).setOnItemClickListener(object :Weather15DaysView.OnItemClickListener{
            override fun onItemClick(position: Int, item: DayWeatherInfo?) {
                Log.i("chen","p = $position , date = ${item?.date}")
            }

        })
    }

    private fun temperature40DaysView() {
        val view = findViewById<Temperature40DaysView>(R.id.temperature_40_days)
        val data = ArrayList<DayWeatherInfo>()
        data.add(DayWeatherInfo("05/16", "昨天", 15, 23,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "西北风", "4-5级",
            EnumAirQuality.AIR_QUALITY_EXCELLENT))
        data.add(DayWeatherInfo("05/17", "今天", 17, 23,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "西北风", "1级",
            EnumAirQuality.AIR_QUALITY_EXCELLENT))
        data.add(DayWeatherInfo("05/18", "星期二", 17, 25,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "1级",
            EnumAirQuality.AIR_QUALITY_GOOD))
        data.add(DayWeatherInfo("05/19", "星期三", 18, 26,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "3-4级",
            EnumAirQuality.AIR_QUALITY_GOOD))
        data.add(DayWeatherInfo("05/20", "星期四", 20, 26,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东北风", "1级",
            EnumAirQuality.AIR_QUALITY_HEAVY))
        data.add(DayWeatherInfo("05/21", "星期五", 20, 30,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "1级",
            EnumAirQuality.AIR_QUALITY_HEAVY))
        data.add(DayWeatherInfo("05/22", "星期六", 21, 31,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "2级",
            null))
        data.add(DayWeatherInfo("05/23", "星期日", 20, 24,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "西风", "2级",
            null))
        data.add(DayWeatherInfo("05/24", "星期一", 18, 28,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "2级",
            null))
        data.add(DayWeatherInfo("05/25", "星期二", 20, 26,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "3-4级",
            null))
        data.add(DayWeatherInfo("05/26", "星期三", 20, 24,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东北风", "1级",
            null))
        data.add(DayWeatherInfo("05/27", "星期四", 24, 27,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "南风", "1级",
            null))
        data.add(DayWeatherInfo("05/28", "星期无", 17, 27,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "1级",
            null))
        data.add(DayWeatherInfo("05/29", "星期六", 18, 25,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "1级",
            null))
        data.add(DayWeatherInfo("05/30", "星期日", 18, 22,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "北风", "2级",
            null))
        data.add(DayWeatherInfo("05/31", "星期一", 20, 28,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "2级",
            null))
        data.add(DayWeatherInfo("06/01", "昨天", 15, 23,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "西北风", "4-5级",
            EnumAirQuality.AIR_QUALITY_EXCELLENT))
        data.add(DayWeatherInfo("06/02", "今天", 17, 23,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "西北风", "1级",
            EnumAirQuality.AIR_QUALITY_EXCELLENT))
        data.add(DayWeatherInfo("06/03", "星期二", 17, 25,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "1级",
            EnumAirQuality.AIR_QUALITY_GOOD))
        data.add(DayWeatherInfo("06/04", "星期三", 18, 26,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "3-4级",
            EnumAirQuality.AIR_QUALITY_GOOD))
        data.add(DayWeatherInfo("06/05", "星期四", 20, 26,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东北风", "1级",
            EnumAirQuality.AIR_QUALITY_HEAVY))
        data.add(DayWeatherInfo("06/06", "星期五", 20, 30,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "1级",
            EnumAirQuality.AIR_QUALITY_HEAVY))
        data.add(DayWeatherInfo("06/07", "星期六", 21, 31,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "2级",
            null))
        data.add(DayWeatherInfo("06/08", "星期日", 20, 24,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "西风", "2级",
            null))
        data.add(DayWeatherInfo("06/09", "星期一", 18, 28,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "2级",
            null))
        data.add(DayWeatherInfo("06/10", "星期二", 20, 26,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "3-4级",
            null))
        data.add(DayWeatherInfo("06/11", "星期三", 20, 24,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东北风", "1级",
            null))
        data.add(DayWeatherInfo("06/12", "星期四", 24, 27,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "南风", "1级",
            null))
        data.add(DayWeatherInfo("06/13", "星期无", 17, 27,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "1级",
            null))
        data.add(DayWeatherInfo("06/14", "星期六", 18, 25,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "1级",
            null))
        data.add(DayWeatherInfo("06/15", "星期日", 18, 22,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "北风", "2级",
            null))
        data.add(DayWeatherInfo("06/16", "星期一", 20, 28,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "2级",
            null))
        data.add(DayWeatherInfo("06/17", "星期日", 20, 24,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "西风", "2级",
            null))
        data.add(DayWeatherInfo("06/18", "星期一", 18, 28,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "2级",
            null))
        data.add(DayWeatherInfo("06/19", "星期二", 20, 26,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "3-4级",
            null))
        data.add(DayWeatherInfo("06/20", "星期三", 20, 24,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东北风", "1级",
            null))
        data.add(DayWeatherInfo("06/21", "星期四", 24, 27,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "南风", "1级",
            null))
        data.add(DayWeatherInfo("06/22", "星期无", 17, 27,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东风", "1级",
            null))
        data.add(DayWeatherInfo("06/23", "星期六", 18, 25,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "东南风", "1级",
            null))
        data.add(DayWeatherInfo("06/24", "星期日", 18, 22,
            EnumWeather.CLOUDY, EnumWeather.CLOUDY,
            EnumWeatherIcon.W1, EnumWeatherIcon.W1, "北风", "2级",
            null))

        val info = WeatherDaysInfo(22,31,data)
        view.updateData(info)
        /*findViewById<Weather15DaysView>(R.id.weather_15_info).setOnItemClickListener(object :Weather15DaysView.OnItemClickListener{
            override fun onItemClick(position: Int, item: DayWeatherInfo?) {
                Log.i("chen","p = $position , date = ${item?.date}")
            }

        })*/
    }
}