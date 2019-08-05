package com.hacksterkrishna.a1parents.fragment

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import com.crashlytics.android.Crashlytics
import com.hacksterkrishna.a1parents.Constants
import com.hacksterkrishna.a1parents.R
import com.hacksterkrishna.a1parents.RequestInterface
import com.hacksterkrishna.a1parents.Utils
import com.hacksterkrishna.a1parents.model.Parent
import com.hacksterkrishna.a1parents.model.ServerRequest
import com.hacksterkrishna.a1parents.model.ServerResponse
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_complaint.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ComplaintFragment : Fragment(), AnkoLogger {

    private val complaintList = ArrayList<HashMap<String,String>>()
    private val KEY_COMPLAINT = "complaint"
    private val KEY_TIME = "time"
    private val KEY_TEACHER = "teacher"
    private val utils = Utils()
    private var mCompositeDisposable: CompositeDisposable? = null
    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity.title="My messages"
        return inflater.inflate(R.layout.fragment_complaint, container, false)

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
        setComplaintList()

    }

    private fun setComplaintList(){

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        pref = activity.getSharedPreferences("parentPrefs", Context.MODE_PRIVATE)
        val sid:String = pref!!.getString(Constants.SID,"Sid")
        val parent = Parent(null,null,null,sid,null,null,null,null,null,null,null,null,null,null)
        val request = ServerRequest(Constants.FETCH_COMPLAINT,parent,null,null,null)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
        view_complaint_progress.visibility= View.VISIBLE
    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.result == Constants.SUCCESS) {

            var complaintResp=resp.msg
            for(complaints in complaintResp!!){
                var map = HashMap<String,String>()
                map.put(KEY_COMPLAINT,complaints.cmsg!!.trim())
                map.put(KEY_TIME,"{cmd-calendar} "+utils.prettifyDateTime(complaints.cclock!!))
                map.put(KEY_TEACHER,"{cmd-account-circle} "+complaints.ctname!!)
                complaintList.add(map)
            }

            loadListView()

        } else {
            view_complaint_progress.visibility=View.GONE
            complaint_list_view.visibility=View.GONE
            tv_complaint_error_msg.text=resp.message
            complaint_error_card.visibility=View.VISIBLE
            debug(resp.message)
        }
    }

    private fun handleError(error: Throwable) {
        view_complaint_progress.visibility=View.GONE
        complaint_list_view.visibility=View.GONE
        tv_complaint_error_msg.text=error.localizedMessage
        complaint_error_card.visibility=View.VISIBLE
        debug(error.localizedMessage)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    private fun loadListView(){
        view_complaint_progress.visibility=View.GONE
        val adapter = SimpleAdapter(activity, complaintList, R.layout.complaint_list_item,
                arrayOf(KEY_COMPLAINT,KEY_TIME,KEY_TEACHER),
                intArrayOf(R.id.tv_complaint_message, R.id.itv_complaint_time, R.id.itv_complaint_tname))
        complaint_list_view.adapter=adapter
    }

    companion object {

        fun newInstance(): ComplaintFragment {

            return ComplaintFragment()
        }
    }
}