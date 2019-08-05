package com.hacksterkrishna.a1principal.fragment

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.activity.AttendanceType1View
import com.hacksterkrishna.a1principal.activity.AttendanceType2View
import com.hacksterkrishna.a1principal.activity.AttendanceType3View
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by krishna on 31/12/17.
 */
class AttendanceFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener{


    private var spinner_attendance_type : Spinner ?= null
    private var attendance_type_1 : CardView ?= null
    private var et_attendance_type_1_date : EditText ?= null
    private var spinner_attendance_type_1_class : Spinner ?= null
    private var spinner_attendance_type_1_sec : Spinner ?= null
    private var bt_attendance_type1 : AppCompatButton ?= null
    private var attendance_type_2 : CardView ?= null
    private var spinner_attendance_type_2_class : Spinner ?= null
    private var spinner_attendance_type_2_sec : Spinner ?= null
    private var bt_attendance_type2 : AppCompatButton ?= null
    private var attendance_type_3 : CardView ?= null
    private var et_attendance_type_3_date : EditText ?= null
    private var bt_attendance_type3 : AppCompatButton ?= null
    private var typeSelected:Int ?= null
    private var type1Sec:String ?= null
    private var type1Standard:String ?= null
    private var type2Sec:String ?= null
    private var type2Standard:String ?= null
    private var today:String ?= null

    var attendationType = arrayOf("Select an option", "View Attendance", "View Attendance by class", "View Attendance by date")
    var standard = arrayOf( "Select Class","Pre-Nursery","Nursery","LKG","UKG","1","2","3","4","5","6","7","8","9","10","11","12")
    var section = arrayOf("Select Section","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.fragment_attendance, container, false)
        activity.title = "Attendance"
        initViews(view)
        return view
    }

    private fun initViews (view: View) {

        spinner_attendance_type=view.findViewById(R.id.spinner_attendance_type)
        attendance_type_1=view.findViewById(R.id.attendance_type_1)
        et_attendance_type_1_date=view.findViewById(R.id.et_attendance_type_1_date)
        et_attendance_type_1_date!!.setOnClickListener(this)
        et_attendance_type_1_date!!.showSoftInputOnFocus=false
        spinner_attendance_type_1_class=view.findViewById(R.id.spinner_attendance_type1_class)
        spinner_attendance_type_1_sec=view.findViewById(R.id.spinner_attendance_type1_sec)
        bt_attendance_type1=view.findViewById(R.id.bt_attendance_type1)
        bt_attendance_type1!!.setOnClickListener(this)
        attendance_type_2=view.findViewById(R.id.attendance_type_2)
        spinner_attendance_type_2_class=view.findViewById(R.id.spinner_attendance_type2_class)
        spinner_attendance_type_2_sec=view.findViewById(R.id.spinner_attendance_type2_sec)
        bt_attendance_type2=view.findViewById(R.id.bt_attendance_type2)
        bt_attendance_type2!!.setOnClickListener(this)
        attendance_type_3=view.findViewById(R.id.attendance_type_3)
        et_attendance_type_3_date=view.findViewById(R.id.et_attendance_type_3_date)
        et_attendance_type_3_date!!.setOnClickListener(this)
        et_attendance_type_3_date!!.showSoftInputOnFocus=false
        bt_attendance_type3=view.findViewById(R.id.bt_attendance_type3)
        bt_attendance_type3!!.setOnClickListener(this)
        var currentDate: Date = Date()
        today = SimpleDateFormat("yyyy-MM-dd").format(currentDate)
        val attendanceTypeAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, attendationType)
        spinner_attendance_type!!.adapter = attendanceTypeAdapter

        spinner_attendance_type!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_attendance_type!!.selectedItemPosition
                typeSelected=position
                when(position) {
                    0 -> {
                        attendance_type_1!!.visibility=View.GONE
                        attendance_type_2!!.visibility=View.GONE
                        attendance_type_3!!.visibility=View.GONE
                    }
                    1 -> {
                        attendance_type_1!!.visibility=View.VISIBLE
                        attendance_type_2!!.visibility=View.GONE
                        attendance_type_3!!.visibility=View.GONE
                    }
                    2 -> {
                        attendance_type_1!!.visibility=View.GONE
                        attendance_type_2!!.visibility=View.VISIBLE
                        attendance_type_3!!.visibility=View.GONE
                    }
                    3 -> {
                        attendance_type_1!!.visibility=View.GONE
                        attendance_type_2!!.visibility=View.GONE
                        attendance_type_3!!.visibility=View.VISIBLE
                    }
                }
                }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }

        val attendanceType1ClassAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, standard)
        spinner_attendance_type_1_class!!.adapter = attendanceType1ClassAdapter

        spinner_attendance_type_1_class!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_attendance_type_1_class!!.selectedItemPosition

                    type1Standard = standard[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }



        val attendanceType1SectionAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, section)
        spinner_attendance_type_1_sec!!.adapter = attendanceType1SectionAdapter

        spinner_attendance_type_1_sec!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_attendance_type_1_sec!!.selectedItemPosition

                    type1Sec = section[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }

        val attendanceType2ClassAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, standard)
        spinner_attendance_type_2_class!!.adapter = attendanceType2ClassAdapter

        spinner_attendance_type_2_class!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_attendance_type_2_class!!.selectedItemPosition

                type2Standard = standard[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }



        val attendanceType2SectionAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, section)
        spinner_attendance_type_2_sec!!.adapter = attendanceType2SectionAdapter

        spinner_attendance_type_2_sec!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_attendance_type_2_sec!!.selectedItemPosition

                type2Sec = section[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }

    }

    override fun onClick(v: View?) {

        when(v!!.id){

            R.id.et_attendance_type_1_date,R.id.et_attendance_type_3_date ->{

                var date: Calendar = Calendar.getInstance()
                var datePicker:DatePickerDialog = DatePickerDialog.newInstance (this, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH) )
                datePicker.isThemeDark = false
                datePicker.dismissOnPause(true)
                datePicker.setVersion(DatePickerDialog.Version.VERSION_2)
                datePicker.show(fragmentManager, "Datepickerdialog")


            }

            R.id.bt_attendance_type1 -> {
                if(!et_attendance_type_1_date!!.text.isEmpty() && (type1Standard!=null && !type1Standard.equals(standard[0]))  &&  (type1Sec!=null && !type1Sec.equals(section[0]))){

                    if(et_attendance_type_1_date!!.text.toString().replace("-","").toInt()<=today!!.replace("-","").toInt()) {

                        val attendanceType1View = Intent(activity, AttendanceType1View::class.java)
                        attendanceType1View.putExtra("date", et_attendance_type_1_date!!.text.toString())
                        attendanceType1View.putExtra("standard", type1Standard)
                        attendanceType1View.putExtra("sec", type1Sec)
                        startActivity(attendanceType1View)
                    } else {
                        Snackbar.make(view!!, "Wrong date , can't predict future !", Snackbar.LENGTH_LONG).show()
                    }

                } else {
                    Snackbar.make(view!!, "Fields are empty !", Snackbar.LENGTH_LONG).show()
                }

            }

            R.id.bt_attendance_type2 -> {
                if((type2Standard!=null && !type2Standard.equals(standard[0]))  &&  (type2Sec!=null && !type2Sec.equals(section[0]))){

                        val attendanceType2View = Intent(activity, AttendanceType2View::class.java)
                        attendanceType2View.putExtra("standard", type2Standard)
                        attendanceType2View.putExtra("sec", type2Sec)
                        startActivity(attendanceType2View)

                } else {
                    Snackbar.make(view!!, "Fields are empty !", Snackbar.LENGTH_LONG).show()
                }

            }

            R.id.bt_attendance_type3 -> {
                if(!et_attendance_type_3_date!!.text.isEmpty()){

                    if(et_attendance_type_3_date!!.text.toString().replace("-","").toInt()<=today!!.replace("-","").toInt()) {

                        val attendanceType3View = Intent(activity, AttendanceType3View::class.java)
                        attendanceType3View.putExtra("date", et_attendance_type_3_date!!.text.toString())
                        startActivity(attendanceType3View)
                    } else {
                        Snackbar.make(view!!, "Wrong date , can't predict future !", Snackbar.LENGTH_LONG).show()
                    }

                } else {
                    Snackbar.make(view!!, "Field is empty !", Snackbar.LENGTH_LONG).show()
                }

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
        if(typeSelected!=0 && typeSelected!=null) {
            when(typeSelected) {
                1 -> {
                    et_attendance_type_1_date!!.setText(newDate)
                }
                3-> {
                    et_attendance_type_3_date!!.setText(newDate)

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        var datePicker:DatePickerDialog? = fragmentManager.findFragmentByTag("Datepickerdialog") as DatePickerDialog?
        if(datePicker!=null)
            datePicker.setOnDateSetListener(this)
    }
}