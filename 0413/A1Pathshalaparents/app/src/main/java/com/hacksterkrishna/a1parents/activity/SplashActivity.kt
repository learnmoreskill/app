package com.hacksterkrishna.a1parents.activity

/**
 * Created by krishna on 31/12/17.
 */

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.crashlytics.android.Crashlytics
import com.hacksterkrishna.a1parents.Constants
import com.hacksterkrishna.a1parents.R
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
        pref=getSharedPreferences("parentPrefs", Context.MODE_PRIVATE)
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)
        var splashThread=SplashThread()
        splashThread.start()

    }

    inner class SplashThread: Thread {

        constructor():super()

        override fun run() {
            sleep(2000)
            if (!pref!!.getBoolean(Constants.IS_LOGGED_IN, false)) {
                startActivity<LoginActivity>()
            } else if (pref!!.getBoolean(Constants.IS_LOGGED_IN, false)) {
                startActivity<ProfileActivity>()
            } else {
                startActivity<LoginActivity>()
                info("ErrorPrefs")
            }
            finish()
        }

    }
}