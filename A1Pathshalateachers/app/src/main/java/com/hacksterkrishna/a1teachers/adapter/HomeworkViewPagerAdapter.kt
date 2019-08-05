package com.hacksterkrishna.a1teachers.adapter

import android.app.Fragment
import android.app.FragmentManager
import android.support.v13.app.FragmentStatePagerAdapter
import com.hacksterkrishna.a1teachers.fragment.HomeworkFragment
import com.hacksterkrishna.a1teachers.fragment.HomeworkHistoryFragment

/**
 * Created by krishna on 31/12/17.
 */
class HomeworkViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return HomeworkFragment.newInstance()
            1 -> return HomeworkHistoryFragment.newInstance()
        }
        return null
    }

    override fun getCount(): Int {
        return TAB_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return HomeworkFragment.TITLE

            1 -> return HomeworkHistoryFragment.TITLE

        }
        return super.getPageTitle(position)
    }

    companion object {

        private val TAB_COUNT = 2
    }

}