package com.hacksterkrishna.a1teachers.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.Utils
import com.hacksterkrishna.a1teachers.models.Complaint

/**
 * Created by krishna on 31/12/17.
 */
class MyComplaintAdapter(private val complaint: ArrayList<Complaint>) : RecyclerView.Adapter<MyComplaintAdapter.ViewHolder>() {

    private var utils = Utils()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.mtp_card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.tv_mtp_message.text = complaint[i].getText().trim()
        viewHolder.tv_mtp_sname.text = "{cmd-account} "+complaint[i].getSname()
        viewHolder.tv_mtp_class.text = "{gmi-graduation-cap} "+utils.getStandardName(complaint[i].getStandard())+" - "+complaint[i].getSec()
        viewHolder.itv_mtp_time.text = "{gmi-calendar} "+utils.prettifyDateTime(complaint[i].getDateTime())
        viewHolder.itv_mtp_tname.text = "{gmi-account-circle} "+complaint[i].getTname()

    }

    override fun getItemCount(): Int {
        return complaint.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_mtp_message: TextView = itemView.findViewById(R.id.tv_mtp_message)
        val tv_mtp_sname: TextView = itemView.findViewById(R.id.tv_mtp_sname)
        val tv_mtp_class: TextView = itemView.findViewById(R.id.itv_mtp_class)
        val itv_mtp_time: IconicsTextView = itemView.findViewById(R.id.itv_mtp_time)
        val itv_mtp_tname: IconicsTextView = itemView.findViewById(R.id.itv_mtp_tname)

    }

}