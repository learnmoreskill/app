package com.hacksterkrishna.a1teachers.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.models.GroupComplaint
import com.hacksterkrishna.a1teachers.models.Student

/**
 * Created by krishna on 31/12/17.
 */

class GroupComplaintAdapter(private val students: ArrayList<Student>, var GrpcDataList: ArrayList<GroupComplaint>) : RecyclerView.Adapter<GroupComplaintAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GroupComplaintAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.grpc_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: GroupComplaintAdapter.ViewHolder, i: Int) {
        viewHolder.tv_details.text = students[i].getSroll() + ". " + students[i].getSname()
        if(GrpcDataList[i].cstatus == "Y") {
            viewHolder.cb_status.isChecked=true
        } else if(GrpcDataList[i].cstatus == "N") {
            viewHolder.cb_status.isChecked=false
        }
        viewHolder.cb_status.setOnClickListener {
            updateStatus(i, viewHolder.cb_status.isChecked)
        }

    }

    fun updateStatus(index: Int, status: Boolean) {
        val statusText = if (status) {
            "Y"
        } else {
            "N"
        }

        GrpcDataList[index].cstatus = statusText

    }

    override fun getItemCount(): Int {
        return students.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_details: TextView = itemView.findViewById(R.id.tv_details)
        val cb_status: CheckBox = itemView.findViewById(R.id.cb_status)

    }

}