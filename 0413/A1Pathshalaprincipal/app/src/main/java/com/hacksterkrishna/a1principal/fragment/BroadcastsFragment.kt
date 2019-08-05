package com.hacksterkrishna.a1principal.fragment

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.hacksterkrishna.a1principal.Constants
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.RequestInterface
import com.hacksterkrishna.a1principal.adapter.BroadcastDataAdapter
import com.hacksterkrishna.a1principal.models.Broadcast
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

/**
 * Created by krishna on 31/12/17.
 */
class BroadcastsFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private var et_bm_date : EditText? = null
    private var spinner_bm_class : Spinner? = null
    private var spinner_bm_sec : Spinner? = null
    private var bt_bm : AppCompatButton? = null
    private var tv_bm_error_msg : TextView? = null
    private var bm_error_card :CardView? = null
    private var bm_recyclerView: RecyclerView? = null
    private var bm_data: ArrayList<Broadcast>? = null
    private var bm_adapter: BroadcastDataAdapter? = null
    private var bm_progress: ProgressBar? = null
    private var fetchType:Int?=null
    private var bmSec:String ?= null
    private var bmStandard:String ?= null
    private var today:String ?= null
    private var launch:Boolean = true
    private var mCompositeDisposable: CompositeDisposable? = null

    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    var standard = arrayOf( "Select Class","Pre-Nursery","Nursery","LKG","UKG","1","2","3","4","5","6","7","8","9","10","11","12")
    var section = arrayOf("Select Section","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_broadcasts, container, false)
        activity.title = "Broadcast Messages"

        pref=activity.getSharedPreferences("princiPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        mCompositeDisposable = CompositeDisposable()
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        et_bm_date = view.findViewById(R.id.et_bm_date)
        spinner_bm_class = view.findViewById(R.id.spinner_bm_class)
        spinner_bm_sec = view.findViewById(R.id.spinner_bm_sec)
        bt_bm = view.findViewById(R.id.bt_bm)
        bm_progress = view.findViewById(R.id.bm_progress)
        bm_error_card = view.findViewById(R.id.bm_error_card)
        tv_bm_error_msg = view.findViewById(R.id.tv_bm_error_msg)
        bm_recyclerView = view.findViewById(R.id.bm_card_recycler_view)
        bm_recyclerView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        bm_recyclerView!!.layoutManager = layoutManager
        et_bm_date!!.setOnClickListener(this)
        et_bm_date!!.showSoftInputOnFocus=false
        bt_bm!!.setOnClickListener(this)
        fetchType= Constants.FETCH_ALL

        val broadcastClassAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, standard)
        spinner_bm_class!!.adapter = broadcastClassAdapter

        spinner_bm_class!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_bm_class!!.selectedItemPosition

                bmStandard = standard[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }



        val broadcastSectionAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, section)
        spinner_bm_sec!!.adapter = broadcastSectionAdapter

        spinner_bm_sec!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_bm_sec!!.selectedItemPosition

                bmSec = section[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }


        var currentDate: Date = Date()
        today= SimpleDateFormat("yyyy-MM-dd").format(currentDate)
        et_bm_date!!.setText(today)
        getBroadcasts()
    }

    fun getBroadcasts(){

        bm_progress!!.visibility = View.VISIBLE
        bm_error_card!!.visibility = View.GONE
        bm_recyclerView!!.visibility = View.GONE

        var queryDate:String=et_bm_date!!.text.toString()
        if(launch) {
            et_bm_date!!.setText("")
        }
        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val broadcast = Broadcast()
        if (queryDate.isEmpty()){
            broadcast.setDate("-")
            fetchType= Constants.FETCH_ALL
        } else {
            broadcast.setDate(queryDate)
            fetchType= Constants.FETCH_SPECIFIED
        }
        if ((bmStandard==null && bmSec==null) || (bmStandard.equals(standard[0]) && bmSec.equals(section[0]))){
            broadcast.setClass("0")
            broadcast.setSec("0")
        } else {
            broadcast.setClass(bmStandard!!)
            broadcast.setSec(bmSec!!)
        }
        val request = ServerRequest()
        request.setOperation(Constants.FETCH_BM_OPERATION)
        request.setBroadcast(broadcast)
        request.setFetchType(fetchType!!)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))


        launch=false

    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            bm_data=resp.getBroadcast()
            bm_adapter = BroadcastDataAdapter(bm_data!!)
            bm_progress!!.visibility = View.GONE
            bm_recyclerView!!.visibility = View.VISIBLE
            bm_recyclerView!!.adapter = bm_adapter

        } else{

            bm_progress!!.visibility = View.GONE
            tv_bm_error_msg!!.text=resp.getMessage()
            bm_error_card!!.visibility = View.VISIBLE
            //Snackbar.make(view!!, resp.getMessage(), Snackbar.LENGTH_LONG).show()


        }


    }

    private fun handleError(error: Throwable) {

        bm_progress!!.visibility = View.GONE
        tv_bm_error_msg!!.text=error.localizedMessage
        bm_error_card!!.visibility = View.VISIBLE
        Log.d(Constants.TAG, "failed")
        //Snackbar.make(view!!, t.localizedMessage, Snackbar.LENGTH_LONG).show()


    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }


    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.et_bm_date ->{

                var date:Calendar = Calendar.getInstance()
                var datePicker:DatePickerDialog = DatePickerDialog.newInstance (this, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH) )
                datePicker.isThemeDark = false
                datePicker.dismissOnPause(true)
                datePicker.setTitle("Pick a Date")
                datePicker.setVersion(DatePickerDialog.Version.VERSION_2)
                datePicker.show(fragmentManager, "Datepickerdialog")


            }

            R.id.bt_bm -> {
                if(et_bm_date!!.text.isEmpty() && (!bmStandard.equals(standard[0]) && !bmSec.equals(section[0]))) {

                    getBroadcasts()
                }else if(!et_bm_date!!.text.isEmpty() && (bmStandard.equals(standard[0]) && bmSec.equals(section[0]))) {
                    if (et_bm_date!!.text.toString().replace("-", "").toInt() <= today!!.replace("-", "").toInt()) {
                        getBroadcasts()
                    } else {
                        Snackbar.make(view!!, "Wrong date , can't predict future !", Snackbar.LENGTH_LONG).show()
                    }
                } else if(!et_bm_date!!.text.isEmpty() && (!bmStandard.equals(standard[0]) && !bmSec.equals(section[0]))){
                    if (et_bm_date!!.text.toString().replace("-", "").toInt() <= today!!.replace("-", "").toInt()) {
                        getBroadcasts()
                    } else {
                        Snackbar.make(view!!, "Wrong date , can't predict future !", Snackbar.LENGTH_LONG).show()
                    }
                } else {
                    Snackbar.make(view!!, "Either Select Date or Select class and section !", Snackbar.LENGTH_LONG).show()
                }


            }
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        if(fetchType!= Constants.FETCH_SPECIFIED){
            fetchType= Constants.FETCH_SPECIFIED
        }
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
        et_bm_date!!.setText(newDate)
    }

    override fun onResume() {
        super.onResume()

        var datePicker:DatePickerDialog? = fragmentManager.findFragmentByTag("Datepickerdialog") as DatePickerDialog?
        if(datePicker!=null)
            datePicker.setOnDateSetListener(this)
    }

}