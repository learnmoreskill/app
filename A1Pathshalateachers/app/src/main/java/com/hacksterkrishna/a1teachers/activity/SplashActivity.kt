package com.hacksterkrishna.a1teachers.activity

/**
 * Created by krishna on 31/12/17.
 */

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.crashlytics.android.Crashlytics
import com.hacksterkrishna.a1teachers.Constants
import com.hacksterkrishna.a1teachers.R
import io.fabric.sdk.android.Fabric
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity


class SplashActivity : FragmentActivity(), AnkoLogger {

    private var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_splash)
        pref=getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        var splashThread=SplashThread()
        splashThread.start()

    }

    inner class SplashThread: Thread {

        constructor():super()

        override fun run() {
            sleep(2000)
            if (!pref!!.getBoolean(Constants.IS_LOGGED_IN, false)) {
                startActivity<LoggedoutActivity>()
            } else if (pref!!.getBoolean(Constants.IS_LOGGED_IN, false)) {
                startActivity<LoginActivity>()
            } else {
                startActivity<LoggedoutActivity>()
                info("ErrorPrefs")
            }
            finish()
        }

    }
}