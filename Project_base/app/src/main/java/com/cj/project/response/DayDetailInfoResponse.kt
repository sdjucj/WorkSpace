package com.cj.project.response

import com.cj.framework.http.response.HttpResponse
import com.google.gson.Gson

/**
 * TODO
 * @author CJ
 * @date 2021/5/11 14:17
 */
class DayDetailInfoResponse : HttpResponse<DayDetailInfo>() {

    private var code: Int? = null
    private var msg: String? = null
    private var data: Data? = null
    private var ok: Boolean = false

    override fun isSucceed(): Boolean {
        return ok
    }

    override fun getErrorCode(): Int {
        return code ?: -1
    }

    override fun getMsg(): String? {
        return msg
    }

    override fun getData(): DayDetailInfo? {
        return data?.let {
            it.content?.run {
                Gson().fromJson(this, DayDetailInfo::class.java)
            }
        }
    }

    private class Data {
        private var platform: String? = null
        private var busiType: String? = null
        private var busiCode: String? = null
        var content: String? = null
    }
}