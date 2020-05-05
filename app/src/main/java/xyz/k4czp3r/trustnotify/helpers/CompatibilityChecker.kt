package xyz.k4czp3r.trustnotify.helpers

import android.content.Context
import com.jaredrummler.android.device.DeviceName
import xyz.k4czp3r.trustnotify.models.BlacklistedDevice

class CompatibilityChecker {


    private val blacklistedDevices = listOf(
        BlacklistedDevice(
            "OnePlus 7T Pro",
            "In OOS there is no option to show camera but not unlock device."
        )
    )

    private fun getDeviceName(context: Context): String {
        DeviceName.init(context)
        return DeviceName.getDeviceName()
    }

    fun isDeviceBlacklisted(context: Context): Boolean {
        val deviceName = getDeviceName(context)
        for (device in blacklistedDevices) {
            if (device.Name == deviceName) return true
        }
        return false
    }

    fun getReason(context: Context): String {
        if (!isDeviceBlacklisted(context)) return ""
        for (device in blacklistedDevices) {
            if (device.Name == getDeviceName(context)) {
                return device.Reason
            }
        }
        return "Unknown"
    }
}