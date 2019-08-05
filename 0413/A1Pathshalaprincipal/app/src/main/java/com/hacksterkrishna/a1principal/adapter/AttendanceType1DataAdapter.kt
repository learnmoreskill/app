package com.hacksterkrishna.a1principal.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.Utils
import com.hacksterkrishna.a1principal.models.Attendance

/**
 * Created by krishna on 31/12/17.
 */
class AttendanceType1DataAdapter(private val attendance: ArrayList<Attendance>) : RecyclerView.Adapter<AttendanceType1DataAdapter.ViewHolder>() {

    private val utils = Utils()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.attendance_type1_list_item, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.tv_attendance_type_1_list_details.text = attendance[i].getRoll()+". "+attendance[i].getName()
        //viewHolder.tv_attendance_type_1_list_status.text = attendance[i].getStatus()
        if(attendance[i].getStatus().equals("A")) {
            viewHolder.tv_attendance_type_1_list_status.text = "{cmd-close}"
        } else if(attendance[i].getStatus().equals("P")) {
            viewHolder.tv_attendance_type_1_list_status.text = "{cmd-check}"
        } else {
            viewHolder.tv_attendance_type_1_list_status.text = "{cmd-minus}"
        }

    }

    override fun getItemCount(): Int {
        return attendance.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_attendance_type_1_list_details: TextView = itemView.findViewById(R.id.tv_attendance_type_1_list_details)
        val tv_attendance_type_1_list_status: IconicsTextView = itemView.findViewById(R.id.tv_attendance_type_1_list_status)

    }

}