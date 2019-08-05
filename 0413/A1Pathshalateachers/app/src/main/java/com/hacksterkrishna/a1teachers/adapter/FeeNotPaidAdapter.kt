package com.hacksterkrishna.a1teachers.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.models.FeeNotPaid
import com.hacksterkrishna.a1teachers.models.Student

/**
 * Created by krishna on 31/12/17.
 */
class FeeNotPaidAdapter(private val students: ArrayList<Student>,var FeenpDataList: ArrayList<FeeNotPaid>) : RecyclerView.Adapter<FeeNotPaidAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): FeeNotPaidAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.feenp_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: FeeNotPaidAdapter.ViewHolder, i: Int) {
        viewHolder.tv_details.text = students[i].getSroll() + ". " + students[i].getSname()
        if(FeenpDataList[i].feenpstatus == "NP") {
            viewHolder.cb_status.isChecked=true
        } else if(FeenpDataList[i].feenpstatus == "P") {
            viewHolder.cb_status.isChecked=false
        }
        viewHolder.cb_status.setOnClickListener {
            updateStatus(i, viewHolder.cb_status.isChecked)
        }

    }

    fun updateStatus(index: Int, status: Boolean) {
        val statusText = if (status) {
            "NP"
        } else {
            "P"
        }

        FeenpDataList[index].feenpstatus = statusText

    }

    override fun getItemCount(): Int {
        return students.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_details: TextView = itemView.findViewById(R.id.tv_details)
        val cb_status: CheckBox = itemView.findViewById(R.id.cb_status)

    }

}