package com.hacksterkrishna.a1principal.fragment

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hacksterkrishna.a1principal.Constants
import com.hacksterkrishna.a1principal.R

/**
 * Created by krishna on 31/12/17.
 */
class DashboardFragment:Fragment(){

    private var pref: SharedPreferences? = null
    private var tv_db_name: TextView? = null
    private var tv_db_email: TextView? = null
    private var tv_db_school: TextView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_dashboard, container, false)
        activity.title = "Dashboard"
        initViews(view)
        return view
    }

    private fun initViews(view: View) {

        pref = activity.getSharedPreferences("princiPrefs", Context.MODE_PRIVATE)

        tv_db_name=view.findViewById(R.id.tv_db_name)
        tv_db_name!!.text=pref!!.getString(Constants.NAME,"Name")
        tv_db_email=view.findViewById(R.id.tv_db_email)
        tv_db_email!!.text=pref!!.getString(Constants.EMAIL,"Email")
        tv_db_school=view.findViewById(R.id.tv_db_school)
        tv_db_school!!.text=pref!!.getString(Constants.SCHOOL,"School")

    }


}