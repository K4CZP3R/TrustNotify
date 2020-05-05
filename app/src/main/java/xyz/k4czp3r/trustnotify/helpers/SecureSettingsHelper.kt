package xyz.k4czp3r.trustnotify.helpers

import android.provider.Settings
import xyz.k4czp3r.trustnotify.TrustNotify

enum class SecureSetting {
    lock_screen_show_notifications, lock_screen_allow_private_notifications
}

class SecureSettingsHelper {
    fun putStr(key: SecureSetting, value: String): Boolean {
        Settings.Secure.putString(TrustNotify.instance.contentResolver, key.name, value)
        return true
    }
    /*fun getStr(key: SecureSetting): String{
        return Settings.Secure.getString(TrustNotify.instance.contentResolver, key.name)
    }*/


}