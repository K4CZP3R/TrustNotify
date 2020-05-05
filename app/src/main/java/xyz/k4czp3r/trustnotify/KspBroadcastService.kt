package xyz.k4czp3r.trustnotify

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class KspBroadcastService : Service() {
    private val tagName = KspBroadcastService::class.qualifiedName

    companion object {
        private val LAUNCHER = FgServiceLauncher(KspBroadcastService::class.java)

        @JvmStatic
        fun start(context: Context) = LAUNCHER.startService(context)

        @JvmStatic
        fun stop(context: Context) = LAUNCHER.stopService(context)

        @JvmStatic
        fun state() = LAUNCHER.state()

    }

    private val br: BroadcastReceiver = KspBroadcastReceiver()

    override fun onDestroy() {
        Log.v(tagName, "onDestroy, unregister receiver!")
        try {
            unregisterReceiver(br)
        } catch (e: Exception) {
            Log.w(tagName, "Tried to unregister receiver, failed!")
            e.printStackTrace()
        }
    }

    override fun onCreate() {
        super.onCreate()


        val channelId = createNotificationChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )
        val builder = NotificationCompat.Builder(this, channelId)
            .setOngoing(true)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_content))
            .setCategory(Notification.CATEGORY_SERVICE)
            .setSmallIcon(R.drawable.ic_face)
            .build()

        startForeground(1338, builder)


        LAUNCHER.onServiceCreated(this)

    }

    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw NotImplementedError("Why would I want to implement it?")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(tagName, "Registering broadcast receiver!")
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        registerReceiver(br, filter)
        return super.onStartCommand(intent, flags, startId)
    }

}