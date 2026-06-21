package com.anishkun.hidetext

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.anishkun.hidetext.domain.manager.AppLockManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLifecycleObserver @Inject constructor(
    private val appLockManager: AppLockManager
) : DefaultLifecycleObserver {

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        // App went to the background, trigger the panic switch
        appLockManager.lockApp()
    }
}
