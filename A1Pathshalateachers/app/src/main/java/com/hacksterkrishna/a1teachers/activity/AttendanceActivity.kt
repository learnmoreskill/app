package com.hacksterkrishna.a1teachers.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.crashlytics.android.Crashlytics
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.hacksterkrishna.a1teachers.Constants
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.RequestInterface
import com.hacksterkrishna.a1teachers.Utils
import com.hacksterkrishna.a1teachers.adapter.AttendanceListAdapter
import com.hacksterkrishna.a1teachers.models.*
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_attendance.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.design.longSnackbar
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by krishna on 31/12/17.
 */
class AttendanceActivity:AppCompatActivity(){
    private var attendance_list: ArrayList<Student>? = null
    private var attendance_adapter: AttendanceListAdapter? = null
    private var utils = Utils()
    var attendance_data_list = ArrayList<AttendanceData>()
    private var mCompositeDisposable: CompositeDisposable? = null

    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_attendance)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pref = getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        this.title="Attendance"
        attendance_class.text="{cmd-school} "+utils.getStandardName(pref!!.getString(Constants.STANDARD,"Class"))+" - "+pref!!.getString(Constants.SEC,"Sec")
        val currentDate = Date()
        val today= SimpleDateFormat("yyyy-MM-dd").format(currentDate)
        attendance_date.text="{gmi-calendar} "+utils.prettifyDate(today)
        attendance_card_recycler_view.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        attendance_card_recycler_view.layoutManager = layoutManager
        mCompositeDisposable = CompositeDisposable()
        CreateList()
    }

    private fun CreateList(){
        attendance_shimmer_recycler_view.addItemDecoration(HorizontalDividerItemDecoration.Builder(this@AttendanceActivity).color(R.color.colorAccent).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build())
        //attendance_progress.visibility=View.VISIBLE
        //card_attendance.visibility=View.GONE
        attendance_shimmer_recycler_view.showShimmerAdapter()

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val teacher = Teacher()
        teacher.setStandard(pref!!.getString(Constants.STANDARD,"Class"))
        teacher.setSec(pref!!.getString(Constants.SEC,"Sec"))
        val request = ServerRequest()
        request.setOperation(Constants.FETCH_ALIST)
        request.setTeacher(teacher)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            attendance_list=resp.getStudents()
            for (data in attendance_list!!){
                attendance_data_list.add(AttendanceData(data.getSid(),data.getSroll(),data.getSname(),"P",data.getSchoolCode(),data.getSclass(),data.getSsec(),data.getSpnumber()))
            }
            attendance_adapter = AttendanceListAdapter(attendance_list!!,attendance_data_list)
            //attendance_progress.visibility = View.GONE
            attendance_shimmer_recycler_view.hideShimmerAdapter()
            //card_attendance.visibility = View.VISIBLE
            attendance_card_recycler_view.adapter = attendance_adapter
            attendance_card_recycler_view.addItemDecoration(HorizontalDividerItemDecoration.Builder(this@AttendanceActivity).color(R.color.colorAccent).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build())
            btn_attendance_submit.visibility = View.VISIBLE
            btn_attendance_submit.setOnClickListener{
                val attendanceData=attendance_adapter!!.AttendanceDataList
                submitData(attendanceData)
            }

        } else{

            //attendance_progress.visibility = View.GONE
            attendance_shimmer_recycler_view.hideShimmerAdapter()
            card_attendance.visibility = View.GONE
            longSnackbar(attendance_layout, resp.getMessage())

        }

    }

    private fun handleError(error: Throwable) {

        //attendance_progress.visibility = View.GONE
        attendance_shimmer_recycler_view.hideShimmerAdapter()
        card_attendance.visibility = View.GONE
        longSnackbar(attendance_layout, error.localizedMessage)
        Log.d(Constants.TAG, "failed")

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    fun submitData(attendanceData: ArrayList<AttendanceData>){

        attendance_progress.visibility=View.VISIBLE
        card_attendance.visibility=View.GONE
        btn_attendance_submit.visibility=View.GONE

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

        val attendance = Attendance()
        attendance.setStandard(pref!!.getString(Constants.STANDARD,"Class"))
        attendance.setSec(pref!!.getString(Constants.SEC,"Sec"))
        attendance.setSchoolcode(pref!!.getString(Constants.SCHOOL_CODE,"Schoolcode"))
        attendance.setAttendanceData(attendanceData)
        val request = ServerRequest()
        request.setOperation(Constants.SUBMIT_ADATA)
        request.setAttendance(attendance)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleSubmitResponse, this::handleSubmitError))
    }

    private fun handleSubmitResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {
            attendance_progress.visibility = View.GONE
            card_attendance.visibility = View.GONE
            btn_attendance_submit.visibility = View.GONE
            iv_attendance_error.setImageResource(R.drawable.ic_correct)
            tv_attendance_error_msg.text=resp.getMessage()
            attendance_error_card.visibility=View.VISIBLE
        } else{
            attendance_progress.visibility = View.GONE
            card_attendance.visibility = View.GONE
            btn_attendance_submit.visibility=View.GONE
            iv_attendance_error.setImageResource(R.drawable.ic_error3)
            tv_attendance_error_msg.text=resp.getMessage()
            attendance_error_card.visibility=View.VISIBLE

        }

    }

    private fun handleSubmitError(error: Throwable) {


        attendance_progress.visibility = View.GONE
        card_attendance.visibility = View.GONE
        btn_attendance_submit.visibility=View.GONE
        iv_attendance_error.setImageResource(R.drawable.ic_error3)
        tv_attendance_error_msg.text=error.localizedMessage
        attendance_error_card.visibility=View.VISIBLE
        Log.d(Constants.TAG, "failed")


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