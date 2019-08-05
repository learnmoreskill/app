package com.hacksterkrishna.a1teachers.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.models.HomeworkNotDone
import com.hacksterkrishna.a1teachers.models.Student

/**
 * Created by krishna on 31/12/17.
 */
class HomeworkNotDoneAdapter(private val students: ArrayList<Student>,var HwndDataList: ArrayList<HomeworkNotDone>) : RecyclerView.Adapter<HomeworkNotDoneAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): HomeworkNotDoneAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.hwnd_list_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: HomeworkNotDoneAdapter.ViewHolder, i: Int) {
        viewHolder.tv_details.text = students[i].getSroll() + ". " + students[i].getSname()
        if(HwndDataList[i].hwndstatus == "ND") {
            viewHolder.cb_status.isChecked=true
        } else if(HwndDataList[i].hwndstatus == "D") {
            viewHolder.cb_status.isChecked=false
        }
        viewHolder.cb_status.setOnClickListener {
            updateStatus(i, viewHolder.cb_status.isChecked)
        }

    }

    fun updateStatus(index: Int, status: Boolean) {
        val statusText = if (status) {
            "ND"
        } else {
            "D"
        }

        HwndDataList[index].hwndstatus = statusText

    }

    override fun getItemCount(): Int {
        return students.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_details: TextView = itemView.findViewById(R.id.tv_details)
        val cb_status: CheckBox = itemView.findViewById(R.id.cb_status)

    }

}