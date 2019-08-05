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
import com.hacksterkrishna.a1teachers.activity.ComplaintSendActivity
import com.hacksterkrishna.a1teachers.models.Student
import org.jetbrains.anko.startActivity

/**
 * Created by krishna on 31/12/17.
 */
class StudentsDataAdapter(private val students: ArrayList<Student>) : RecyclerView.Adapter<StudentsDataAdapter.ViewHolder>() {

    private var utils = Utils()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): StudentsDataAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.sd_search_card, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: StudentsDataAdapter.ViewHolder, i: Int) {
        viewHolder.sd_search_name.text = students[i].getSname()
        viewHolder.itv_sd_search_class.text = "{cmd-school} "+utils.getStandardName(students[i].getSclass())+"-"+students[i].getSsec()
        viewHolder.itv_sd_search_rollno.text = "{cmd-file-document-box} "+students[i].getSroll()
        viewHolder.sd_search_card_view.setOnClickListener { v ->

            v.context.startActivity<ComplaintSendActivity>("sid" to students[i].getSid(),"sname" to students[i].getSname()
                    , "sclass" to students[i].getSclass(), "ssec" to students[i].getSsec(),"sroll" to students[i].getSroll()
                    , "semail" to students[i].getSemail(), "saddress" to students[i].getSaddress(),"spname" to students[i].getSpname()
                    , "spnumber" to students[i].getSpnumber(),"spemail" to students[i].getSpemail())

        }

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