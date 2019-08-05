package com.hacksterkrishna.a1principal.activity

/**
 * Created by krishna on 31/12/17.
 */

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.hacksterkrishna.a1principal.Constants
import com.hacksterkrishna.a1principal.R
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class SplashActivity : FragmentActivity() {

    private var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_splash)

        pref=getSharedPreferences("princiPrefs", Context.MODE_PRIVATE)

        var splashThread=SplashThread()
        splashThread.start()

    }

    inner class SplashThread: Thread {

        constructor():super()

        override fun run() {
            sleep(2000)

            var intent_out=Intent(this@SplashActivity, LoggedoutActivity::class.java)
            var intent_in=Intent(this@SplashActivity, LoginActivity::class.java)

            if (!pref!!.getBoolean(Constants.IS_LOGGED_IN, false)) {
                startActivity(intent_out)
            } else if (pref!!.getBoolean(Constants.IS_LOGGED_IN, false)) {
                startActivity(intent_in)
            } else {
                startActivity(intent_out)
                Log.i("SplashActivity","ErrorPrefs")
            }
            finish()
        }

    }
}