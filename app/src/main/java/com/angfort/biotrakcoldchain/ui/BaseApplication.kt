package com.angfort.biotrakcoldchain.ui

import android.app.Application
import com.angfort.biotrakcoldchain.data.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BaseApplication)
            modules(appModule)
        }
    }
}
