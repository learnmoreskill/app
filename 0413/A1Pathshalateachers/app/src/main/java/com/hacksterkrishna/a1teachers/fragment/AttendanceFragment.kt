package com.hacksterkrishna.a1teachers.fragment

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import org.jetbrains.anko.design.longSnackbar
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.crashlytics.android.Crashlytics
import com.facebook.shimmer.ShimmerFrameLayout
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.hacksterkrishna.a1teachers.Constants
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.RequestInterface
import com.hacksterkrishna.a1teachers.activity.AttendanceActivity
import com.hacksterkrishna.a1teachers.activity.AttendanceEditActivity
import com.hacksterkrishna.a1teachers.activity.AttendanceViewActivity
import com.hacksterkrishna.a1teachers.models.AttendanceCheck
import com.hacksterkrishna.a1teachers.models.ServerRequest
import com.hacksterkrishna.a1teachers.models.ServerResponse
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.startActivity
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by krishna on 31/12/17.
 */
class AttendanceFragment: Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener{

    private var attendance_check_progress:ProgressBar? = null
    private var attendance_start_card:CardView? = null
    private var attendance_start_placeholder_card:CardView? =null
    private var tv_attendance_info:TextView? = null
    private var btn_attendance_start:TextView? = null
    private var btn_attendance_view:TextView? = null
    private var attendance_start_shimmer:ShimmerFrameLayout? = null
    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    private var attendance_view_card:CardView? = null
    private var et_attendance_view_date : EditText?= null
    private var spinner_attendance_view_class : Spinner ?= null
    private var spinner_attendance_view_sec : Spinner?= null
    private var bt_attendance_view : AppCompatButton?= null
    private var selectedSec:String? = null
    private var selectedStandard:String? = null
    private var today:String? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    var standards = arrayOf( "Select Class","Nursery","LKG","UKG","1","2","3","4","5","6","7","8","9","10","11","12")
    var sections = arrayOf("Select Section","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Fabric.with(activity, Crashlytics())
        val view = inflater!!.inflate(R.layout.fragment_attendance, container, false)
        activity.title = "Attendance"
        mCompositeDisposable = CompositeDisposable()
        pref=activity.getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        initViews(view)
        return view
    }

    private fun initViews(view: View){
        attendance_check_progress=view.findViewById(R.id.attendance_check_progress)
        attendance_start_card=view.findViewById(R.id.attendance_start_card)
        attendance_start_placeholder_card=view.findViewById(R.id.attendance_start_placeholder_card)
        tv_attendance_info=view.findViewById(R.id.tv_attendace_info)
        btn_attendance_start=view.findViewById(R.id.btn_attendance_start)
        btn_attendance_start!!.setOnClickListener {
            startActivity<AttendanceActivity>()
        }
        btn_attendance_view=view.findViewById(R.id.btn_attendance_view)
        btn_attendance_view!!.setOnClickListener {
            startActivity<AttendanceEditActivity>()

        }
        attendance_start_shimmer=view.findViewById(R.id.attendance_start_shimmer_view_container)
        attendance_view_card=view.findViewById(R.id.attendance_view_card)
        et_attendance_view_date=view.findViewById(R.id.et_attendance_view_date)
        et_attendance_view_date!!.setOnClickListener(this)
        et_attendance_view_date!!.showSoftInputOnFocus=false
        spinner_attendance_view_class=view.findViewById(R.id.spinner_attendance_view_class)
        spinner_attendance_view_sec=view.findViewById(R.id.spinner_attendance_view_sec)
        bt_attendance_view=view.findViewById(R.id.bt_attendance_view)
        bt_attendance_view!!.setOnClickListener(this)
        val currentDate = Date()
        today = SimpleDateFormat("yyyy-MM-dd").format(currentDate)

        val attendanceViewClassAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, standards)
        spinner_attendance_view_class!!.adapter = attendanceViewClassAdapter

        spinner_attendance_view_class!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_attendance_view_class!!.selectedItemPosition

                selectedStandard = standards[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }



        val attendanceViewSectionAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, sections)
        spinner_attendance_view_sec!!.adapter = attendanceViewSectionAdapter

        spinner_attendance_view_sec!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_attendance_view_sec!!.selectedItemPosition

                selectedSec = sections[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }
        createLayout()
    }

    override fun onResume() {
        super.onResume()

        val datePicker:DatePickerDialog? = fragmentManager.findFragmentByTag("Datepickerdialog") as DatePickerDialog?
        if(datePicker!=null)
            datePicker.onDateSetListener = this

        createLayout()
    }

    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.et_attendance_view_date ->{

                val date: Calendar = Calendar.getInstance()
                val datePicker:DatePickerDialog = DatePickerDialog.newInstance (this, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH) )
                datePicker.isThemeDark = false
                datePicker.dismissOnPause(true)
                datePicker.setVersion(DatePickerDialog.Version.VERSION_2)
                datePicker.show(fragmentManager, "Datepickerdialog")


            }

            R.id.bt_attendance_view -> {
                if(!et_attendance_view_date!!.text.isEmpty() && (selectedStandard!=null && !selectedStandard.equals(standards[0]))  &&  (selectedSec!=null && !selectedSec.equals(sections[0]))){

                    if(et_attendance_view_date!!.text.toString().replace("-","").toInt()<=today!!.replace("-","").toInt()) {
                        startActivity<AttendanceViewActivity>("date" to et_attendance_view_date!!.text.toString(),"standard" to selectedStandard,"sec" to selectedSec)
                    } else {
                        longSnackbar(view!!, "Wrong date , can't predict future !")
                    }

                } else {
                    longSnackbar(view!!, "Fields are empty !")
                }

            }

        }

    }

    private fun createLayout(){
       // attendance_check_progress!!.visibility=View.VISIBLE
        attendance_start_card!!.visibility=View.GONE
        attendance_view_card!!.visibility=View.GONE
        attendance_start_placeholder_card!!.visibility=View.VISIBLE
        attendance_start_shimmer!!.startShimmerAnimation()
        //attendance_start_card!!.visibility=View.GONE

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val attendanceCheck = AttendanceCheck()
        attendanceCheck.setClass(pref!!.getString(Constants.STANDARD,"Class"))
        attendanceCheck.setSec(pref!!.getString(Constants.SEC,"Sec"))
        val request = ServerRequest()
        request.setOperation(Constants.CHECK_ATTENDANCE_DONE)
        request.setAttendanceCheck(attendanceCheck)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.YES) {

            btn_attendance_view!!.visibility = View.VISIBLE
            btn_attendance_start!!.visibility = View.GONE
            tv_attendance_info!!.text = getString(R.string.attendance_taken)
            //attendance_check_progress!!.visibility=View.GONE
            attendance_start_placeholder_card!!.visibility = View.GONE
            attendance_start_shimmer!!.stopShimmerAnimation()
            attendance_start_card!!.visibility = View.VISIBLE
            attendance_view_card!!.visibility = View.VISIBLE


        } else if (resp.getResult() == Constants.NO) {

            btn_attendance_view!!.visibility = View.GONE
            btn_attendance_start!!.visibility = View.VISIBLE
            tv_attendance_info!!.text = getString(R.string.attendance_take)
            //attendance_check_progress!!.visibility=View.GONE
            attendance_start_placeholder_card!!.visibility = View.GONE
            attendance_start_shimmer!!.stopShimmerAnimation()
            attendance_start_card!!.visibility = View.VISIBLE
            attendance_view_card!!.visibility = View.VISIBLE
            //longSnackbar(view!!, resp.getMessage())


        } else {
            //attendance_check_progress!!.visibility=View.GONE
            attendance_start_placeholder_card!!.visibility = View.GONE
            attendance_start_shimmer!!.stopShimmerAnimation()
            attendance_start_card!!.visibility = View.GONE
            attendance_view_card!!.visibility = View.GONE
            longSnackbar(view!!, resp.getMessage())
        }
    }

    private fun handleError(error: Throwable) {

        //attendance_check_progress!!.visibility=View.GONE
        attendance_start_placeholder_card!!.visibility = View.GONE
        attendance_start_shimmer!!.stopShimmerAnimation()
        attendance_start_card!!.visibility = View.GONE
        attendance_view_card!!.visibility = View.GONE
        Log.d(Constants.TAG, "failed")
        longSnackbar(view!!, error.localizedMessage)

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
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
        et_attendance_view_date!!.setText(newDate)
    }

}