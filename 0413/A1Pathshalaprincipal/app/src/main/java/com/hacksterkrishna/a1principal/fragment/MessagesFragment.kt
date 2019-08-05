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
import com.hacksterkrishna.a1principal.adapter.MessageDataAdapter
import com.hacksterkrishna.a1principal.models.Message
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

class MessagesFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private var et_msg_date : EditText? = null
    private var spinner_msg_class : Spinner? = null
    private var spinner_msg_sec : Spinner? = null
    private var bt_msg : AppCompatButton? = null
    private var tv_msg_error_msg : TextView? = null
    private var msg_error_card : CardView? = null
    private var msg_recyclerView: RecyclerView? = null
    private var msg_data: ArrayList<Message>? = null
    private var msg_adapter: MessageDataAdapter? = null
    private var msg_progress: ProgressBar? = null
    private var fetchType:Int?=null
    private var msgSec:String ?= null
    private var msgStandard:String ?= null
    private var today:String ?= null
    private var launch:Boolean = true
    private var mCompositeDisposable: CompositeDisposable? = null

    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    var standard = arrayOf( "Select Class","Pre-Nursery","Nursery","LKG","UKG","1","2","3","4","5","6","7","8","9","10","11","12")
    var section = arrayOf("Select Section","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_message, container, false)
        activity.title = "Messages to Parents"

        pref=activity.getSharedPreferences("princiPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,null)

        mCompositeDisposable = CompositeDisposable()
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        et_msg_date = view.findViewById(R.id.et_msg_date)
        spinner_msg_class = view.findViewById(R.id.spinner_msg_class)
        spinner_msg_sec = view.findViewById(R.id.spinner_msg_sec)
        bt_msg = view.findViewById(R.id.bt_msg)
        msg_progress = view.findViewById(R.id.msg_progress)
        msg_error_card = view.findViewById(R.id.msg_error_card)
        tv_msg_error_msg = view.findViewById(R.id.tv_msg_error_msg)
        msg_recyclerView = view.findViewById(R.id.msg_card_recycler_view)
        msg_recyclerView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        msg_recyclerView!!.layoutManager = layoutManager
        et_msg_date!!.setOnClickListener(this)
        et_msg_date!!.showSoftInputOnFocus=false
        bt_msg!!.setOnClickListener(this)
        fetchType= Constants.FETCH_ALL

        val messagesClassAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, standard)
        spinner_msg_class!!.adapter = messagesClassAdapter

        spinner_msg_class!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_msg_class!!.selectedItemPosition

                msgStandard = standard[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }



        val messagesSectionAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, section)
        spinner_msg_sec!!.adapter = messagesSectionAdapter

        spinner_msg_sec!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_msg_sec!!.selectedItemPosition

                msgSec = section[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }


        var currentDate: Date = Date()
        today= SimpleDateFormat("yyyy-MM-dd").format(currentDate)
        et_msg_date!!.setText(today)
        getMessages()
    }

    fun getMessages(){

        msg_progress!!.visibility = View.VISIBLE
        msg_error_card!!.visibility = View.GONE
        msg_recyclerView!!.visibility = View.GONE

        var queryDate:String=et_msg_date!!.text.toString()
        if(launch) {
            et_msg_date!!.setText("")
        }
        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val message = Message()
        if (queryDate.isEmpty()){
            message.setDate("-")
            fetchType= Constants.FETCH_ALL
        } else {
            message.setDate(queryDate)
            fetchType= Constants.FETCH_SPECIFIED
        }
        if ((msgStandard==null && msgSec==null) || (msgStandard.equals(standard[0]) && msgSec.equals(section[0]))){
            message.setClass("0")
            message.setSec("0")
        } else {
            message.setClass(msgStandard!!)
            message.setSec(msgSec!!)
        }
        val request = ServerRequest()
        request.setOperation(Constants.FETCH_MSG_OPERATION)
        request.setMessage(message)
        request.setFetchType(fetchType!!)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))

        launch=false

    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            msg_data=resp.getMsg()
            msg_adapter = MessageDataAdapter(msg_data!!)
            msg_progress!!.visibility = View.GONE
            msg_recyclerView!!.visibility = View.VISIBLE
            msg_recyclerView!!.adapter = msg_adapter

        } else{

            msg_progress!!.visibility = View.GONE
            tv_msg_error_msg!!.text=resp.getMessage()
            msg_error_card!!.visibility = View.VISIBLE
            //Snackbar.make(view!!, resp.getMessage(), Snackbar.LENGTH_LONG).show()


        }

    }

    private fun handleError(error: Throwable) {

        msg_progress!!.visibility = View.GONE
        tv_msg_error_msg!!.text=error.localizedMessage
        msg_error_card!!.visibility = View.VISIBLE
        Log.d(Constants.TAG, "failed")
        //Snackbar.make(view!!, t.localizedMessage, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }


    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.et_msg_date ->{

                var date: Calendar = Calendar.getInstance()
                var datePicker: DatePickerDialog = DatePickerDialog.newInstance (this, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH) )
                datePicker.isThemeDark = false
                datePicker.dismissOnPause(true)
                datePicker.setTitle("Pick a Date")
                datePicker.setVersion(DatePickerDialog.Version.VERSION_2)
                datePicker.show(fragmentManager, "Datepickerdialog")


            }

            R.id.bt_msg -> {
                if(et_msg_date!!.text.isEmpty() && (!msgStandard.equals(standard[0]) && !msgSec.equals(section[0]))) {

                    getMessages()
                }else if(!et_msg_date!!.text.isEmpty() && (msgStandard.equals(standard[0]) && msgSec.equals(section[0]))) {
                    if (et_msg_date!!.text.toString().replace("-", "").toInt() <= today!!.replace("-", "").toInt()) {
                        getMessages()
                    } else {
                        Snackbar.make(view!!, "Wrong date , can't predict future !", Snackbar.LENGTH_LONG).show()
                    }
                } else if(!et_msg_date!!.text.isEmpty() && (!msgStandard.equals(standard[0]) && !msgSec.equals(section[0]))){
                    if (et_msg_date!!.text.toString().replace("-", "").toInt() <= today!!.replace("-", "").toInt()) {
                        getMessages()
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
        et_msg_date!!.setText(newDate)
    }

    override fun onResume() {
        super.onResume()

        var datePicker: DatePickerDialog? = fragmentManager.findFragmentByTag("Datepickerdialog") as DatePickerDialog?
        if(datePicker!=null)
            datePicker.setOnDateSetListener(this)
    }

}