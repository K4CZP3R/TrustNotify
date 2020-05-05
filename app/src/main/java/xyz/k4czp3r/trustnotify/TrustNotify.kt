package xyz.k4czp3r.trustnotify

import android.app.Application

class TrustNotify : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: TrustNotify
            private set
    }

}