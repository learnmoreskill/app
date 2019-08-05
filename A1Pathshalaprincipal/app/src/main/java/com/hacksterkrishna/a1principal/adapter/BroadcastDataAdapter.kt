package com.hacksterkrishna.a1principal.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.Utils
import com.hacksterkrishna.a1principal.models.Broadcast

/**
 * Created by krishna on 31/12/17.
 */

class BroadcastDataAdapter(private val broadCast: ArrayList<Broadcast>) : RecyclerView.Adapter<BroadcastDataAdapter.ViewHolder>() {

    private var utils = Utils()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.bm_card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.tv_bm_message.text = broadCast[i].getText()
        viewHolder.itv_bm_time.text = "{gmi-calendar} "+utils.prettifyDateTime(broadCast[i].getTime())
        viewHolder.itv_bm_tname.text = "{gmi-account-circle} "+broadCast[i].getTname()
        viewHolder.itv_bm_class.text = "{gmi-graduation-cap} "+broadCast[i].getStandard()+"-"+broadCast[i].getSection()

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