package com.cj.project.api

import com.cj.framework.http.HttpClient
import com.cj.project.response.DayDetailInfoResponse
import io.reactivex.Observable
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * TODO
 * @author CJ
 * @date 2021/5/11 14:12
 */
interface IApi {

    @Headers(HttpClient.GLOBAL_DOMAIN_CALENDAR)
    @POST("/calendarPlatform/forward")
    fun getDayDetailInfo(@Query("busiCode") date: String): Observable<DayDetailInfoResponse>

}