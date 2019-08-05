package com.hacksterkrishna.a1teachers.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.crashlytics.android.Crashlytics
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.hacksterkrishna.a1teachers.Constants
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.RequestInterface
import com.hacksterkrishna.a1teachers.Utils
import com.hacksterkrishna.a1teachers.models.Complaint
import com.hacksterkrishna.a1teachers.models.ServerRequest
import com.hacksterkrishna.a1teachers.models.ServerResponse
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_complaint_send.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.design.longSnackbar
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by krishna on 31/12/17.
 */
class ComplaintSendActivity:AppCompatActivity(){

    private var sid:String?=null
    private var sname:String?=null
    private var sclass:String?=null
    private var ssec:String?=null
    private var sroll:String?=null
    private var spnumber:String?=null
    private var utils = Utils()
    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    private var mCompositeDisposable: CompositeDisposable? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_complaint_send)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        pref=getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        sid = intent.getStringExtra("sid")
        sname = intent.getStringExtra("sname")
        sclass = intent.getStringExtra("sclass")
        ssec= intent.getStringExtra("ssec")
        sroll = intent.getStringExtra("sroll")
        spnumber = intent.getStringExtra("spnumber")


        this.title= "Message $sname's Parent"
        mCompositeDisposable = CompositeDisposable()

        tv_sd_name.text=sname
        tv_sd_class.text=utils.getStandardName(sclass!!)+" - $ssec"
        tv_sd_roll.text=sroll
        btn_mtp_send.setOnClickListener{
            val message= et_mtp_msg!!.text
            if(message==null || message.isEmpty()){
                longSnackbar(complaint_send_layout, "Field Empty")
            }
            else if(message.length>140) {
                longSnackbar(complaint_send_layout, "Can't exceed 140 chars")
            } else {
                SendComplaint(message.toString())
            }
        }
    }

    fun SendComplaint(message:String){

        mtp_progress.visibility=View.VISIBLE
        btn_mtp_send!!.visibility=View.GONE

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val complaint = Complaint()
        complaint.setTid(pref!!.getInt(Constants.ID,0))
        complaint.setTname(pref!!.getString(Constants.NAME,"Name"))
        complaint.setSchoolcode(pref!!.getString(Constants.SCHOOL_CODE,"SchoolCode"))
        complaint.setSid(sid!!)
        complaint.setSname(sname!!)
        complaint.setStandard(sclass!!)
        complaint.setSection(ssec!!)
        complaint.setText(message.trim())
        complaint.setPnumber(spnumber!!)
        val request = ServerRequest()
        request.setOperation(Constants.SEND_MTP_OPERATION)
        request.setComplaint(complaint)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))

    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            mtp_progress!!.visibility = View.GONE
            btn_mtp_send!!.visibility = View.VISIBLE
            et_mtp_msg!!.setText("")
            longSnackbar(complaint_send_layout, resp.getMessage())

        } else{

            mtp_progress!!.visibility = View.GONE
            btn_mtp_send!!.visibility = View.VISIBLE
            longSnackbar(complaint_send_layout, resp.getMessage())


        }


    }

    private fun handleError(error: Throwable) {

        mtp_progress!!.visibility = View.GONE
        btn_mtp_send!!.visibility = View.VISIBLE
        Log.d(Constants.TAG, "failed")
        longSnackbar(complaint_send_layout, error.localizedMessage)

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
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