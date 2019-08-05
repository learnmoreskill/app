package com.hacksterkrishna.a1teachers.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.Utils
import com.hacksterkrishna.a1teachers.models.BroadcastMessage

/**
 * Created by krishna on 31/12/17.
 */
class MyBroadcastAdapter(private val broadcastMessage: ArrayList<BroadcastMessage>) : RecyclerView.Adapter<MyBroadcastAdapter.ViewHolder>() {

    private var utils = Utils()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.brd_card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.tv_brd_message.text = broadcastMessage[i].getText().trim()
        viewHolder.tv_brd_class.text = "{gmi-graduation-cap} "+utils.getStandardName(broadcastMessage[i].getStandard())+" - "+broadcastMessage[i].getSec()
        viewHolder.itv_brd_time.text = "{gmi-calendar} "+utils.prettifyDateTime(broadcastMessage[i].getDateTime())
        viewHolder.itv_brd_tname.text = "{gmi-account-circle} "+broadcastMessage[i].getTname()

    }

    override fun getItemCount(): Int {
        return broadcastMessage.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_brd_message: TextView = itemView.findViewById(R.id.tv_brd_message)
        val tv_brd_class: TextView = itemView.findViewById(R.id.itv_brd_class)
        val itv_brd_time: IconicsTextView = itemView.findViewById(R.id.itv_brd_time)
        val itv_brd_tname: IconicsTextView = itemView.findViewById(R.id.itv_brd_tname)

    }

}