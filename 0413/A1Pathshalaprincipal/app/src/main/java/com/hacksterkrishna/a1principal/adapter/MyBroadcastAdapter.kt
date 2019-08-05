package com.hacksterkrishna.a1principal.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.Utils
import com.hacksterkrishna.a1principal.models.BroadcastMessage

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
        viewHolder.tv_brd_message.text = broadcastMessage[i].getText()
        viewHolder.itv_brd_time.text = "{gmi-calendar} "+utils.prettifyDateTime(broadcastMessage[i].getDate())
        viewHolder.itv_brd_pname.text = "{gmi-account-circle} "+broadcastMessage[i].getPname()

    }

    override fun getItemCount(): Int {
        return broadcastMessage.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_brd_message: TextView = itemView.findViewById(R.id.tv_brd_message)
        val itv_brd_time: IconicsTextView = itemView.findViewById(R.id.itv_brd_time)
        val itv_brd_pname: IconicsTextView = itemView.findViewById(R.id.itv_brd_pname)

    }

}