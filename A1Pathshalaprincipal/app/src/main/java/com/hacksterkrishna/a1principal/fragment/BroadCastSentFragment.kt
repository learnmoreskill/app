package com.hacksterkrishna.a1principal.fragment

/**
 * Created by krishna on 31/12/17.
 */
import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.hacksterkrishna.a1principal.Constants
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.RequestInterface
import com.hacksterkrishna.a1principal.adapter.MyBroadcastAdapter
import com.hacksterkrishna.a1principal.models.BroadcastMessage
import com.hacksterkrishna.a1principal.models.ServerRequest
import com.hacksterkrishna.a1principal.models.ServerResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class BroadCastSentFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private var et_brd_date : EditText? = null
    private var tv_brd_error_msg : TextView? = null
    private var brd_error_card : CardView? = null
    private var brd_recyclerView: RecyclerView? = null
    private var brd_data: ArrayList<BroadcastMessage>? = null
    private var brd_adapter: MyBroadcastAdapter? = null
    private var brd_progress: ProgressBar? = null
    private var isLaunch: Boolean = true
    private var today:String? = null

    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    private var mCompositeDisposable: CompositeDisposable? = null

    companion object {


        val TITLE: String = "HISTORY"

        fun newInstance(): BroadCastSentFragment {

            return BroadCastSentFragment()
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_broadcast_sent, container, false)
        activity.title = "Broadcast"

        pref=activity.getSharedPreferences("princiPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        mCompositeDisposable = CompositeDisposable()
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        et_brd_date = view.findViewById(R.id.et_brd_date)
        brd_progress = view.findViewById(R.id.brd_progress)
        brd_error_card = view.findViewById(R.id.brd_error_card)
        tv_brd_error_msg = view.findViewById(R.id.tv_brd_error_msg)
        brd_recyclerView = view.findViewById(R.id.brd_card_recycler_view)
        brd_recyclerView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        brd_recyclerView!!.layoutManager = layoutManager
        et_brd_date!!.setOnClickListener(this)
        et_brd_date!!.showSoftInputOnFocus=false


        var currentDate: Date = Date()
        today= SimpleDateFormat("yyyy-MM-dd").format(currentDate)
        et_brd_date!!.setText(today)
        getMyBroadcasts()
    }

    fun getMyBroadcasts(){

        brd_progress!!.visibility = View.VISIBLE
        brd_error_card!!.visibility = View.GONE
        brd_recyclerView!!.visibility = View.GONE

        var queryDate:String=et_brd_date!!.text.toString()
        if(isLaunch)
            et_brd_date!!.setText("")
        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val broadcastMessage = BroadcastMessage()
        broadcastMessage.setDate(queryDate)
        val request = ServerRequest()
        request.setOperation(Constants.FETCH_MY_BRD_OPERATION)
        request.setBroadcastMessage(broadcastMessage)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))


        isLaunch=false

    }


    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            brd_data=resp.getBroadcastMessage()
            brd_adapter = MyBroadcastAdapter(brd_data!!)
            brd_progress!!.visibility = View.GONE
            brd_recyclerView!!.visibility = View.VISIBLE
            brd_recyclerView!!.adapter = brd_adapter

        } else{

            brd_progress!!.visibility = View.GONE
            tv_brd_error_msg!!.text=resp.getMessage()
            brd_error_card!!.visibility = View.VISIBLE
            //Snackbar.make(view!!, resp.getMessage(), Snackbar.LENGTH_LONG).show()


        }

    }

    private fun handleError(error: Throwable) {

        brd_progress!!.visibility = View.GONE
        tv_brd_error_msg!!.text=error.localizedMessage
        brd_error_card!!.visibility = View.VISIBLE
        Log.d(Constants.TAG, "failed")
        //Snackbar.make(view!!, t.localizedMessage, Snackbar.LENGTH_LONG).show()

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.et_brd_date ->{

                var date:Calendar = Calendar.getInstance()
                var datePicker:DatePickerDialog = DatePickerDialog.newInstance (this, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH) )
                datePicker.isThemeDark = false
                datePicker.dismissOnPause(true)
                datePicker.setTitle("Pick a Date")
                datePicker.setVersion(DatePickerDialog.Version.VERSION_2)
                datePicker.show(fragmentManager, "Datepickerdialog")


            }
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var month:String
        var day:String
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
        var newDate:String=year.toString()+"-"+month+"-"+day
        if(newDate.replace("-", "").toInt() <= today!!.replace("-", "").toInt()) {
            et_brd_date!!.setText(newDate)
            getMyBroadcasts()
        } else {
            Snackbar.make(getView(), "Wrong date , can't predict future !", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()

        var datePicker:DatePickerDialog? = fragmentManager.findFragmentByTag("Datepickerdialog") as DatePickerDialog?
        if(datePicker!=null)
            datePicker.setOnDateSetListener(this)
    }

}