package com.anishkun.hidetext

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

import timber.log.Timber
import javax.inject.Inject
import androidx.lifecycle.ProcessLifecycleOwner

@HiltAndroidApp
class HideTextApp : Application() {
    
    @Inject
    lateinit var appLifecycleObserver: AppLifecycleObserver

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
    }
}
