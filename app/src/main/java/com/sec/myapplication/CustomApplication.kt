package com.sec.myapplication

import android.app.Application
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics

class CustomApplication : Application() {

    object FirebaseAnalyticsObject{
        var firebaseAnalytics: FirebaseAnalytics? = null

        fun sendFireBaseEvent(key: String, bundle: Bundle) {
            firebaseAnalytics.let {
                firebaseAnalytics?.logEvent(key,bundle)
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseAnalyticsObject.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

}