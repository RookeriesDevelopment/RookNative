package io.tryrook.rooknative.core.framework.launcher

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.provider.Settings
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import io.tryrook.rooknative.core.domain.launcher.Launcher
import timber.log.Timber

class DefaultLauncher(private val context: Context) : Launcher {
    override fun openUrlOnChrome(url: String) {
        val intent = CustomTabsIntent.Builder()
            .setUrlBarHidingEnabled(true)
            .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
            .setDownloadButtonEnabled(false)
            .setBookmarksButtonEnabled(false)
            .build()

        intent.intent.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }

        intent.launchUrl(context, url.toUri())
    }

    override fun openHealthConnectOnPlayStore() {
        val url = "https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata"

        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        context.startActivity(intent)
    }

    override fun openSamsungHealthOnPlayStore() {
        val url = "https://play.google.com/store/apps/details?id=com.sec.android.app.shealth"

        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        context.startActivity(intent)
    }

    // TODO: 29/09/25 remove and use from samsung health repository
    override fun openSamsungHealthSettings() {
        var intent = context.packageManager.getLaunchIntentForPackage(
            "com.sec.android.app.shealth"
        )?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        if (intent == null) {
            val url = "https://play.google.com/store/apps/details?id=com.sec.android.app.shealth"

            intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }

        context.startActivity(intent)
    }

    override fun openSettings() {
        val intent = Intent(Settings.ACTION_SETTINGS).apply {

            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }

        context.startActivity(intent)
    }

    override fun openApplicationSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = "package:${context.packageName}".toUri()

            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }

        context.startActivity(intent)
    }

    @SuppressLint("BatteryLife")
    override fun requestIgnoringBatteryOptimizations() {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = "package:${context.packageName}".toUri()

            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }

        context.startActivity(intent)
    }

    override fun isIgnoringBatteryOptimizations(): Boolean {
        return try {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

            powerManager.isIgnoringBatteryOptimizations(context.packageName)
        } catch (expected: Exception) {
            Timber.e(expected, "Failed to get power manager")

            false
        }
    }
}
