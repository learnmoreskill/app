package com.hacksterkrishna.a1principal.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.Utils
import com.hacksterkrishna.a1principal.models.Homework

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
        viewHolder.tv_hw_topic.text = homeWork[i].getTopic()
        viewHolder.tv_hw_subject.text = "{gmi-book} "+homeWork[i].getSubject()
        viewHolder.itv_hw_time.text = "{gmi-calendar} "+utils.prettifyDateTime(homeWork[i].getTime())
        viewHolder.itv_hw_tname.text = "{gmi-account-circle} "+homeWork[i].getTname()
        viewHolder.itv_hw_class.text = "{gmi-graduation-cap} "+homeWork[i].getStandard()+"-"+homeWork[i].getSection()

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