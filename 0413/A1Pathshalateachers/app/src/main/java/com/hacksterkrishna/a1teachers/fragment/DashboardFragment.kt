package com.hacksterkrishna.a1teachers.fragment

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.crashlytics.android.Crashlytics
import com.hacksterkrishna.a1teachers.Constants
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.Utils
import io.fabric.sdk.android.Fabric

/**
 * Created by krishna on 31/12/17.
 */
class DashboardFragment:Fragment(){

    private var pref: SharedPreferences? = null
    private var tv_db_name: TextView? = null
    private var tv_db_email: TextView? = null
    private var tv_db_address: TextView? = null
    private var tv_db_school: TextView? = null
    private var tv_db_standard: TextView? = null
    private var utils = Utils()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Fabric.with(activity, Crashlytics())
        val view = inflater!!.inflate(R.layout.fragment_dashboard, container, false)
        activity.title = "Dashboard"
        initViews(view)
        return view
    }

    private fun initViews(view: View) {

        pref = activity.getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)

        tv_db_name=view.findViewById(R.id.tv_db_name)
        tv_db_name!!.text=pref!!.getString(Constants.NAME,"Name")
        tv_db_email=view.findViewById(R.id.tv_db_email)
        tv_db_email!!.text=pref!!.getString(Constants.EMAIL,"Email")
        tv_db_school=view.findViewById(R.id.tv_db_school)
        tv_db_school!!.text=pref!!.getString(Constants.SCHOOL,"School")
        tv_db_address=view.findViewById(R.id.tv_db_address)
        tv_db_address!!.text=pref!!.getString(Constants.ADDRESS,"Nepal")
        tv_db_standard=view.findViewById(R.id.tv_db_class)
        tv_db_standard!!.text=utils.getStandardName(pref!!.getString(Constants.STANDARD,"class"))+" - "+pref!!.getString(Constants.SEC,"sec")

    }


}