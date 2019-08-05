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
import com.hacksterkrishna.a1teachers.adapter.AttendanceEditListAdapter
import com.hacksterkrishna.a1teachers.models.*
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_attendance_edit.*
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
class AttendanceEditActivity: AppCompatActivity(){
    private var attendance_edit_list: ArrayList<AttendanceEditData>? = null
    private var attendance_edit_data_list: ArrayList<AttendanceEditData>? = null
    private var attendance_edit_adapter: AttendanceEditListAdapter? = null
    private var utils= Utils()
    private var mCompositeDisposable: CompositeDisposable? = null

    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_attendance_edit)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        pref = getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        this.title="Attendance Edit"
        attendance_edit_class.text="{cmd-school} "+utils.getStandardName(pref!!.getString(Constants.STANDARD,"Class"))+" - "+pref!!.getString(Constants.SEC,"Sec")
        val currentDate = Date()
        val today= SimpleDateFormat("yyyy-MM-dd").format(currentDate)
        attendance_edit_date.text="{gmi-calendar} "+utils.prettifyDate(today)
        attendance_edit_card_recycler_view.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        attendance_edit_card_recycler_view.layoutManager = layoutManager
        mCompositeDisposable = CompositeDisposable()
        CreateList()
    }

    private fun CreateList(){
        attendance_edit_shimmer_recycler_view.addItemDecoration(HorizontalDividerItemDecoration.Builder(this@AttendanceEditActivity).color(R.color.colorAccent).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build())
        //attendance_edit_progress.visibility= View.VISIBLE
        //card_attendance_edit.visibility= View.GONE
        attendance_edit_shimmer_recycler_view.showShimmerAdapter()

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val teacher = Teacher()
        teacher.setStandard(pref!!.getString(Constants.STANDARD,"Class"))
        teacher.setSec(pref!!.getString(Constants.SEC,"Sec"))
        val request = ServerRequest()
        request.setOperation(Constants.FETCH_ALIST_EDIT)
        request.setTeacher(teacher)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            attendance_edit_list=resp.getAttendanceEdit()
            attendance_edit_data_list = attendance_edit_list
            attendance_edit_adapter = AttendanceEditListAdapter(attendance_edit_list!!,attendance_edit_data_list!!)
            //attendance_edit_progress.visibility = View.GONE
            //card_attendance_edit.visibility = View.VISIBLE
            attendance_edit_shimmer_recycler_view.hideShimmerAdapter()
            attendance_edit_card_recycler_view.adapter = attendance_edit_adapter
            attendance_edit_card_recycler_view.addItemDecoration(HorizontalDividerItemDecoration.Builder(this@AttendanceEditActivity).color(R.color.colorAccent).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build())
            btn_attendance_edit_submit.visibility = View.VISIBLE
            btn_attendance_edit_submit.setOnClickListener{
                val attendanceEditData=attendance_edit_adapter!!.AttendanceEditDataList
                submitData(attendanceEditData)
            }

        } else{

            //attendance_edit_progress.visibility = View.GONE
            attendance_edit_shimmer_recycler_view.hideShimmerAdapter()
            card_attendance_edit.visibility = View.GONE
            longSnackbar(attendance_edit_layout, resp.getMessage())

        }

    }

    private fun handleError(error: Throwable) {

        //attendance_edit_progress.visibility = View.GONE
        attendance_edit_shimmer_recycler_view.hideShimmerAdapter()
        card_attendance_edit.visibility = View.GONE
        longSnackbar(attendance_edit_layout, error.localizedMessage)
        Log.d(Constants.TAG, "failed")

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    fun submitData(attendanceEditData: ArrayList<AttendanceEditData>){

        attendance_edit_progress.visibility= View.VISIBLE
        card_attendance_edit.visibility= View.GONE
        btn_attendance_edit_submit.visibility= View.GONE

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
        attendance.setAttendanceEditData(attendanceEditData)
        val request = ServerRequest()
        request.setOperation(Constants.SUBMIT_ADATA_EDIT)
        request.setAttendance(attendance)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleSubmitResponse, this::handleSubmitError))
    }

    private fun handleSubmitResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {
            attendance_edit_progress.visibility = View.GONE
            card_attendance_edit.visibility = View.GONE
            btn_attendance_edit_submit.visibility = View.GONE
            iv_attendance_edit_error.setImageResource(R.drawable.ic_correct)
            tv_attendance_edit_error_msg.text=resp.getMessage()
            attendance_edit_error_card.visibility= View.VISIBLE
        } else{
            attendance_edit_progress.visibility = View.GONE
            card_attendance_edit.visibility = View.GONE
            btn_attendance_edit_submit.visibility= View.GONE
            iv_attendance_edit_error.setImageResource(R.drawable.ic_error3)
            tv_attendance_edit_error_msg.text=resp.getMessage()
            attendance_edit_error_card.visibility= View.VISIBLE

        }

    }

    private fun handleSubmitError(error: Throwable) {

        attendance_edit_progress.visibility = View.GONE
        card_attendance_edit.visibility = View.GONE
        btn_attendance_edit_submit.visibility= View.GONE
        iv_attendance_edit_error.setImageResource(R.drawable.ic_error3)
        tv_attendance_edit_error_msg.text=error.localizedMessage
        attendance_edit_error_card.visibility= View.VISIBLE
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