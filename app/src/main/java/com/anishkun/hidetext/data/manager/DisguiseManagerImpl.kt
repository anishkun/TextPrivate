package com.anishkun.hidetext.data.manager

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.anishkun.hidetext.domain.manager.DisguiseManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DisguiseManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DisguiseManager {

    private val packageManager = context.packageManager
    private val packageName = context.packageName

    // Activity alias component names
    private val calculatorAlias = ComponentName(packageName, "com.anishkun.hidetext.CalculatorAlias")
    private val notesAlias = ComponentName(packageName, "com.anishkun.hidetext.NotesAlias")

    override fun toggleDisguise() {
        val isCalculatorEnabled = packageManager.getComponentEnabledSetting(calculatorAlias) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED

        if (isCalculatorEnabled) {
            // Switch to Notes
            enableComponent(notesAlias)
            disableComponent(calculatorAlias)
        } else {
            // Switch to Calculator
            enableComponent(calculatorAlias)
            disableComponent(notesAlias)
        }
    }

    private fun enableComponent(componentName: ComponentName) {
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun disableComponent(componentName: ComponentName) {
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}
