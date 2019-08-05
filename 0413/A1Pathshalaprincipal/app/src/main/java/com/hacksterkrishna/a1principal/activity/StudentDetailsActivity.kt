package com.hacksterkrishna.a1principal.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.crashlytics.android.Crashlytics
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.hacksterkrishna.a1principal.R
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_student_details.*
/**
 * Created by krishna on 31/12/17.
 */
class StudentDetailsActivity : AppCompatActivity() {

    private var sid:String?=null
    private var sroll:String?=null
    private var sname:String?=null
    private var saddress:String?=null
    private var semail:String?=null
    private var spname:String?=null
    private var spnumber:String?=null
    private var spphone:String?=null
    private var spemail:String?=null
    private var sclass:String?=null
    private var ssec:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_student_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        sid = intent.getStringExtra("sid")
        sname = intent.getStringExtra("sname")
        sroll = intent.getStringExtra("sroll")
        sclass = intent.getStringExtra("sclass")
        ssec = intent.getStringExtra("ssec")
        semail = intent.getStringExtra("semail")
        saddress = intent.getStringExtra("saddress")
        spname = intent.getStringExtra("spname")
        spemail = intent.getStringExtra("spemail")
        spnumber = intent.getStringExtra("spnumber")
        spphone = intent.getStringExtra("spphone")
        this.title=sname+"'s Details"

        bt_sd_view_attendance.setOnClickListener({_ ->
            val attendanceDetailActivity = Intent(this, StudentDetailAttendance::class.java)
            attendanceDetailActivity.putExtra("sid", sid)
            attendanceDetailActivity.putExtra("sname", sname)
            startActivity(attendanceDetailActivity)
        })

        bt_sd_view_complaint.setOnClickListener({_ ->
            val complaintDetailActivity = Intent(this, StudentDetailComplaint::class.java)
            complaintDetailActivity.putExtra("sid", sid)
            complaintDetailActivity.putExtra("sname", sname)
            startActivity(complaintDetailActivity)
        })

        bt_sd_view_hwnotdone.setOnClickListener({_ ->
            val hwnotdoneDetailActivity = Intent(this, StudentDetailHomework::class.java)
            hwnotdoneDetailActivity.putExtra("sid", sid)
            hwnotdoneDetailActivity.putExtra("sname", sname)
            startActivity(hwnotdoneDetailActivity)
        })

        setCardData()

    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }

    private fun setCardData(){
        sd_view_sname.text=sname
        tv_sd_view_class.text="{cmd-school} "+sclass+"-"+ssec
        tv_sd_view_rollno.text="{cmd-file-document-box} "+sroll
        if(semail!!.isEmpty()){ tv_sd_view_email.visibility= View.GONE } else { tv_sd_view_email.text="{cmd-email} "+semail }
        if(saddress!!.isEmpty()){ tv_sd_view_address.visibility=View.GONE } else {tv_sd_view_address.text="{cmd-home} "+saddress }
        sd_view_spname.text=spname
        tv_sd_view_spnumber.text="{cmd-cellphone-android} "+spphone
        if(spemail!!.isEmpty()){ tv_sd_view_spemail.visibility=View.GONE } else {tv_sd_view_spemail.text="{cmd-email} "+spemail }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                //NavUtils.navigateUpFromSameTask(this)
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
