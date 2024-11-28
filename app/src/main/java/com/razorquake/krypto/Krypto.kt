package com.razorquake.krypto

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Krypto: Application() {
    override fun onCreate() {
        super.onCreate()
        if (Timber.treeCount == 0) {
            Timber.plant(Timber.DebugTree())
        }
    }
}