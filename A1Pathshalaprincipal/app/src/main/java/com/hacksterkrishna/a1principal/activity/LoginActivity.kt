package com.hacksterkrishna.a1principal.activity

import android.app.Fragment
import android.content.Context
import android.content.Intent
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
import com.hacksterkrishna.a1principal.Constants
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.fragment.*
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.app_bar_login.*
import kotlinx.android.synthetic.main.nav_header_login.view.*

class LoginActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var pref: SharedPreferences? = null
    private val mDrawerHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        pref=this.getSharedPreferences("princiPrefs", Context.MODE_PRIVATE)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar_login)
        val header: View = nav_login_view.getHeaderView(0)
        header.tv_princi_name.text = pref!!.getString(Constants.NAME,"Name")
        header.tv_princi_email.text = pref!!.getString(Constants.EMAIL,"email@domain.com")
        header.iv_pro.setImageResource(R.drawable.ic_pro_pic)

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
        if(!(item.itemId==R.id.nav_about || item.itemId==R.id.nav_send_brdcst_msg))
        item.isChecked=true
        return true
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }

    private fun displaySelectedScreen(itemId:Int){

        var fragment: Fragment?=null
        when(itemId) {
            R.id.nav_dashboard -> {
                fragment = DashboardFragment()
            }

            R.id.nav_stdnt_details -> {
                fragment = DetailsFragment()
            }

            R.id.nav_attendance -> {

                fragment = AttendanceFragment()

            }

            R.id.nav_hw -> {
                fragment = HomeworksFragment()

            }

            R.id.nav_prnts_msg -> {
                fragment = MessagesFragment()

            }

            R.id.nav_brdcst_msg -> {
                fragment = BroadcastsFragment()

            }

            R.id.nav_send_brdcst_msg -> {
                mDrawerHandler.postDelayed({
                var intentBroadcast= Intent(this@LoginActivity, BroadCastActivity::class.java)
                startActivity(intentBroadcast)
                },250)
            }

            R.id.nav_logout -> {
                logout()
            }

            R.id.nav_about -> {
                mDrawerHandler.postDelayed({
                var intentAbout= Intent(this@LoginActivity, AboutActivity::class.java)
                startActivity(intentAbout)
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

    private fun logout() {
        val editor = pref!!.edit()
        editor.putBoolean(Constants.IS_LOGGED_IN, false)
        editor.putString(Constants.ID, "")
        editor.putString(Constants.NAME, "")
        editor.putString(Constants.EMAIL, "")
        editor.putString(Constants.SCHOOL, "")

        //clear the SchoolUrl from shared preferences
        editor.putString(Constants.SCHOOL_URL,"")

        editor.apply()
        goToLogin()
    }

    private fun goToLogin() {

        var intent_out= Intent(this@LoginActivity, LoggedoutActivity::class.java)
        startActivity(intent_out)
        finish()
    }
}
