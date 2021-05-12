package com.sec.myapplication

import android.os.Bundle
import android.provider.Telephony.Mms.Part.CONTENT_ID
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.sec.hnky.spl.R


class MainActivity : AppCompatActivity() {
    var appName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        FirebaseInstallations.getInstance().getToken(true).
        addOnCompleteListener {
            val token = it.result!!.token
            Log.d(this.javaClass.simpleName, "Refreshed token on getInstanceId:$token")
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "AppName :$appName", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.CONTENT, "Button")
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Button Clicked")
            CustomApplication.FirebaseAnalyticsObject.sendFireBaseEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)

        }
    }

    override fun onStart() {
        super.onStart()
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val firebaseRemoteConfigSettings: FirebaseRemoteConfigSettings =
            FirebaseRemoteConfigSettings.Builder().build()
        firebaseRemoteConfig.setConfigSettingsAsync(firebaseRemoteConfigSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        val cacheExpiration: Long = 0
        appName = firebaseRemoteConfig.getString("app_name")
        firebaseRemoteConfig.fetch(cacheExpiration)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseRemoteConfig.fetchAndActivate()
                    appName = firebaseRemoteConfig.getString("app_name")
                    Log.i("Firebase Config", appName)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}