package com.manimarank.websitemonitor.ui.settings

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.manimarank.websitemonitor.R
import com.manimarank.websitemonitor.databinding.ActivitySettingsBinding
import com.manimarank.websitemonitor.utils.Constants.IS_DARK_MODE_ENABLED
import com.manimarank.websitemonitor.utils.Constants.MONITORING_INTERVAL
import com.manimarank.websitemonitor.utils.Constants.NOTIFY_ONLY_SERVER_ISSUES
import com.manimarank.websitemonitor.utils.Interval.nameList
import com.manimarank.websitemonitor.utils.Interval.valueList
import com.manimarank.websitemonitor.utils.SharedPrefsManager
import com.manimarank.websitemonitor.utils.SharedPrefsManager.set
import com.manimarank.websitemonitor.utils.Utils
import com.manimarank.websitemonitor.utils.Utils.getMonitorTime
import com.manimarank.websitemonitor.utils.Utils.isCustomRom
import com.manimarank.websitemonitor.utils.Utils.openAutoStartScreen
import com.manimarank.websitemonitor.utils.Utils.startWorkManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var activitySettingsBinding: ActivitySettingsBinding
    private lateinit var btnMonitorInterval: LinearLayout
    private lateinit var layoutEnableAutoStart: LinearLayout
    private lateinit var btnEnableAutoStart: TextView
    private lateinit var switchNotifyOnlyServerIssues: SwitchMaterial
    private lateinit var txtIntervalDetails: AppCompatTextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySettingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(activitySettingsBinding.root)
        btnMonitorInterval = activitySettingsBinding.btnMonitorInterval
        layoutEnableAutoStart = activitySettingsBinding.layoutEnableAutoStart
        btnEnableAutoStart = activitySettingsBinding.btnEnableAutoStart
        switchNotifyOnlyServerIssues = activitySettingsBinding.switchNotifyOnlyServerIssues
        txtIntervalDetails = activitySettingsBinding.txtIntervalDetails

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnMonitorInterval.setOnClickListener { showIntervalChooseDialog() }

        layoutEnableAutoStart.visibility = if (isCustomRom()) View.VISIBLE else View.GONE
        btnEnableAutoStart.setOnClickListener { openAutoStartScreen(this) }

        updateIntervalTimeOnUi()

        switchNotifyOnlyServerIssues.isChecked = SharedPrefsManager.customPrefs.getBoolean(NOTIFY_ONLY_SERVER_ISSUES, false)
        switchNotifyOnlyServerIssues.setOnCheckedChangeListener { _, isChecked ->
            SharedPrefsManager.customPrefs[NOTIFY_ONLY_SERVER_ISSUES] = isChecked
        }

        activitySettingsBinding.switchDarkMode.isChecked = SharedPrefsManager.customPrefs.getBoolean(IS_DARK_MODE_ENABLED, false)
        activitySettingsBinding.switchDarkMode.text  = getString(if (activitySettingsBinding.switchDarkMode.isChecked) R.string.disable_dark_mode else R.string.enable_dark_mode)
        activitySettingsBinding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            SharedPrefsManager.customPrefs[IS_DARK_MODE_ENABLED] = isChecked
            activitySettingsBinding.switchDarkMode.text  = getString(if (isChecked) R.string.disable_dark_mode else R.string.enable_dark_mode)
            Utils.enableDarkMode(isChecked)
        }
    }

    private fun showIntervalChooseDialog() {

        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle(getString(R.string.choose_interval))

        val checkedItem = valueList.indexOf(SharedPrefsManager.customPrefs.getInt(MONITORING_INTERVAL, 60))
        alertBuilder.setSingleChoiceItems(
            nameList,
            checkedItem
        ) { dialog: DialogInterface, which: Int ->
            SharedPrefsManager.customPrefs[MONITORING_INTERVAL] = valueList[which]
            startWorkManager(this, true)
            updateIntervalTimeOnUi()
            dialog.dismiss()
        }
        alertBuilder.setNegativeButton(getString(R.string.cancel), null)
        val dialog = alertBuilder.create()
        dialog.show()
    }

    private fun updateIntervalTimeOnUi() {
        txtIntervalDetails.text = getMonitorTime()
    }
}