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
import com.hacksterkrishna.a1teachers.adapter.AttendanceViewAdapter
import com.hacksterkrishna.a1teachers.models.AttendanceView
import com.hacksterkrishna.a1teachers.models.ServerRequest
import com.hacksterkrishna.a1teachers.models.ServerResponse
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_attendance_view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by krishna on 31/12/17.
 */
class AttendanceViewActivity:AppCompatActivity(){

    private var date:String?=null
    private var standard:String?=null
    private var sec:String?=null
    private var data: ArrayList<AttendanceView>? = null
    private var adapter: AttendanceViewAdapter? = null
    private val utils= Utils()
    private var mCompositeDisposable: CompositeDisposable? = null

    private var pref: SharedPreferences?= null
    private var Base_url: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_attendance_view)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        date=intent.getStringExtra("date")
        standard=intent.getStringExtra("standard")
        sec=intent.getStringExtra("sec")
        pref = getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        this.title="View Attendance"
        tv_attendance_view_heading_class.text="{cmd-school} "+standard+" - "+sec
        tv_attendance_view_heading_date.text="{gmi-calendar} "+utils.prettifyDate(date!!)
        mCompositeDisposable = CompositeDisposable()
        getAttendanceView()
    }

    private fun getAttendanceView(){

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val attendanceViewQuery = AttendanceView()
        attendanceViewQuery.setDate(date!!)
        attendanceViewQuery.setStandard(standard!!)
        attendanceViewQuery.setSection(sec!!)
        val request = ServerRequest()
        request.setOperation(Constants.FETCH_ATTENDANCE_VIEW_OPERATION)
        request.setAttendanceView(attendanceViewQuery)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
        attendance_view_progress.visibility= View.VISIBLE
    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            tv_attendance_view_count_absent.text = resp.getAcount()
            tv_attendance_view_count_present.text = resp.getPcount()

            data=resp.getAttendanceView()
            adapter = AttendanceViewAdapter(data!!)
            attendance_view_progress.visibility= View.GONE
            attendance_view_heading.visibility= View.VISIBLE
            attendance_view_count.visibility= View.VISIBLE
            attendance_view_heading_detail.visibility=View.VISIBLE
            attendance_view_list_card.visibility=View.VISIBLE
            attendance_view_recycler_view.addItemDecoration(HorizontalDividerItemDecoration.Builder(this@AttendanceViewActivity).color(R.color.colorAccent).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build())
            attendance_view_recycler_view.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(this@AttendanceViewActivity)
            attendance_view_recycler_view.layoutManager = layoutManager
            attendance_view_recycler_view.visibility = View.VISIBLE
            attendance_view_recycler_view.adapter = adapter

        } else{
            attendance_view_progress.visibility= View.GONE
            attendance_view_heading.visibility= View.GONE
            attendance_view_count.visibility= View.GONE
            attendance_view_heading_detail.visibility=View.GONE
            attendance_view_list_card.visibility=View.GONE
            attendance_view_recycler_view.visibility = View.GONE
            tv_attendance_view_error_msg.text=resp.getMessage()
            attendance_view_error_card.visibility= View.VISIBLE
            Log.d(Constants.TAG, resp.getMessage())


        }


    }

    private fun handleError(error: Throwable) {

        attendance_view_progress.visibility= View.GONE
        attendance_view_heading.visibility= View.GONE
        attendance_view_count.visibility= View.GONE
        attendance_view_heading_detail.visibility=View.GONE
        attendance_view_list_card.visibility=View.GONE
        attendance_view_recycler_view.visibility = View.GONE
        tv_attendance_view_error_msg.text=error.localizedMessage
        attendance_view_error_card.visibility= View.VISIBLE
        Log.d(Constants.TAG, error.localizedMessage)

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