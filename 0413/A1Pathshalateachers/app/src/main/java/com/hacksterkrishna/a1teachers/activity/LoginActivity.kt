package com.hacksterkrishna.a1teachers.activity

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.crashlytics.android.Crashlytics
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.hacksterkrishna.a1teachers.Constants
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.fragment.AttendanceFragment
import com.hacksterkrishna.a1teachers.fragment.DashboardFragment
import com.hacksterkrishna.a1teachers.fragment.FeeNotPaidFragment
import com.hacksterkrishna.a1teachers.fragment.GroupComplaintFragment
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.app_bar_login.*
import kotlinx.android.synthetic.main.nav_header_login.view.*
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var pref: SharedPreferences? = null
    private val mDrawerHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        pref=this.getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar_login)
        val header: View = nav_login_view.getHeaderView(0)
        header.tv_teacher_name.text = pref!!.getString(Constants.NAME,"Name")
        header.tv_teacher_email.text = pref!!.getString(Constants.EMAIL,"email@domain.com")
        header.iv_pro.setImageResource(R.drawable.ic_propic)

        val toggle = ActionBarDrawerToggle(
                this, drawer_login_layout, toolbar_login, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_login_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_login_view.setNavigationItemSelectedListener(this)
        displaySelectedScreen(R.id.nav_dashboard)
        nav_login_view.setCheckedItem(R.id.nav_dashboard)

    }

    override fun onBackPressed() {
        if (drawer_login_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_login_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /*   override fun onCreateOptionsMenu(menu: Menu): Boolean {
           // Inflate the menu; this adds items to the action bar if it is present.
           menuInflater.inflate(R.menu.login, menu)
           return true
       }

       override fun onOptionsItemSelected(item: MenuItem): Boolean {
           // Handle action bar item clicks here. The action bar will
           // automatically handle clicks on the Home/Up button, so long
           // as you specify a parent activity in AndroidManifest.xml.
           when (item.itemId) {
               R.id.action_settings -> return true
               else -> return super.onOptionsItemSelected(item)
           }
       }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        drawer_login_layout.closeDrawer(GravityCompat.START)
            displaySelectedScreen(item.itemId)
        if(!(item.itemId==R.id.nav_about || item.itemId==R.id.nav_broadcast || item.itemId==R.id.nav_complaint || item.itemId==R.id.nav_homework))
        item.isChecked=true
        return true
    }

    private fun displaySelectedScreen(itemId:Int){

        var fragment: Fragment?=null
        when(itemId) {
            R.id.nav_dashboard -> {
                fragment = DashboardFragment()
            }

            R.id.nav_attendance -> {

                fragment = AttendanceFragment()
            }

            R.id.nav_broadcast -> {
                mDrawerHandler.postDelayed({
                startActivity<BroadCastActivity>()
                },250)
            }

            R.id.nav_complaint -> {
                mDrawerHandler.postDelayed({
                startActivity<ComplaintActivity>()
                },250)
            }

            R.id.nav_homework -> {
                mDrawerHandler.postDelayed({
                    startActivity<HomeworkActivity>()
                },250)
            }

            R.id.nav_feenp -> {
                fragment = FeeNotPaidFragment()
            }

            R.id.nav_group -> {
                fragment = GroupComplaintFragment()
            }


            R.id.nav_logout -> {
                logout()
            }

            R.id.nav_about -> {
                mDrawerHandler.postDelayed({
                    startActivity<AboutActivity>()
                },250)

            }

        }
        if(fragment != null){
            mDrawerHandler.postDelayed({
            val ft = fragmentManager.beginTransaction()
            ft.replace(R.id.login_fragment_frame, fragment)
            ft.commit()
            },250)
        }

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }

    private fun logout() {
        val editor = pref!!.edit()
        editor.putBoolean(Constants.IS_LOGGED_IN, false)
        editor.putString(Constants.ID, "")
        editor.putString(Constants.NAME, "")
        editor.putString(Constants.EMAIL, "")
        editor.putString(Constants.ADDRESS, "")
        editor.putString(Constants.SCHOOL, "")
        editor.putString(Constants.SCHOOL_CODE, "")
        editor.putString(Constants.STANDARD, "")
        editor.putString(Constants.SEC, "")

        //clear the SchoolUrl from shared preferences
        editor.putString(Constants.SCHOOL_URL,"")

        editor.apply()
        goToLogin()
    }

    private fun goToLogin() {
        startActivity<LoggedoutActivity>()
        finish()
    }
}
