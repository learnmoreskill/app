package com.hacksterkrishna.a1teachers.activity

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.crashlytics.android.Crashlytics
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.fragment.LoginFragment
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_loggedout.*
import kotlinx.android.synthetic.main.app_bar_loggedout.*

class LoggedoutActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_loggedout)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_logout_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_logout_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_logout_view.setNavigationItemSelectedListener(this)
        displaySelectedScreen(R.id.nav_login)
        nav_logout_view.setCheckedItem(R.id.nav_login)
    }

    override fun onBackPressed() {
        if (drawer_logout_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_logout_layout.closeDrawer(GravityCompat.START)
        }  else {
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


        displaySelectedScreen(item.itemId)
        item.isChecked=true
        return true
    }

    fun displaySelectedScreen(itemId:Int){

        var fragment: Fragment?=null
        when(itemId) {
            R.id.nav_login -> {
                fragment = LoginFragment()
            }

            R.id.nav_about -> {
                //TODO
            }

        }
        if(fragment != null){
            val ft = fragmentManager.beginTransaction()
            ft.replace(R.id.loggedout_fragment_frame, fragment)
            ft.commit()
        }




        drawer_logout_layout.closeDrawer(GravityCompat.START)

    }
}
