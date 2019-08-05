package com.hacksterkrishna.a1teachers.activity

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
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.adapter.BroadcastViewPagerAdapter
import io.fabric.sdk.android.Fabric


class BroadCastActivity:AppCompatActivity() {

    private var mViewPager: ViewPager? = null
    private var mBroadcastViewPagerAdapter: BroadcastViewPagerAdapter? = null
    private var mTabLayout: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_broadcast)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.elevation= 0F
        setViewPager()

    }

    private fun setViewPager() {

        mViewPager = findViewById(R.id.pager)
        mBroadcastViewPagerAdapter = BroadcastViewPagerAdapter(fragmentManager)
        mViewPager!!.adapter = mBroadcastViewPagerAdapter

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