package com.hacksterkrishna.a1teachers.fragment

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import org.jetbrains.anko.design.longSnackbar
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.crashlytics.android.Crashlytics
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.hacksterkrishna.a1teachers.Constants
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.RequestInterface
import com.hacksterkrishna.a1teachers.adapter.MyComplaintAdapter
import com.hacksterkrishna.a1teachers.models.Complaint
import com.hacksterkrishna.a1teachers.models.ServerRequest
import com.hacksterkrishna.a1teachers.models.ServerResponse
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by krishna on 31/12/17.
 */
class ComplaintHistoryFragment :Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private var et_mtp_date : EditText? = null
    private var tv_mtp_error_msg : TextView? = null
    private var mtp_error_card : CardView? = null
    private var mtp_recyclerView: RecyclerView? = null
    private var mtp_data: ArrayList<Complaint>? = null
    private var mtp_adapter: MyComplaintAdapter? = null
    private var mtp_progress: ProgressBar? = null
    private var isLaunch: Boolean = true
    private var today:String? = null
    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    private var mtp_card_shimmer_recycler_view:ShimmerRecyclerView? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Fabric.with(activity, Crashlytics())
        val view = inflater!!.inflate(R.layout.fragment_complaint_history, container, false)
        activity.title = "Message to parents"
        mCompositeDisposable = CompositeDisposable()
        pref=activity.getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        initViews(view)
        return view
    }

    private fun initViews(view: View){
        et_mtp_date = view.findViewById(R.id.et_mtp_date)
        mtp_progress = view.findViewById(R.id.mtp_progress)
        mtp_error_card = view.findViewById(R.id.mtp_error_card)
        tv_mtp_error_msg = view.findViewById(R.id.tv_mtp_error_msg)
        mtp_card_shimmer_recycler_view = view.findViewById(R.id.mtp_card_shimmer_recycler_view)
        mtp_recyclerView = view.findViewById(R.id.mtp_card_recycler_view)
        mtp_recyclerView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        mtp_recyclerView!!.layoutManager = layoutManager
        et_mtp_date!!.setOnClickListener(this)
        et_mtp_date!!.showSoftInputOnFocus=false


        val currentDate = Date()
        today= SimpleDateFormat("yyyy-MM-dd").format(currentDate)
        et_mtp_date!!.setText(today)
        getMyComplaints()

    }

    fun getMyComplaints(){

        //mtp_progress!!.visibility = View.VISIBLE
        mtp_card_shimmer_recycler_view!!.showShimmerAdapter()
        mtp_error_card!!.visibility = View.GONE
        mtp_recyclerView!!.visibility = View.GONE

        val queryDate:String=et_mtp_date!!.text.toString()
        if(isLaunch)
            et_mtp_date!!.setText("")
        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val complaint = Complaint()
        complaint.setDate(queryDate)
        complaint.setTid(pref!!.getInt(Constants.ID,0))
        val request = ServerRequest()
        request.setOperation(Constants.FETCH_MY_MTP_OPERATION)
        request.setComplaint(complaint)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))

        isLaunch=false

    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            mtp_data=resp.getComplaints()
            mtp_adapter = MyComplaintAdapter(mtp_data!!)
            //mtp_progress!!.visibility = View.GONE
            mtp_card_shimmer_recycler_view!!.hideShimmerAdapter()
            mtp_recyclerView!!.visibility = View.VISIBLE
            mtp_recyclerView!!.adapter = mtp_adapter

        } else{

            //mtp_progress!!.visibility = View.GONE
            mtp_card_shimmer_recycler_view!!.hideShimmerAdapter()
            tv_mtp_error_msg!!.text=resp.getMessage()
            mtp_error_card!!.visibility = View.VISIBLE
            //longSnackbar(view!!, resp.getMessage())


        }


    }

    private fun handleError(error: Throwable) {

        //mtp_progress!!.visibility = View.GONE
        mtp_card_shimmer_recycler_view!!.hideShimmerAdapter()
        tv_mtp_error_msg!!.text=error.localizedMessage
        mtp_error_card!!.visibility = View.VISIBLE
        Log.d(Constants.TAG, "failed")
        //longSnackbar(view!!, t.localizedMessage)




    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.et_mtp_date ->{

                val date:Calendar = Calendar.getInstance()
                val datePicker:DatePickerDialog = DatePickerDialog.newInstance (this, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH) )
                datePicker.isThemeDark = false
                datePicker.dismissOnPause(true)
                datePicker.setTitle("Pick a Date")
                datePicker.setVersion(DatePickerDialog.Version.VERSION_2)
                datePicker.show(fragmentManager, "Datepickerdialog")


            }
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val month:String
        val day:String
        if(monthOfYear+1<=9){
            month="0"+(monthOfYear+1).toString()
        } else {
            month=(monthOfYear+1).toString()
        }
        if(dayOfMonth<=9){
            day="0"+dayOfMonth.toString()
        } else{
            day=dayOfMonth.toString()
        }
        val newDate:String=year.toString()+"-"+month+"-"+day
        if(newDate.replace("-", "").toInt() <= today!!.replace("-", "").toInt()) {
            et_mtp_date!!.setText(newDate)
            getMyComplaints()
        } else {
            longSnackbar(getView(), "Wrong date , can't predict future !")
        }
    }

    override fun onResume() {
        super.onResume()

        val datePicker:DatePickerDialog? = fragmentManager.findFragmentByTag("Datepickerdialog") as DatePickerDialog?
        if(datePicker!=null)
            datePicker.onDateSetListener = this
    }

    companion object {

        val TITLE = "MESSAGE HISTORY"

        fun newInstance(): ComplaintHistoryFragment {

            return ComplaintHistoryFragment()
        }
    }
}