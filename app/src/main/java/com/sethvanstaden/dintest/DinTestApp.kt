package com.sethvanstaden.dintest

import android.app.Application
import com.sethvanstaden.dintest.di.appModule
import com.sethvanstaden.dintest.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class DinTestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DinTestApp)
            modules(appModule, networkModule)
        }
    }
}