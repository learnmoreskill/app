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
import com.hacksterkrishna.a1teachers.adapter.HomeworkNotDoneAdapter
import com.hacksterkrishna.a1teachers.models.*
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_hw_notdone.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.design.longSnackbar
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by krishna on 31/12/17.
 */
class HomeworkNotDoneActivity:AppCompatActivity(){

    private var standard:String?=null
    private var sec:String?=null
    private var subject:String?=null
    private var topic:String?=null
    private var date:String?=null
    private var utils = Utils()
    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    private var hwnd_student_list: ArrayList<Student>? = null
    private var hwnd_data_list= ArrayList<HomeworkNotDone>()
    private var hwnd_adapter: HomeworkNotDoneAdapter? = null
    private var mCompositeDisposable: CompositeDisposable? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_hw_notdone)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        pref=getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        standard = intent.getStringExtra("standard")
        sec = intent.getStringExtra("sec")
        subject = intent.getStringExtra("subject")
        topic= intent.getStringExtra("topic")
        date = intent.getStringExtra("date")
        this.title= "Report HW not done"
        hw_class.text="{cmd-school} "+utils.getStandardName(standard!!)+" - "+sec
        hw_subject.text="{cmd-book-open-page-variant} "+subject
        hw_topic.text="{cmd-format-list-checks} "+topic
        hw_date.text="{gmi-calendar} "+utils.prettifyDate(date!!)
        hwnd_recycler_view.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        hwnd_recycler_view.layoutManager = layoutManager
        mCompositeDisposable = CompositeDisposable()
        CreateList()

    }

    private fun CreateList(){
        //attendance_shimmer_recycler_view.addItemDecoration(HorizontalDividerItemDecoration.Builder(this@AttendanceActivity).color(R.color.colorAccent).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build())
        hwnd_progress.visibility=View.VISIBLE
        card_hwnd_list.visibility=View.GONE
        //attendance_shimmer_recycler_view.showShimmerAdapter()

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val teacher = Teacher()
        teacher.setStandard(standard!!)
        teacher.setSec(sec!!)
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

            hwnd_student_list=resp.getStudents()
            for(data in hwnd_student_list!!){
                hwnd_data_list.add(HomeworkNotDone(data.getSid(), subject, "D", date, data.getSpnumber()))
            }
            hwnd_adapter = HomeworkNotDoneAdapter(hwnd_student_list!!,hwnd_data_list)
            hwnd_progress.visibility = View.GONE
            //attendance_shimmer_recycler_view.hideShimmerAdapter()
            card_hwnd_list.visibility = View.VISIBLE
            hwnd_recycler_view.adapter = hwnd_adapter
            hwnd_recycler_view.addItemDecoration(HorizontalDividerItemDecoration.Builder(this@HomeworkNotDoneActivity).color(R.color.colorAccent).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build())
            btn_hwnd_submit.visibility = View.VISIBLE
            btn_hwnd_submit.setOnClickListener{
                val hwndData=hwnd_adapter!!.HwndDataList
                submitData(hwndData)
            }

        } else{

            hwnd_progress.visibility = View.GONE
            //attendance_shimmer_recycler_view.hideShimmerAdapter()
            card_hwnd_list.visibility = View.GONE
            longSnackbar(hwnd_layout, resp.getMessage())

        }


    }

    private fun handleError(error: Throwable) {

        hwnd_progress.visibility = View.GONE
        //attendance_shimmer_recycler_view.hideShimmerAdapter()
        card_hwnd_list.visibility = View.GONE
        longSnackbar(hwnd_layout, error.localizedMessage)
        Log.d(Constants.TAG, "failed")

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    fun submitData(hwndData: ArrayList<HomeworkNotDone>){

        hwnd_progress.visibility=View.VISIBLE
        card_hwnd_list.visibility=View.GONE
        btn_hwnd_submit.visibility=View.GONE

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

        val request = ServerRequest()
        request.setOperation(Constants.SUBMIT_HWND_DATA)
        request.setHwnd(hwndData)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleSubmitResponse, this::handleSubmitError))

    }


    private fun handleSubmitResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {
            hwnd_progress.visibility = View.GONE
            card_hwnd_list.visibility = View.GONE
            btn_hwnd_submit.visibility = View.GONE
            iv_hwnd_error.setImageResource(R.drawable.ic_correct)
            tv_hwnd_error_msg.text=resp.getMessage()
            hwnd_error_card.visibility=View.VISIBLE
        } else{
            hwnd_progress.visibility = View.GONE
            card_hwnd_list.visibility = View.GONE
            btn_hwnd_submit.visibility=View.GONE
            iv_hwnd_error.setImageResource(R.drawable.ic_error3)
            tv_hwnd_error_msg.text=resp.getMessage()
            hwnd_error_card.visibility=View.VISIBLE

        }


    }

    private fun handleSubmitError(error: Throwable) {

        hwnd_progress.visibility = View.GONE
        card_hwnd_list.visibility = View.GONE
        btn_hwnd_submit.visibility=View.GONE
        iv_hwnd_error.setImageResource(R.drawable.ic_error3)
        tv_hwnd_error_msg.text=error.localizedMessage
        hwnd_error_card.visibility=View.VISIBLE
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