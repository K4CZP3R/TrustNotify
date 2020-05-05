package xyz.k4czp3r.trustnotify.helpers

import android.content.Context
import android.content.SharedPreferences
import xyz.k4czp3r.trustnotify.TrustNotify

enum class PrefsKeys {
    START_AT_BOOT, PERMISSIONS_GRANTED, SELECTED_NOTIFICATION_MODE, COMP_CHECK_SEEN, DELAY_AFTER_DETECT
}

class SharedPrefs {
    private var sharedPrefs: SharedPreferences =
        TrustNotify.instance.getSharedPreferences("FaceNotifyPreferences", Context.MODE_PRIVATE)

    fun putInt(key: PrefsKeys, value: Int) {
        with(sharedPrefs.edit()) {
            putInt(key.name, value)
            commit()
        }
    }

    fun getInt(key: PrefsKeys): Int {
        return sharedPrefs.getInt(key.name, 0)
    }

    fun putBoolean(key: PrefsKeys, value: Boolean) {
        with(sharedPrefs.edit()) {
            putBoolean(key.name, value)
            commit()
        }
    }

    fun getBoolean(key: PrefsKeys): Boolean {
        return sharedPrefs.getBoolean(key.name, false)
    }
}