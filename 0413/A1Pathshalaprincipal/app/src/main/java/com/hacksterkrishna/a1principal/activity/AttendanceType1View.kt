package com.hacksterkrishna.a1principal.activity

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
import com.hacksterkrishna.a1principal.Constants
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.RequestInterface
import com.hacksterkrishna.a1principal.Utils
import com.hacksterkrishna.a1principal.adapter.AttendanceType1DataAdapter
import com.hacksterkrishna.a1principal.models.Attendance
import com.hacksterkrishna.a1principal.models.ServerRequest
import com.hacksterkrishna.a1principal.models.ServerResponse
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_attendance_type1_view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
/**
 * Created by krishna on 31/12/17.
 */
class AttendanceType1View : AppCompatActivity() {

    private var date:String?=null
    private var standard:String?=null
    private var sec:String?=null
    private var data: ArrayList<Attendance>? = null
    private var adapter: AttendanceType1DataAdapter? = null
    private val utils= Utils()
    private var mCompositeDisposable: CompositeDisposable? = null

    private var pref: SharedPreferences?= null
    private var Base_url: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_attendance_type1_view)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        date=intent.getStringExtra("date")
        standard=intent.getStringExtra("standard")
        sec=intent.getStringExtra("sec")
        this.title="View Attendance"

        pref=getSharedPreferences("princiPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        mCompositeDisposable = CompositeDisposable()
        tv_attendance_type_1_heading_class.text="Attendance Details for "+standard+" "+sec
        tv_attendance_type_1_heading_date.text="Date : "+utils.prettifyDate(date!!)
        getAttendanceType1()
    }

    private fun getAttendanceType1(){

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val attendanceType1Query = Attendance()
        attendanceType1Query.setDate(date!!)
        attendanceType1Query.setStandard(standard!!)
        attendanceType1Query.setSection(sec!!)
        val request = ServerRequest()
        request.setOperation(Constants.FETCH_ATTENDANCE_TYPE1_OPERATION)
        request.setAttendance(attendanceType1Query)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
        attendance_type_1_progress.visibility= View.VISIBLE

    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            tv_attendance_type_1_count_absent.text = resp.getAcount()
            tv_attendance_type_1_count_present.text = resp.getPcount()

            data=resp.getAttendance()
            adapter = AttendanceType1DataAdapter(data!!)
            attendance_type_1_progress.visibility= View.GONE
            attendance_type_1_heading.visibility= View.VISIBLE
            attendance_type_1_count.visibility= View.VISIBLE
            attendance_type_1_heading_detail.visibility=View.VISIBLE
            attendance_type_1_list_card.visibility=View.VISIBLE
            attendance_type_1_recycler_view.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(this@AttendanceType1View)
            attendance_type_1_recycler_view.layoutManager = layoutManager
            attendance_type_1_recycler_view.visibility = View.VISIBLE
            attendance_type_1_recycler_view.adapter = adapter

        } else{
            attendance_type_1_progress.visibility= View.GONE
            attendance_type_1_heading.visibility= View.GONE
            attendance_type_1_count.visibility= View.GONE
            attendance_type_1_heading_detail.visibility=View.GONE
            attendance_type_1_list_card.visibility=View.GONE
            attendance_type_1_recycler_view.visibility = View.GONE
            tv_attendance_type_1_error_msg.text=resp.getMessage()
            attendance_type_1_error_card.visibility= View.VISIBLE
            Log.d(Constants.TAG, resp.getMessage())


        }


    }

    private fun handleError(error: Throwable) {

        attendance_type_1_progress.visibility= View.GONE
        attendance_type_1_heading.visibility= View.GONE
        attendance_type_1_count.visibility= View.GONE
        attendance_type_1_heading_detail.visibility=View.GONE
        attendance_type_1_list_card.visibility=View.GONE
        attendance_type_1_recycler_view.visibility = View.GONE
        tv_attendance_type_1_error_msg.text=error.localizedMessage
        attendance_type_1_error_card.visibility= View.VISIBLE
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
