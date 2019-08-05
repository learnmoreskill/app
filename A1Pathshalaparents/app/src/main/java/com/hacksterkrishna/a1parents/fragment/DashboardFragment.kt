package com.hacksterkrishna.a1parents.fragment

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crashlytics.android.Crashlytics
import com.hacksterkrishna.a1parents.Constants
import com.hacksterkrishna.a1parents.R
import com.hacksterkrishna.a1parents.Utils
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private var pref: SharedPreferences? = null
    private var utils = Utils()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity.title="Dashboard"
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Fabric.with(activity, Crashlytics())
        val fabric = Fabric.Builder(activity)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)

        pref = activity.getSharedPreferences("parentPrefs", Context.MODE_PRIVATE)
        tv_db_name.text=pref!!.getString(Constants.NAME,"Name")
        tv_db_no.text=pref!!.getString(Constants.NUMBER,"Number")
        tv_db_sname.text=pref!!.getString(Constants.SNAME,"Student Name")
        tv_db_school.text=pref!!.getString(Constants.SSCHOOL,"School")
        tv_db_class.text=utils.getStandardName(pref!!.getString(Constants.SSTANDARD,"Class"))+" - "+pref!!.getString(Constants.SSEC,"Sec")

    }

    companion object {

        fun newInstance(): DashboardFragment {

            return DashboardFragment()
        }
    }
}