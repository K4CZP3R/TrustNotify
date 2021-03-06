package xyz.k4czp3r.trustnotify.user_interface

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.topjohnwu.superuser.Shell
import xyz.k4czp3r.trustnotify.KspBroadcastService
import xyz.k4czp3r.trustnotify.KspTrustDetection
import xyz.k4czp3r.trustnotify.R
import xyz.k4czp3r.trustnotify.helpers.PermissionHelper
import xyz.k4czp3r.trustnotify.helpers.PrefsKeys
import xyz.k4czp3r.trustnotify.helpers.SharedPrefs
import xyz.k4czp3r.trustnotify.models.DelayAfterDetectTypes
import xyz.k4czp3r.trustnotify.models.NotificationTypes


class FragmentSettings : Fragment() {

    private lateinit var spinnerNotificationMode: Spinner
    private lateinit var switchStartOnBoot: Switch
    private lateinit var permissionsStatus: TextView
    private lateinit var buttonHowToGrant: Button
    private lateinit var buttonRestoreDefault: Button
    private lateinit var spinnerDelayAfterDetect: Spinner
    private lateinit var buttonMagiskGrant: Button
    private val sharedPrefs: SharedPrefs = SharedPrefs()
    private val permissionHelper = PermissionHelper()
    private val neededPermissions = listOf(Manifest.permission.WRITE_SECURE_SETTINGS)
    private val TAG = FragmentSettings::class.qualifiedName

    companion object {
        fun newInstance(): FragmentSettings {
            return FragmentSettings()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerNotificationMode = view.findViewById(R.id.f_settings_spinnerNotificationMode)
        switchStartOnBoot = view.findViewById(R.id.f_settings_switchStartOnBoot)
        permissionsStatus = view.findViewById(R.id.f_settings_textView_permissionsStatus_value)
        buttonHowToGrant = view.findViewById(R.id.f_settings_button_howtoPerm)
        buttonRestoreDefault = view.findViewById(R.id.f_settings_button_restoreDefault)
        spinnerDelayAfterDetect = view.findViewById(R.id.f_settings_spinnerDelayAfterDetect)
        buttonMagiskGrant = view.findViewById(R.id.f_settings_button_magiskPerm)


        updateData(view.context)


        spinnerNotificationMode.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (sharedPrefs.getInt(PrefsKeys.SELECTED_NOTIFICATION_MODE) != position && KspBroadcastService.state()) showAlert(
                        view!!.context,
                        activity!!.getString(R.string.service_restart_required_title),
                        activity!!.getString(R.string.service_restart_required_content)
                    )
                    sharedPrefs.putInt(PrefsKeys.SELECTED_NOTIFICATION_MODE, position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        spinnerDelayAfterDetect.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sharedPrefs.putInt(PrefsKeys.DELAY_AFTER_DETECT, position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        switchStartOnBoot.setOnCheckedChangeListener { button, b ->
            switchStartOnBootChange(
                button.context,
                b
            )
        }
        buttonHowToGrant.setOnClickListener { v -> buttonHowToGrantClick(v) }
        buttonRestoreDefault.setOnClickListener { v -> buttonRestoreDefaultClick(v) }
        buttonMagiskGrant.setOnClickListener { v -> buttonMagiskGrantClick(v) }
    }
    private fun buttonMagiskGrantClick(view: View){
        MaterialAlertDialogBuilder(view.context)
            .setTitle(activity!!.getString(R.string.magisk_alert_title))
            .setMessage(activity!!.getString(R.string.magisk_alert_content))
            .setPositiveButton(activity!!.getString(R.string.magisk_alert_yes), DialogInterface.OnClickListener{_,_ ->
                val result = Shell.su("pm grant xyz.k4czp3r.trustnotify android.permission.WRITE_SECURE_SETTINGS").exec().isSuccess
                Log.i(TAG, "Shell command returned: $result")
                if(result) updateData(view.context)
            })
            .setNeutralButton(activity!!.getString(R.string.magisk_alert_no), DialogInterface.OnClickListener{_,_->
                Log.i(TAG, "Skipping magisk")
            })
            .show()
    }
    private fun buttonRestoreDefaultClick(view: View) {
        if (sharedPrefs.getBoolean(PrefsKeys.PERMISSIONS_GRANTED)) {
            KspTrustDetection().restoreDefaultNotificationSettings()
            Toast.makeText(view.context, "Restored!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(view.context, "Grant permissions to do this!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buttonHowToGrantClick(view: View) {
        MaterialAlertDialogBuilder(view.context)
            .setTitle(activity!!.getString(R.string.how_to_grant_title))
            .setMessage(activity!!.getString(R.string.how_to_grant_content))
            .setNeutralButton(activity!!.getString(R.string.how_to_grant_open), DialogInterface.OnClickListener { _, _ ->
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(activity!!.getString(R.string.tutorial_link)))
                startActivity(browserIntent)
            })
            .show()
    }

    private fun switchStartOnBootChange(context: Context, newState: Boolean) {
        sharedPrefs.putBoolean(PrefsKeys.START_AT_BOOT, newState)
        updateData(context)

    }

    private fun updateDataDelayAfterDetect(context: Context) {
        val dadAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item,
            convertDelayAfterUnlockTypesToList()
        )
        spinnerDelayAfterDetect.adapter = dadAdapter
        var dadId = sharedPrefs.getInt(PrefsKeys.DELAY_AFTER_DETECT)
        if (dadId > DelayAfterDetectTypes.size) dadId = 0
        spinnerDelayAfterDetect.setSelection(dadId)
    }

    private fun updateDataNotificationMode(context: Context) {
        //NOTIFICATION MODE SELECT
        val notModAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_spinner_dropdown_item,
            convertNotificationTypesToList()
        )
        spinnerNotificationMode.adapter = notModAdapter
        var notModeId = sharedPrefs.getInt(PrefsKeys.SELECTED_NOTIFICATION_MODE)
        if (notModeId > NotificationTypes.size) notModeId = 0
        spinnerNotificationMode.setSelection(notModeId)
    }

    private fun updateDataStartOnBoot() {
        //START AT BOOT
        val startAtBoot = sharedPrefs.getBoolean(PrefsKeys.START_AT_BOOT)
        switchStartOnBoot.isChecked = startAtBoot
        if (startAtBoot) switchStartOnBoot.text =
            activity!!.getString(R.string.start_on_boot_switch_on)
        else switchStartOnBoot.text = activity!!.getString(R.string.start_on_boot_switch_off)
    }

    private fun updateDataMissingPerms() {
        //MISSING PERMISSIONS
        val missingPermissions = arrayListOf<String>()
        for (i in neededPermissions) {
            if (!permissionHelper.isPermissionGranted(i)) {
                missingPermissions.add(i)
            }
        }

        if (missingPermissions.size > 0) { //There are still missing permissions
            var permissionText = "Missing:\n"
            for (p in missingPermissions) {
                permissionText += "${p}\n"
            }
            permissionsStatus.text = permissionText
            buttonHowToGrant.visibility = View.VISIBLE
            buttonMagiskGrant.visibility = View.VISIBLE
            sharedPrefs.putBoolean(PrefsKeys.PERMISSIONS_GRANTED, false)
        } else {
            permissionsStatus.text = activity!!.getString(R.string.permissions_granted)
            sharedPrefs.putBoolean(PrefsKeys.PERMISSIONS_GRANTED, true)
            buttonHowToGrant.visibility = View.GONE
            buttonMagiskGrant.visibility = View.GONE

        }
    }


    private fun showAlert(context: Context, title: String, content: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(content)
            .setNeutralButton(activity!!.getString(R.string.OK)) { _: DialogInterface, _: Int -> }
            .show()
    }

    private fun updateData(context: Context) {

        updateDataNotificationMode(context)
        updateDataStartOnBoot()
        updateDataMissingPerms()
        updateDataDelayAfterDetect(context)
    }

    private fun convertDelayAfterUnlockTypesToList(): List<String> {
        val toReturnList = arrayListOf<String>()
        for (daut in DelayAfterDetectTypes) {
            toReturnList.add(daut.readable)
        }
        return toReturnList
    }

    private fun convertNotificationTypesToList(): List<String> {
        val toReturnList = arrayListOf<String>()
        for (nt in NotificationTypes) {
            toReturnList.add(nt.name)
        }
        return toReturnList
    }

}