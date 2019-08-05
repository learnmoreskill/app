package com.hacksterkrishna.a1principal.adapter

import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.Utils
import com.hacksterkrishna.a1principal.activity.AttendanceType1View
import com.hacksterkrishna.a1principal.models.AttendanceLog

/**
 * Created by krishna on 31/12/17.
 */

class AttendanceType2DataAdapter(private val attendancelog: ArrayList<AttendanceLog>) : RecyclerView.Adapter<AttendanceType2DataAdapter.ViewHolder>() {

    private val utils = Utils()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.attendance_type_2_item, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itv_attendance_type_2_date.text = "{cmd-calendar} "+utils.prettifyDate(attendancelog[i].getDate())
        viewHolder.itv_attendance_type_2_acount.text = "{cmd-map-marker-off} "+attendancelog[i].getAcount()
        viewHolder.itv_attendance_type_2_pcount.text = "{cmd-map-marker} "+attendancelog[i].getPcount()

        viewHolder.attendance_type_2_item_cardview.setOnClickListener({ v ->

            val attendanceType1View = Intent(v.context, AttendanceType1View::class.java)
            attendanceType1View.putExtra("date", attendancelog[i].getDate())
            attendanceType1View.putExtra("standard", attendancelog[i].getStandard())
            attendanceType1View.putExtra("sec", attendancelog[i].getSec())
            v.context.startActivity(attendanceType1View)

        })

    }

    override fun getItemCount(): Int {
        return attendancelog.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itv_attendance_type_2_date: IconicsTextView = itemView.findViewById(R.id.itv_attendance_type_2_date)
        val itv_attendance_type_2_acount: IconicsTextView = itemView.findViewById(R.id.itv_attendance_type_2_acount)
        val itv_attendance_type_2_pcount: IconicsTextView = itemView.findViewById(R.id.itv_attendance_type_2_pcount)
        val attendance_type_2_item_cardview: CardView = itemView.findViewById(R.id.attendance_type_2_item_cardview)

    }

}