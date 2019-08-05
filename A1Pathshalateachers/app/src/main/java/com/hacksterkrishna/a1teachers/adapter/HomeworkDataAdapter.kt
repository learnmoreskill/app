package com.hacksterkrishna.a1teachers.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.Utils
import com.hacksterkrishna.a1teachers.activity.HomeworkNotDoneActivity
import com.hacksterkrishna.a1teachers.models.Homework
import org.jetbrains.anko.startActivity

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
        viewHolder.tv_hw_subject.text = "{gmi-book} "+homeWork[i].hsubject!!.trim()
        viewHolder.itv_hw_time.text = "{gmi-calendar} "+utils.prettifyDateTime(homeWork[i].hclock!!)
        viewHolder.itv_hw_tname.text = "{gmi-account-circle} "+homeWork[i].htname
        viewHolder.itv_hw_class.text = "{gmi-graduation-cap} "+utils.getStandardName(homeWork[i].hclass!!)+"-"+homeWork[i].hsec
        viewHolder.hw_card_view.setOnClickListener { v ->
            v.context.startActivity<HomeworkNotDoneActivity>("standard" to homeWork[i].hclass,"sec" to homeWork[i].hsec,"subject" to homeWork[i].hsubject,"topic" to homeWork[i].htopic,"date" to homeWork[i].hdate)
        }

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
        val hw_card_view: CardView = itemView.findViewById(R.id.hw_card_view)

    }

}