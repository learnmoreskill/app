package com.hacksterkrishna.a1principal.adapter

import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mikepenz.iconics.view.IconicsTextView
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.activity.StudentDetailsActivity
import com.hacksterkrishna.a1principal.models.Student

/**
 * Created by krishna on 31/12/17.
 */
class StudentsDataAdapter(private val students: ArrayList<Student>) : RecyclerView.Adapter<StudentsDataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.sd_search_card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.sd_search_name.text = students[i].getSname()
        viewHolder.itv_sd_search_class.text = "{cmd-school} "+students[i].getSclass()+"-"+students[i].getSsec()
        viewHolder.itv_sd_search_rollno.text = "{cmd-file-document-box} "+students[i].getSroll()
        viewHolder.sd_search_card_view.setOnClickListener({ v ->

            val detailActivity = Intent(v.context, StudentDetailsActivity::class.java)
            detailActivity.putExtra("sid", students[i].getSid())
            detailActivity.putExtra("sname", students[i].getSname())
            detailActivity.putExtra("sclass", students[i].getSclass())
            detailActivity.putExtra("ssec", students[i].getSsec())
            detailActivity.putExtra("sroll", students[i].getSroll())
            detailActivity.putExtra("semail", students[i].getSemail())
            detailActivity.putExtra("saddress", students[i].getSaddress())
            detailActivity.putExtra("spname", students[i].getSpname())
            detailActivity.putExtra("spnumber", students[i].getSpnumber())
            detailActivity.putExtra("spphone", students[i].getSpphone())
            detailActivity.putExtra("spemail", students[i].getSpemail())
            v.context.startActivity(detailActivity)

        })

    }

    override fun getItemCount(): Int {
        return students.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sd_search_name: TextView = itemView.findViewById(R.id.sd_search_name)
        val itv_sd_search_class: IconicsTextView = itemView.findViewById(R.id.itv_sd_search_class)
        val itv_sd_search_rollno: IconicsTextView = itemView.findViewById(R.id.itv_sd_search_rollno)
        val sd_search_card_view: CardView = itemView.findViewById(R.id.sd_search_cardview)

    }

}