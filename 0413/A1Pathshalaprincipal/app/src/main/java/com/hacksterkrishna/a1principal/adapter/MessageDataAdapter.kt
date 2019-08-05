package com.hacksterkrishna.a1principal.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.Utils
import com.hacksterkrishna.a1principal.models.Message

/**
 * Created by krishna on 31/12/17.
 */
class MessageDataAdapter(private val message: ArrayList<Message>) : RecyclerView.Adapter<MessageDataAdapter.ViewHolder>() {

    private var utils = Utils()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.msg_card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.tv_msg_message.text = message[i].getMessage()
        viewHolder.tv_msg_sname.text = "{cmd-face} "+message[i].getSname()
        viewHolder.itv_msg_time.text = "{gmi-calendar} "+utils.prettifyDateTime(message[i].getTime())
        viewHolder.itv_msg_tname.text = "{gmi-account-circle} "+message[i].getTname()
        viewHolder.itv_msg_class.text = "{gmi-graduation-cap} "+message[i].getStandard()+"-"+message[i].getSection()

    }

    override fun getItemCount(): Int {
        return message.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_msg_message: TextView = itemView.findViewById(R.id.tv_msg_message)
        val tv_msg_sname: IconicsTextView = itemView.findViewById(R.id.tv_msg_sname)
        val itv_msg_time: IconicsTextView = itemView.findViewById(R.id.itv_msg_time)
        val itv_msg_tname: IconicsTextView = itemView.findViewById(R.id.itv_msg_tname)
        val itv_msg_class: IconicsTextView = itemView.findViewById(R.id.itv_msg_class)

    }

}