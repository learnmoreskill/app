package com.hacksterkrishna.a1parents.fragment

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crashlytics.android.Crashlytics
import com.hacksterkrishna.a1parents.Constants
import com.hacksterkrishna.a1parents.R
import com.hacksterkrishna.a1parents.RequestInterface
import com.hacksterkrishna.a1parents.adapter.HomeworkDataAdapter
import com.hacksterkrishna.a1parents.model.Homework
import com.hacksterkrishna.a1parents.model.ServerRequest
import com.hacksterkrishna.a1parents.model.ServerResponse
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_homework.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Created by krishna on 31/12/17.
 */

class HomeworkFragment : Fragment(), AnkoLogger {

    private var hw_data: ArrayList<Homework>? = null
    private var hw_adapter: HomeworkDataAdapter? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity.title="Homework"
        return inflater.inflate(R.layout.fragment_homework, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Fabric.with(activity, Crashlytics())
        val fabric = Fabric.Builder(activity)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)
        pref = activity.getSharedPreferences("parentPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        mCompositeDisposable = CompositeDisposable()
        hw_card_recycler_view.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        hw_card_recycler_view.layoutManager = layoutManager
        getHomeworks()
    }

    private fun getHomeworks(){

        hw_progress.visibility = View.VISIBLE
        hw_error_card.visibility = View.GONE
        hw_card_recycler_view.visibility = View.GONE

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        pref = activity.getSharedPreferences("parentPrefs", Context.MODE_PRIVATE)
        val standard:String = pref!!.getString(Constants.SSTANDARD,"Standard")
        val sec:String = pref!!.getString(Constants.SSEC,"Sec")
        val homework = Homework(null,null,null,standard,sec,null,null)
        val request = ServerRequest(Constants.FETCH_HOMEWORK,null,null,null,homework)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))

    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.result == Constants.SUCCESS) {
            hw_data=resp.homework
            hw_adapter = HomeworkDataAdapter(hw_data!!)
            hw_progress.visibility = View.GONE
            hw_card_recycler_view.visibility = View.VISIBLE
            hw_card_recycler_view.adapter = hw_adapter

        } else{

            hw_progress.visibility = View.GONE
            tv_hw_error_msg.text=resp.message
            hw_error_card.visibility = View.VISIBLE

        }


    }

    private fun handleError(error: Throwable) {

        hw_progress!!.visibility = View.GONE
        tv_hw_error_msg!!.text=error.localizedMessage
        hw_error_card!!.visibility = View.VISIBLE
        debug("failed")
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    companion object {
        fun newInstance(): HomeworkFragment {

            return HomeworkFragment()
        }
    }
}