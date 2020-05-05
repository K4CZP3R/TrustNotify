package xyz.k4czp3r.trustnotify.models

import xyz.k4czp3r.trustnotify.TrustNotify
import xyz.k4czp3r.trustnotify.R

enum class InstructionTypes {
    whole_notification, only_content
}

data class NotificationType(val id: Int, val name: String, val instruction: InstructionTypes)

val HIDE_WHOLE_NOTIFICATION = NotificationType(
    0,
    TrustNotify.instance.applicationContext.getString(R.string.notification_mode_whole),
    InstructionTypes.whole_notification
)
val HIDE_ONLY_CONTENT = NotificationType(
    1,
    TrustNotify.instance.applicationContext.getString(R.string.notification_mode_content),
    InstructionTypes.only_content
)

val NotificationTypes = arrayOf(HIDE_WHOLE_NOTIFICATION, HIDE_ONLY_CONTENT)
