package com.hottouk.gameinschool

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk

class App : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "1bda287d523c81a881c20f58aaf2ddaf")
    }

    companion object {
        private var instance: App? = null

        fun context(): Context {
            return instance!!.applicationContext
        }
    }
}