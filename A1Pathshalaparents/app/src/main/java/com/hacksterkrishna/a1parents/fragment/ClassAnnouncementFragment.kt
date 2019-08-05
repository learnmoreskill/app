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
import com.hacksterkrishna.a1parents.adapter.ClassBroadcastDataAdapter
import com.hacksterkrishna.a1parents.model.ClassBroadcast
import com.hacksterkrishna.a1parents.model.ServerRequest
import com.hacksterkrishna.a1parents.model.ServerResponse
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_class_announcement.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ClassAnnouncementFragment : Fragment(), AnkoLogger {

    private var bm_data: ArrayList<ClassBroadcast>? = null
    private var bm_adapter: ClassBroadcastDataAdapter? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity.title="Announcement"
        return inflater.inflate(R.layout.fragment_class_announcement, container, false)

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
        bm_card_recycler_view.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        bm_card_recycler_view.layoutManager = layoutManager
        getBroadcasts()
    }

    private fun getBroadcasts(){

        bm_progress.visibility = View.VISIBLE
        bm_error_card.visibility = View.GONE
        bm_card_recycler_view.visibility = View.GONE

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        pref = activity.getSharedPreferences("parentPrefs", Context.MODE_PRIVATE)
        val standard:String = pref!!.getString(Constants.SSTANDARD,"Standard")
        val sec:String = pref!!.getString(Constants.SSEC,"Sec")
        val broadcast = ClassBroadcast(null,null,standard,sec,null,null)
        val request = ServerRequest(Constants.FETCH_CLASS_BROADCAST,null,broadcast,null,null)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))

    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.result == Constants.SUCCESS) {
            bm_data=resp.classBroadcast
            bm_adapter = ClassBroadcastDataAdapter(bm_data!!)
            bm_progress.visibility = View.GONE
            bm_card_recycler_view.visibility = View.VISIBLE
            bm_card_recycler_view.adapter = bm_adapter

        } else{

            bm_progress.visibility = View.GONE
            tv_bm_error_msg.text=resp.message
            bm_error_card.visibility = View.VISIBLE

        }


    }

    private fun handleError(error: Throwable) {

        bm_progress!!.visibility = View.GONE
        tv_bm_error_msg!!.text=error.localizedMessage
        bm_error_card!!.visibility = View.VISIBLE
        debug("failed")
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    companion object {

        val TITLE = "Class Announcement"

        fun newInstance(): ClassAnnouncementFragment {

            return ClassAnnouncementFragment()
        }
    }
}