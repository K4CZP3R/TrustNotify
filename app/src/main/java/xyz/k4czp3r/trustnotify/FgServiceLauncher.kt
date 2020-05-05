package xyz.k4czp3r.trustnotify

import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class FgServiceLauncher(private val serviceClass: Class<out Service>) {
    private var isStarting = false
    private var shouldStop = false
    private var isRunning = false

    @Synchronized
    fun startService(context: Context, block: Intent.() -> Unit = {}) {
        isStarting = true
        shouldStop = false
        ContextCompat.startForegroundService(
            context,
            Intent(context, serviceClass).apply { block() })
    }

    @Synchronized
    fun stopService(context: Context) {
        if (isStarting) {
            shouldStop = true
        } else {
            isRunning = false
            context.stopService(Intent(context, serviceClass))
        }
    }

    @Synchronized
    fun state(): Boolean {
        return isRunning
    }

    @Synchronized
    fun onServiceCreated(service: Service) {
        isStarting = false
        isRunning = true
        if (shouldStop) {
            shouldStop = false
            service.stopSelf()
        }
    }
}