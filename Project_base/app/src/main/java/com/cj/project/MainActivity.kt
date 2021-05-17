package com.cj.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import com.cj.common.bean.HourlyWeatherInfo
import com.cj.common.bean.Weather24HoursInfo
import com.cj.common.enums.EnumAirQuality
import com.cj.common.enums.EnumWeather
import com.cj.common.enums.EnumWeatherIcon
import com.cj.common.widget.WeatherLineChartView
import com.cj.framework.http.HttpClient
import com.cj.framework.http.response.HttpObserver
import com.cj.framework.http.response.IResponseCallback

import com.cj.project.api.IApi
import com.cj.project.response.DayDetailInfo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherLineChartView()
        //httpClient()
    }

    private fun httpClient(){
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

    private fun weatherLineChartView(){
        val weatherView = findViewById<WeatherLineChartView>(R.id.weather_view)
        val data = ArrayList<HourlyWeatherInfo>()
        data.add(HourlyWeatherInfo(0, 22, EnumAirQuality.AIR_QUALITY_EXCELLENT, 2, EnumWeather.SUNNY,EnumWeatherIcon.W0))
        data.add(HourlyWeatherInfo(1, 23, EnumAirQuality.AIR_QUALITY_EXCELLENT, 2, EnumWeather.SUNNY,EnumWeatherIcon.W0))
        data.add(HourlyWeatherInfo(2, 23, EnumAirQuality.AIR_QUALITY_EXCELLENT, 3, EnumWeather.SUNNY,EnumWeatherIcon.W0))
        data.add(HourlyWeatherInfo(3, -1, EnumAirQuality.AIR_QUALITY_EXCELLENT, 3, EnumWeather.SUNNY,EnumWeatherIcon.W0))
        data.add(HourlyWeatherInfo(4, -1, EnumAirQuality.AIR_QUALITY_EXCELLENT, 3, EnumWeather.SUNNY,EnumWeatherIcon.W0))
        data.add(HourlyWeatherInfo(5, -1, EnumAirQuality.AIR_QUALITY_GOOD, 4, EnumWeather.CLOUDY,EnumWeatherIcon.W1))
        data.add(HourlyWeatherInfo(6, 23, EnumAirQuality.AIR_QUALITY_GOOD, 4, EnumWeather.CLOUDY,EnumWeatherIcon.W1))
        data.add(HourlyWeatherInfo(7, 23, EnumAirQuality.AIR_QUALITY_GOOD, 4, EnumWeather.CLOUDY,EnumWeatherIcon.W1))
        data.add(HourlyWeatherInfo(8, 23, EnumAirQuality.AIR_QUALITY_GOOD, 3, EnumWeather.CLOUDY,EnumWeatherIcon.W1))
        data.add(HourlyWeatherInfo(9, 22, EnumAirQuality.AIR_QUALITY_GOOD, 3, EnumWeather.CLOUDY,EnumWeatherIcon.W1))
        data.add(HourlyWeatherInfo(10, 21, EnumAirQuality.AIR_QUALITY_GOOD, 3, EnumWeather.CLOUDY,EnumWeatherIcon.W1))
        data.add(HourlyWeatherInfo(11, 21, EnumAirQuality.AIR_QUALITY_MEDIUM, 4, EnumWeather.OVERCAST,EnumWeatherIcon.W2))
        data.add(HourlyWeatherInfo(12, 22, EnumAirQuality.AIR_QUALITY_MEDIUM, 4, EnumWeather.OVERCAST,EnumWeatherIcon.W2))
        data.add(HourlyWeatherInfo(13, 22, EnumAirQuality.AIR_QUALITY_MEDIUM, 4, EnumWeather.OVERCAST,EnumWeatherIcon.W2))
        data.add(HourlyWeatherInfo(14, 23, EnumAirQuality.AIR_QUALITY_MEDIUM, 4, EnumWeather.OVERCAST,EnumWeatherIcon.W2))
        data.add(HourlyWeatherInfo(15, 23, EnumAirQuality.AIR_QUALITY_MEDIUM, 2, EnumWeather.OVERCAST,EnumWeatherIcon.W2))
        data.add(HourlyWeatherInfo(16, 24, EnumAirQuality.AIR_QUALITY_POOR, 2, EnumWeather.THUNDERSHOWERS,EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(17, 24, EnumAirQuality.AIR_QUALITY_POOR, 2, EnumWeather.THUNDERSHOWERS,EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(18, 25, EnumAirQuality.AIR_QUALITY_POOR, 3, EnumWeather.THUNDERSHOWERS,EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(19, 25, EnumAirQuality.AIR_QUALITY_POOR, 3, EnumWeather.THUNDERSHOWERS,EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(20, 25, EnumAirQuality.AIR_QUALITY_POOR, 3, EnumWeather.THUNDERSHOWERS,EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(21, 25, EnumAirQuality.AIR_QUALITY_POOR, 5, EnumWeather.THUNDERSHOWERS,EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(22, 25, EnumAirQuality.AIR_QUALITY_POOR, 5, EnumWeather.THUNDERSHOWERS,EnumWeatherIcon.W5))
        data.add(HourlyWeatherInfo(23, 24, EnumAirQuality.AIR_QUALITY_POOR, 5, EnumWeather.THUNDERSHOWERS,EnumWeatherIcon.W5))


        val list = SparseArray<LinkedHashMap<Int, Int>>()
        val map = LinkedHashMap<Int, Int>()
        var start = data[0]
        data.forEachIndexed { index, item ->
            if (index == 0) {
                map.put(item.time, item.windPower)
                return@forEachIndexed
            }

            if (start.windPower == item.windPower ) {
                map.put(item.time, item.windPower)
            } else {
                list.put(index, LinkedHashMap(map))
                map.clear()
                map.put(item.time, item.windPower)
                start = item
            }
            if(index == data.size - 1 && map.isNotEmpty()){
                list.put(index, LinkedHashMap(map))
                map.clear()
            }
        }

        weatherView.setWeather24HoursData(Weather24HoursInfo(12f, -1, 25, data, list))
    }
}