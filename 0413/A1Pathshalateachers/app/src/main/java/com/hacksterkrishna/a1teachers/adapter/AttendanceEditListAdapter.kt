package com.hacksterkrishna.a1teachers.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.models.AttendanceEditData

/**
 * Created by krishna on 31/12/17.
 */
class AttendanceEditListAdapter(private val attendanceedit: ArrayList<AttendanceEditData>,var AttendanceEditDataList: ArrayList<AttendanceEditData>) : RecyclerView.Adapter<AttendanceEditListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AttendanceEditListAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.attendance_edit_item, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: AttendanceEditListAdapter.ViewHolder, i: Int) {
        viewHolder.tv_details.text = attendanceedit[i].asroll!!+". "+attendanceedit[i].asname!!
        if(AttendanceEditDataList[i].astatus == "P") {
            viewHolder.cb_status.isChecked=true
            viewHolder.cb_status.text="P"
        } else if(AttendanceEditDataList[i].astatus == "A") {
            viewHolder.cb_status.isChecked=false
            viewHolder.cb_status.text="A"
        }
        viewHolder.cb_status.setOnClickListener {
            viewHolder.cb_status.text= if(viewHolder.cb_status.isChecked) { "P" } else{ "A" }
            updateStatus(i,viewHolder.cb_status.isChecked)
        }

    }

    fun updateStatus(index:Int,status:Boolean){
        var statusText = if(status) {
            "P"
        }
        else{
            "A"
        }

        AttendanceEditDataList[index].astatus=statusText

    }

    override fun getItemCount(): Int {
        return attendanceedit.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_details: TextView = itemView.findViewById(R.id.tv_details)
        val cb_status: CheckBox = itemView.findViewById(R.id.cb_status)

    }

}