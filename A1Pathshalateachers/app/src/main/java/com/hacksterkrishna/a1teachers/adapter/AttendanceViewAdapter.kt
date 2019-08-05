package com.hacksterkrishna.a1teachers.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.Utils
import com.hacksterkrishna.a1teachers.models.AttendanceView

/**
 * Created by krishna on 31/12/17.
 */

class AttendanceViewAdapter(private val attendanceview: ArrayList<AttendanceView>) : RecyclerView.Adapter<AttendanceViewAdapter.ViewHolder>() {

    private val utils = Utils()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.attendance_view_list_item, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.tv_attendance_view_list_details.text = attendanceview[i].getRoll()+". "+attendanceview[i].getName()
        //viewHolder.tv_attendance_type_1_list_status.text = attendance[i].getStatus()
        if(attendanceview[i].getStatus() == "A") {
            viewHolder.tv_attendance_view_list_status.text = "{cmd-close}"
        } else if(attendanceview[i].getStatus() == "P") {
            viewHolder.tv_attendance_view_list_status.text = "{cmd-check}"
        } else {
            viewHolder.tv_attendance_view_list_status.text = "{cmd-minus}"
        }

    }

    override fun getItemCount(): Int {
        return attendanceview.size
    }

    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val tv_attendance_view_list_details: TextView = itemView.findViewById(R.id.tv_attendance_view_list_details)
        val tv_attendance_view_list_status: IconicsTextView = itemView.findViewById(R.id.tv_attendance_view_list_status)

    }
}