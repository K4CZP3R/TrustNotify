package xyz.k4czp3r.trustnotify.helpers

import android.content.pm.PackageManager
import xyz.k4czp3r.trustnotify.TrustNotify

class PermissionHelper {
    fun isPermissionGranted(permName: String): Boolean {
        val appContext = TrustNotify.instance.applicationContext
        val packageName = appContext.packageName
        return appContext.packageManager.checkPermission(
            permName,
            packageName
        ) == PackageManager.PERMISSION_GRANTED
    }

    /*fun askForPermission(activity: Activity, permNames: Array<String>, requestCode: Int){
        ActivityCompat.requestPermissions(activity,permNames,requestCode)
    }*/
}