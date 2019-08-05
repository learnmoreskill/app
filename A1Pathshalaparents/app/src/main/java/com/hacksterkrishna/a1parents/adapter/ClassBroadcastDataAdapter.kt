package com.hacksterkrishna.a1parents.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1parents.R
import com.hacksterkrishna.a1parents.Utils
import com.hacksterkrishna.a1parents.model.ClassBroadcast

/**
 * Created by krishna on 31/12/17.
 */

class ClassBroadcastDataAdapter(private val broadCast: ArrayList<ClassBroadcast>) : RecyclerView.Adapter<ClassBroadcastDataAdapter.ViewHolder>() {

    private var utils = Utils()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.bm_card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.tv_bm_message.text = broadCast[i].bmtext?.trim()
        viewHolder.itv_bm_time.text = "{cmd-calendar} "+utils.prettifyDateTime(broadCast[i].bmclock!!)
        viewHolder.itv_bm_tname.text = "{cmd-account-circle} "+broadCast[i].bmtname
        viewHolder.itv_bm_class.text = "{cmd-school} "+utils.getStandardName(broadCast[i].bmclass!!)+"-"+broadCast[i].bmsec

    }

    override fun getItemCount(): Int {
        return broadCast.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_bm_message: TextView = itemView.findViewById(R.id.tv_bm_message)
        val itv_bm_time: IconicsTextView = itemView.findViewById(R.id.itv_bm_time)
        val itv_bm_tname: IconicsTextView = itemView.findViewById(R.id.itv_bm_tname)
        val itv_bm_class: IconicsTextView = itemView.findViewById(R.id.itv_bm_class)

    }

}