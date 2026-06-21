package com.anishkun.hidetext

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

import timber.log.Timber

@HiltAndroidApp
class HideTextApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
