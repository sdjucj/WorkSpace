package com.cj.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cj.framework.http.HttpClient
import com.cj.framework.http.response.HttpObserver
import com.cj.framework.http.response.IResponseCallback
import com.cj.project.api.IApi
import com.cj.project.response.DayDetailInfo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}