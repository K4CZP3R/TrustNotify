package xyz.k4czp3r.trustnotify

import android.app.KeyguardManager
import android.content.Context
import xyz.k4czp3r.trustnotify.helpers.PrefsKeys
import xyz.k4czp3r.trustnotify.helpers.SecureSetting
import xyz.k4czp3r.trustnotify.helpers.SecureSettingsHelper
import xyz.k4czp3r.trustnotify.helpers.SharedPrefs
import xyz.k4czp3r.trustnotify.models.InstructionTypes
import xyz.k4czp3r.trustnotify.models.NotificationType
import xyz.k4czp3r.trustnotify.models.NotificationTypes

class KspTrustDetection {
    private val secureSettingsHelper = SecureSettingsHelper()
    private val sharedPrefs = SharedPrefs()

    fun isDeviceTrusted(): Boolean {
        val keyguardManager =
            TrustNotify.instance.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        return !keyguardManager.isDeviceLocked
    }
    fun restoreDefaultNotificationSettings() {
        secureSettingsHelper.putStr(SecureSetting.lock_screen_show_notifications, "1")
        secureSettingsHelper.putStr(SecureSetting.lock_screen_allow_private_notifications, "1")
    }

    fun prepareSecureSettingsFor(notificationType: NotificationType) {
        when (notificationType.instruction) {
            InstructionTypes.whole_notification -> {
                secureSettingsHelper.putStr(
                    SecureSetting.lock_screen_allow_private_notifications,
                    "1"
                )
                secureSettingsHelper.putStr(SecureSetting.lock_screen_show_notifications, "0")
            }
            InstructionTypes.only_content -> {
                secureSettingsHelper.putStr(SecureSetting.lock_screen_show_notifications, "1")
                secureSettingsHelper.putStr(
                    SecureSetting.lock_screen_allow_private_notifications,
                    "0"
                )
            }
        }
    }

    fun showNotifications() {
        when (NotificationTypes[sharedPrefs.getInt(PrefsKeys.SELECTED_NOTIFICATION_MODE)].instruction) {
            InstructionTypes.whole_notification -> {
                secureSettingsHelper.putStr(SecureSetting.lock_screen_show_notifications, "1")
            }
            InstructionTypes.only_content -> {
                secureSettingsHelper.putStr(
                    SecureSetting.lock_screen_allow_private_notifications,
                    "1"
                )
            }
        }
    }

    fun hideNotifications() {
        when (NotificationTypes[sharedPrefs.getInt(PrefsKeys.SELECTED_NOTIFICATION_MODE)].instruction) {
            InstructionTypes.whole_notification -> {
                secureSettingsHelper.putStr(SecureSetting.lock_screen_show_notifications, "0")
            }
            InstructionTypes.only_content -> {
                secureSettingsHelper.putStr(
                    SecureSetting.lock_screen_allow_private_notifications,
                    "0"
                )
            }
        }
    }

}
