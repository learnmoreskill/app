package com.hacksterkrishna.a1principal.fragment

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
import com.mikepenz.iconics.view.IconicsImageButton
import com.hacksterkrishna.a1principal.Constants
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.RequestInterface
import com.hacksterkrishna.a1principal.adapter.StudentsDataAdapter
import com.hacksterkrishna.a1principal.models.ServerRequest
import com.hacksterkrishna.a1principal.models.ServerResponse
import com.hacksterkrishna.a1principal.models.Student
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Created by krishna on 31/12/17.
 */
class DetailsFragment: Fragment(), View.OnClickListener{


    private var et_sd_name : EditText? = null
    private var tv_sd_error_msg : TextView? = null
    private var sd_error_card : CardView? = null
    private var ivb_sd_search: IconicsImageButton? = null
    private var sd_recyclerView: RecyclerView? = null
    private var sd_data: ArrayList<Student>? = null
    private var sd_adapter: StudentsDataAdapter? = null
    private var sd_progress: ProgressBar? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    private var pref: SharedPreferences? = null
    private var Base_url: String?=null




    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
            val view = inflater!!.inflate(R.layout.fragment_details, container, false)
            activity.title = "Students Details"

            pref=activity.getSharedPreferences("princiPrefs", Context.MODE_PRIVATE)
            Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

            mCompositeDisposable = CompositeDisposable()
            initViews(view)
            return view
        }

    private fun initViews(view: View) {
        et_sd_name=view.findViewById(R.id.et_sd_name)
        tv_sd_error_msg=view.findViewById(R.id.tv_sd_error_msg)
        ivb_sd_search=view.findViewById(R.id.ivb_sd_search)
        sd_error_card=view.findViewById(R.id.sd_error_card)
        sd_recyclerView=view.findViewById(R.id.sd_card_recycler_view)
        sd_progress=view.findViewById(R.id.sd_progress)
        sd_recyclerView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        sd_recyclerView!!.layoutManager = layoutManager
        ivb_sd_search!!.setOnClickListener(this)
    }

    fun search(queryName:String){

        var queryName:String=queryName

        sd_progress!!.visibility = View.VISIBLE
        sd_error_card!!.visibility = View.GONE
        sd_recyclerView!!.visibility = View.GONE

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val student = Student()
        student.setSname(queryName)
        val request = ServerRequest()
        request.setOperation(Constants.FETCH_SD_SEARCH_OPERATION)
        request.setStudent(student)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))

    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            sd_data=resp.getStudents()
            sd_adapter = StudentsDataAdapter(sd_data!!)
            sd_progress!!.visibility = View.GONE
            sd_recyclerView!!.visibility = View.VISIBLE
            sd_recyclerView!!.adapter = sd_adapter

        } else{

            sd_progress!!.visibility = View.GONE
            tv_sd_error_msg!!.text=resp.getMessage()
            sd_error_card!!.visibility = View.VISIBLE
            //Snackbar.make(view!!, resp.getMessage(), Snackbar.LENGTH_LONG).show()


        }


    }

    private fun handleError(error: Throwable) {

        sd_progress!!.visibility = View.GONE
        tv_sd_error_msg!!.text=error.localizedMessage
        sd_error_card!!.visibility = View.VISIBLE
        Log.d(Constants.TAG, "failed")
        //Snackbar.make(view!!, t.localizedMessage, Snackbar.LENGTH_LONG).show()



    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }


    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.ivb_sd_search -> {

                var queryName:String=et_sd_name!!.text.toString()
                if(!queryName.isEmpty()) {

                    search(queryName)
                } else {

                    Snackbar.make(view!!, "Search field empty", Snackbar.LENGTH_LONG).show()
                }
            }

        }


    }



}