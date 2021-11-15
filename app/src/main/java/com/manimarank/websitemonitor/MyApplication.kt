package com.manimarank.websitemonitor

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.manimarank.websitemonitor.utils.Constants
import com.manimarank.websitemonitor.utils.SharedPrefsManager
import com.manimarank.websitemonitor.utils.Utils

class MyApplication : Application(), LifecycleObserver {

    object ActivityVisibility {
        var appIsVisible: Boolean = false
        @JvmStatic
        fun resumeApp() { appIsVisible = true }
        @JvmStatic
        fun pauseApp() { appIsVisible = false }
    }

    override fun onCreate() {
        super.onCreate()
        SharedPrefsManager.init(this)
        Utils.enableDarkMode(SharedPrefsManager.customPrefs.getBoolean(Constants.IS_DARK_MODE_ENABLED, false))
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        ActivityVisibility.pauseApp()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        ActivityVisibility.resumeApp()
    }
}