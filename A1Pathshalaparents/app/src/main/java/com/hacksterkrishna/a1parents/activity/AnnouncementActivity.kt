package com.hacksterkrishna.a1parents.activity

/**
 * Created by krishna on 31/12/17.
 */
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.crashlytics.android.Crashlytics
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.hacksterkrishna.a1parents.R
import com.hacksterkrishna.a1parents.adapter.ViewPagerAdapter
import io.fabric.sdk.android.Fabric


class AnnouncementActivity:AppCompatActivity() {

    private var mViewPager: ViewPager? = null
    private var mViewPagerAdapter: ViewPagerAdapter? = null
    private var mTabLayout: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announcement)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.elevation= 0F
        setViewPager()
        Fabric.with(this, Crashlytics())
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)

    }

    private fun setViewPager() {

        mViewPager = findViewById(R.id.pager)
        mViewPagerAdapter = ViewPagerAdapter(fragmentManager)
        mViewPager!!.adapter = mViewPagerAdapter

        mTabLayout = findViewById(R.id.tab)
        mTabLayout!!.setupWithViewPager(mViewPager)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                //NavUtils.navigateUpFromSameTask(this)
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }

}