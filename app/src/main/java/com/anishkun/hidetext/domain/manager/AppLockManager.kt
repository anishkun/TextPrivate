package com.anishkun.hidetext.domain.manager

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLockManager @Inject constructor() {
    private val _lockEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val lockEvents = _lockEvents.asSharedFlow()

    fun lockApp() {
        _lockEvents.tryEmit(Unit)
    }
}
