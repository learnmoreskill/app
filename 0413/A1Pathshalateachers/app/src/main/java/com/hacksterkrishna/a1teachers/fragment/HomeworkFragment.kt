package com.hacksterkrishna.a1teachers.fragment

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import com.crashlytics.android.Crashlytics
import com.rengwuxian.materialedittext.MaterialEditText
import com.hacksterkrishna.a1teachers.Constants
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.RequestInterface
import com.hacksterkrishna.a1teachers.Utils
import com.hacksterkrishna.a1teachers.models.Homework
import com.hacksterkrishna.a1teachers.models.ServerRequest
import com.hacksterkrishna.a1teachers.models.ServerResponse
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.design.longSnackbar
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by krishna on 31/12/17.
 */
class HomeworkFragment : Fragment(), View.OnClickListener {

    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    private var et_hw_subject: MaterialEditText? = null
    private var et_hw_topic: MaterialEditText? = null
    private var spinner_hw_class: Spinner? =null
    private var spinner_hw_sec: Spinner? = null
    private var bt_hw_submit: AppCompatButton? = null
    private var hw_submit_progress: ProgressBar? = null
    private var selectedSec:String? = null
    private var selectedStandard:String? = null
    private var utils= Utils()
    private var mCompositeDisposable: CompositeDisposable? = null

    var standards = arrayOf( "Select Class","Nursery","LKG","UKG","1","2","3","4","5","6","7","8","9","10","11","12")
    var sections = arrayOf("Select Section","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Fabric.with(activity, Crashlytics())
        val view = inflater!!.inflate(R.layout.fragment_homework, container, false)

        activity.title = "Homeworks"
        mCompositeDisposable = CompositeDisposable()
        pref=activity.getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        initViews(view)
        return view
    }

    private fun initViews (view: View) {
        et_hw_subject=view.findViewById(R.id.et_hw_subject)
        et_hw_topic=view.findViewById(R.id.et_hw_topic)
        spinner_hw_class=view.findViewById(R.id.spinner_hw_class)
        spinner_hw_sec=view.findViewById(R.id.spinner_hw_sec)
        bt_hw_submit=view.findViewById(R.id.bt_hw_submit)
        bt_hw_submit!!.setOnClickListener(this)
        hw_submit_progress=view.findViewById(R.id.hw_submit_progress)

        val hwClassAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, standards)
        spinner_hw_class!!.adapter = hwClassAdapter

        spinner_hw_class!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_hw_class!!.selectedItemPosition

                selectedStandard = standards[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }



        val hwSectionAdapter = ArrayAdapter<String>(
                activity, android.R.layout.simple_spinner_dropdown_item, sections)
        spinner_hw_sec!!.adapter = hwSectionAdapter

        spinner_hw_sec!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(arg0: AdapterView<*>, arg1: View,
                                        arg2: Int, arg3: Long) {

                val position = spinner_hw_sec!!.selectedItemPosition

                selectedSec = sections[position]


            }


            override fun onNothingSelected(arg0: AdapterView<*>) {
                // TODO Auto-generated method stub


            }

        }
    }

    companion object {

        val TITLE = "ADD HOMEWORK"

        fun newInstance(): HomeworkFragment {

            return HomeworkFragment()
        }
    }

    override fun onClick(v: View?) {

        when(v!!.id){

            R.id.bt_hw_submit -> {

                val subject= et_hw_subject!!.text
                val topic=et_hw_topic!!.text

                if(subject==null || topic==null || subject.isEmpty() || topic.isEmpty() || selectedStandard==null || selectedStandard.equals(standards[0]) ||  selectedSec==null || selectedSec.equals(sections[0])){
                    longSnackbar(view!!, "Field Empty")
                }
                else if(subject.length>25 || topic.length>100) {
                    longSnackbar(view!!, "Subject & topic can't exceed 25 & 100 chars respectively.")
                } else {
                    addHomework(subject.toString(),topic.toString())
                }
            }
        }
    }

    private fun addHomework(subject:String, topic:String){

        hw_submit_progress!!.visibility=View.VISIBLE
        bt_hw_submit!!.visibility=View.GONE

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val homework = Homework()
        homework.htid=pref!!.getInt(Constants.ID,0)
        homework.htname=pref!!.getString(Constants.NAME,"Name")
        homework.hschoolcode=pref!!.getString(Constants.SCHOOL_CODE,"SchoolCode")
        homework.hschoolname=pref!!.getString(Constants.SCHOOL,"School")
        homework.hsubject=subject.trim()
        homework.htopic=topic.trim()
        homework.hclass=utils.getStandardValue(selectedStandard!!)
        homework.hsec=selectedSec
        val request = ServerRequest()
        request.setOperation(Constants.SEND_HW_OPERATION)
        request.setHomework(homework)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }


    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            hw_submit_progress!!.visibility = View.GONE
            bt_hw_submit!!.visibility = View.VISIBLE
            et_hw_subject!!.setText("")
            et_hw_topic!!.setText("")
            spinner_hw_class!!.setSelection(0,true)
            spinner_hw_sec!!.setSelection(0,true)
            longSnackbar(view!!, resp.getMessage())

        } else{

            hw_submit_progress!!.visibility = View.GONE
            bt_hw_submit!!.visibility = View.VISIBLE
            longSnackbar(view!!, resp.getMessage())


        }


    }

    private fun handleError(error: Throwable) {

        hw_submit_progress!!.visibility = View.GONE
        bt_hw_submit!!.visibility = View.VISIBLE
        Log.d(Constants.TAG, "failed")
        longSnackbar(view!!, error.localizedMessage)

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }


}