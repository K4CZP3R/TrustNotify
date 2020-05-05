package xyz.k4czp3r.trustnotify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class KspBroadcastReceiver : BroadcastReceiver() {
    private val tagName = KspBroadcastReceiver::class.qualifiedName
    private val thread = KspTrustDetectionThread()

    override fun onReceive(context: Context, intent: Intent?) {
        Log.i(tagName, "action=${intent?.action}")
        when (intent?.action) {
            Intent.ACTION_SCREEN_ON -> thread.screenOnAction()
            Intent.ACTION_SCREEN_OFF -> thread.screenOffAction()
            Intent.ACTION_USER_PRESENT -> thread.userPresentAction()
        }
    }

}