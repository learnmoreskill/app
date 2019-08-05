package com.hacksterkrishna.a1teachers.fragment

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import org.jetbrains.anko.design.longSnackbar
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.crashlytics.android.Crashlytics
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.hacksterkrishna.a1teachers.Constants
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.RequestInterface
import com.hacksterkrishna.a1teachers.Utils
import com.hacksterkrishna.a1teachers.adapter.HomeworkDataAdapter
import com.hacksterkrishna.a1teachers.models.Homework
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
class HomeworkHistoryFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private var et_hw_date : EditText? = null
    private var spinner_hw_class : Spinner? = null
    private var spinner_hw_sec : Spinner? = null
    private var bt_hw : AppCompatButton? = null
    private var tv_error_msg : TextView? = null
    private var error_card : CardView? = null
    private var recyclerView: RecyclerView? = null
    private var data: ArrayList<Homework>? = null
    private var adapter: HomeworkDataAdapter? = null
    private var progress: ProgressBar? = null
    private var fetchType:Int?=null
    private var hwSec:String ?= null
    private var hwStandard:String ?= null
    private var today:String ?= null
    private var launch:Boolean = true
    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    private var utils= Utils()
    private var mCompositeDisposable: CompositeDisposable? = null

    var standard = arrayOf( "Select Class","Nursery","LKG","UKG","1","2","3","4","5","6","7","8","9","10","11","12")
    var section = arrayOf("Select Section","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")


    companion object {


        val TITLE: String = "HISTORY"

        fun newInstance(): HomeworkHistoryFragment {

            return HomeworkHistoryFragment()
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Fabric.with(activity, Crashlytics())
        val view = inflater!!.inflate(R.layout.fragment_homework_history, container, false)
        activity.title = "Homeworks"
        mCompositeDisposable = CompositeDisposable()
        pref=activity.getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        initViews(view)
        return view
    }

    private fun initViews(view: View) {

        et_hw_date = view.findViewById(R.id.et_hw_date)
        et_hw_date!!.showSoftInputOnFocus=false
        spinner_hw_class = view.findViewById(R.id.spinner_hw_class)
        spinner_hw_sec = view.findViewById(R.id.spinner_hw_sec)
        bt_hw = view.findViewById(R.id.bt_hw)
        progress = view.findViewById(R.id.hw_progress)
        error_card = view.findViewById(R.id.hw_error_card)
        tv_error_msg = view.findViewById(R.id.tv_hw_error_msg)
        recyclerView = view.findViewById(R.id.hw_card_recycler_view)
        recyclerView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView!!.layoutManager = layoutManager
        et_hw_date!!.setOnClickListener(this)
        bt_hw!!.setOnClickListener(this)
        fetchType= Constants.FETCH_ALL

        val homeworkClassAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, standard)
        spinner_hw_class!!.adapter = homeworkClassAdapter

        spinner_hw_class!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_hw_class!!.selectedItemPosition

                hwStandard = standard[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }



        val homeworkSectionAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, section)
        spinner_hw_sec!!.adapter = homeworkSectionAdapter

        spinner_hw_sec!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_hw_sec!!.selectedItemPosition

                hwSec = section[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }


        val currentDate = Date()
        today= SimpleDateFormat("yyyy-MM-dd").format(currentDate)
        et_hw_date!!.setText(today)
        getHomeworks()

    }

    fun getHomeworks(){

        progress!!.visibility = View.VISIBLE
        error_card!!.visibility = View.GONE
        recyclerView!!.visibility = View.GONE

        val queryDate:String=et_hw_date!!.text.toString()
        if(launch) {
            et_hw_date!!.setText("")
        }
        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val homework = Homework()
        homework.htid=pref!!.getInt(Constants.ID,0)
        if (queryDate.isEmpty()){
            homework.hdate="-"
            fetchType= Constants.FETCH_ALL
        } else {
            homework.hdate=queryDate
            fetchType= Constants.FETCH_SPECIFIED
        }
        if ((hwStandard==null && hwSec==null) || (hwStandard.equals(standard[0]) && hwSec.equals(section[0]))){
            homework.hclass="0"
            homework.hsec="0"
        } else {
            homework.hclass=utils.getStandardValue(hwStandard!!)
            homework.hsec=hwSec!!
        }
        val request = ServerRequest()
        request.setOperation(Constants.FETCH_HW_OPERATION)
        request.setHomework(homework)
        request.setFetchType(fetchType!!)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))

        launch=false

    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            data=resp.getHomework()
            adapter = HomeworkDataAdapter(data!!)
            progress!!.visibility = View.GONE
            recyclerView!!.visibility = View.VISIBLE
            recyclerView!!.adapter = adapter

        } else{

            progress!!.visibility = View.GONE
            tv_error_msg!!.text=resp.getMessage()
            error_card!!.visibility = View.VISIBLE
            //longSnackbar(view!!, resp.getMessage())


        }


    }

    private fun handleError(error: Throwable) {

        progress!!.visibility = View.GONE
        tv_error_msg!!.text=error.localizedMessage
        error_card!!.visibility = View.VISIBLE
        Log.d(Constants.TAG, "failed")
        //longSnackbar(view!!, t.localizedMessage)

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }


    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.et_hw_date ->{
                val date:Calendar = Calendar.getInstance()
                val datePicker:DatePickerDialog = DatePickerDialog.newInstance (this, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH) )
                datePicker.isThemeDark = false
                datePicker.dismissOnPause(true)
                datePicker.setTitle("Pick a Date")
                datePicker.setVersion(DatePickerDialog.Version.VERSION_2)
                datePicker.show(fragmentManager, "Datepickerdialog")


            }

            R.id.bt_hw -> {
                if(et_hw_date!!.text.isEmpty() && (!hwStandard.equals(standard[0]) && !hwSec.equals(section[0]))) {

                    getHomeworks()
                }else if(!et_hw_date!!.text.isEmpty() && (hwStandard.equals(standard[0]) && hwSec.equals(section[0]))) {
                    if (et_hw_date!!.text.toString().replace("-", "").toInt() <= today!!.replace("-", "").toInt()) {
                        getHomeworks()
                    } else {
                        longSnackbar(view!!, "Wrong date , can't predict future !")
                    }
                } else if(!et_hw_date!!.text.isEmpty() && (!hwStandard.equals(standard[0]) && !hwSec.equals(section[0]))){
                    if (et_hw_date!!.text.toString().replace("-", "").toInt() <= today!!.replace("-", "").toInt()) {
                        getHomeworks()
                    } else {
                        longSnackbar(view!!, "Wrong date , can't predict future !")
                    }
                } else {
                    longSnackbar(view!!, "Either Select Date or Select class and section !")
                }


            }
        }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        if(fetchType!= Constants.FETCH_SPECIFIED){
            fetchType= Constants.FETCH_SPECIFIED
        }

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
        et_hw_date!!.setText(newDate)
    }

    override fun onResume() {
        super.onResume()

        val datePicker: DatePickerDialog? = fragmentManager.findFragmentByTag("Datepickerdialog") as DatePickerDialog?
        if(datePicker!=null)
            datePicker.onDateSetListener = this
    }

}