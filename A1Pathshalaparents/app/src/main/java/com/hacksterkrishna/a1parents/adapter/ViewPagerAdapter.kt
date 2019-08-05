package com.hacksterkrishna.a1parents.adapter

/**
 * Created by krishna on 31/12/17.
 */

import android.app.Fragment
import android.app.FragmentManager
import android.support.v13.app.FragmentStatePagerAdapter
import com.hacksterkrishna.a1parents.fragment.ClassAnnouncementFragment
import com.hacksterkrishna.a1parents.fragment.SchoolAnnouncementFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return ClassAnnouncementFragment.newInstance()
            1 -> return SchoolAnnouncementFragment.newInstance()
        }
        return null
    }

    override fun getCount(): Int {
        return TAB_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return ClassAnnouncementFragment.TITLE

            1 -> return SchoolAnnouncementFragment.TITLE

        }
        return super.getPageTitle(position)
    }

    companion object {

        private val TAB_COUNT = 2
    }

}