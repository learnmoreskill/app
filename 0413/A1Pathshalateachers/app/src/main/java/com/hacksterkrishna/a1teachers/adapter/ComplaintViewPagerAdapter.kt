package com.hacksterkrishna.a1teachers.adapter

import android.app.Fragment
import android.app.FragmentManager
import android.support.v13.app.FragmentStatePagerAdapter
import com.hacksterkrishna.a1teachers.fragment.ComplaintHistoryFragment
import com.hacksterkrishna.a1teachers.fragment.ComplaintSendFragment

/**
 * Created by krishna on 31/12/17.
 */
class ComplaintViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return ComplaintSendFragment.newInstance()
            1 -> return ComplaintHistoryFragment.newInstance()
        }
        return null
    }

    override fun getCount(): Int {
        return TAB_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return ComplaintSendFragment.TITLE

            1 -> return ComplaintHistoryFragment.TITLE

        }
        return super.getPageTitle(position)
    }

    companion object {

        private val TAB_COUNT = 2
    }

}