package com.hacksterkrishna.a1teachers.activity

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.crashlytics.android.Crashlytics
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.adapter.HomeworkViewPagerAdapter
import io.fabric.sdk.android.Fabric

/**
 * Created by krishna on 31/12/17.
 */
class HomeworkActivity: AppCompatActivity() {

    private var mViewPager: ViewPager? = null
    private var mHomeworkViewPagerAdapter: HomeworkViewPagerAdapter? = null
    private var mTabLayout: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_homework)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.elevation= 0F
        setViewPager()

    }

    private fun setViewPager() {

        mViewPager = findViewById(R.id.hw_pager)
        mHomeworkViewPagerAdapter = HomeworkViewPagerAdapter(fragmentManager)
        mViewPager!!.adapter = mHomeworkViewPagerAdapter

        mTabLayout = findViewById(R.id.hw_tab)
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