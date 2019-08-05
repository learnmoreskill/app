package com.hacksterkrishna.a1parents.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.crashlytics.android.Crashlytics
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.roughike.bottombar.BottomBar
import com.roughike.bottombar.TabSelectionInterceptor
import com.hacksterkrishna.a1parents.R
import com.hacksterkrishna.a1parents.fragment.AttendanceFragment
import com.hacksterkrishna.a1parents.fragment.ComplaintFragment
import com.hacksterkrishna.a1parents.fragment.DashboardFragment
import com.hacksterkrishna.a1parents.fragment.HomeworkFragment
import io.fabric.sdk.android.Fabric
import org.jetbrains.anko.startActivity


class ProfileActivity : AppCompatActivity() {

    private var mBottomBar: BottomBar? = null
    private var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        pref=this.getSharedPreferences("parentPrefs", Context.MODE_PRIVATE)
        Fabric.with(this, Crashlytics())
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)

        setupBottomNavigation()

        if (savedInstanceState == null) {

            loadDashboardFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_login,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when(item.itemId){
                R.id.menu_logout->{
                    logout()
                }

                R.id.menu_about -> {

                    startActivity<AboutActivity>()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupBottomNavigation() {

        mBottomBar = findViewById(R.id.bottomBar)
        mBottomBar?.setOnTabSelectListener { tabId ->
            when (tabId) {
                R.id.navigation_dashboard -> {
                    loadDashboardFragment()
                }

                R.id.navigation_attendance -> {
                    loadAttendanceFragment()
                }

                R.id.navigation_complaint -> {
                    loadComplaintFragment()
                }

                R.id.navigation_homework -> {

                    loadHomeworkFragment()
                }

            }
        }

        mBottomBar?.setTabSelectionInterceptor(TabSelectionInterceptor { _, newTabId ->
            if (newTabId == R.id.navigation_announcement) {
                loadAnnouncementActivity()
                return@TabSelectionInterceptor true
            }

            false
        })



    }

    private fun loadDashboardFragment() {

        val fragment = DashboardFragment.newInstance()
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.fragment_frame, fragment)
        ft.commit()
    }

    private fun loadAttendanceFragment() {

        val fragment = AttendanceFragment.newInstance()
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.fragment_frame, fragment)
        ft.commit()
    }

    private fun loadComplaintFragment() {

        val fragment = ComplaintFragment.newInstance()
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.fragment_frame, fragment)
        ft.commit()
    }

    private fun loadHomeworkFragment() {

        val fragment = HomeworkFragment.newInstance()
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.fragment_frame, fragment)
        ft.commit()
    }

    private fun loadAnnouncementActivity() {

        startActivity<AnnouncementActivity>()
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }

    private fun logout() {
        startActivity<LogoutActivity>()
        finish()
    }


}
