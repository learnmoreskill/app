package com.hacksterkrishna.a1parents.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1parents.Utils
import com.hacksterkrishna.a1parents.R
import com.hacksterkrishna.a1parents.model.Homework

/**
 * Created by krishna on 31/12/17.
 */
class HomeworkDataAdapter(private val homeWork: ArrayList<Homework>) : RecyclerView.Adapter<HomeworkDataAdapter.ViewHolder>() {

    private var utils = Utils()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.hw_card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.tv_hw_topic.text = homeWork[i].htopic!!.trim()
        viewHolder.tv_hw_subject.text = "{cmd-book} "+homeWork[i].hsubject!!.trim()
        viewHolder.itv_hw_time.text = "{cmd-calendar} "+utils.prettifyDateTime(homeWork[i].hclock!!)
        viewHolder.itv_hw_tname.text = "{cmd-account-circle} "+homeWork[i].htname
        viewHolder.itv_hw_class.text = "{cmd-school} "+utils.getStandardName(homeWork[i].hclass!!)+"-"+homeWork[i].hsec

    }

    override fun getItemCount(): Int {
        return homeWork.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_hw_topic: TextView = itemView.findViewById(R.id.tv_hw_topic)
        val tv_hw_subject: IconicsTextView = itemView.findViewById(R.id.tv_hw_subject)
        val itv_hw_time: IconicsTextView = itemView.findViewById(R.id.itv_hw_time)
        val itv_hw_tname: IconicsTextView = itemView.findViewById(R.id.itv_hw_tname)
        val itv_hw_class: IconicsTextView = itemView.findViewById(R.id.itv_hw_class)

    }

}