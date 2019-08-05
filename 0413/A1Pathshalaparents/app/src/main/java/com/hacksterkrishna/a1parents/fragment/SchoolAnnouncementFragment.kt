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
import com.hacksterkrishna.a1parents.adapter.SchoolBroadcastDataAdapter
import com.hacksterkrishna.a1parents.model.SchoolBroadcast
import com.hacksterkrishna.a1parents.model.ServerRequest
import com.hacksterkrishna.a1parents.model.ServerResponse
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_school_announcement.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Created by krishna on 31/12/17.
 */

class SchoolAnnouncementFragment : Fragment(), AnkoLogger {

    private var brd_data: ArrayList<SchoolBroadcast>? = null
    private var brd_adapter: SchoolBroadcastDataAdapter? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity.title="Announcement"
        return inflater.inflate(R.layout.fragment_school_announcement, container, false)

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
        brd_card_recycler_view.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        brd_card_recycler_view.layoutManager = layoutManager
        getBroadcasts()
    }

    private fun getBroadcasts(){

        brd_progress.visibility = View.VISIBLE
        brd_error_card.visibility = View.GONE
        brd_card_recycler_view.visibility = View.GONE

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val request = ServerRequest(Constants.FETCH_SCHOOL_BROADCAST,null,null,null,null)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))

    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.result == Constants.SUCCESS) {
            brd_data=resp.schoolBroadcast
            brd_adapter = SchoolBroadcastDataAdapter(brd_data!!)
            brd_progress.visibility = View.GONE
            brd_card_recycler_view.visibility = View.VISIBLE
            brd_card_recycler_view.adapter = brd_adapter

        } else {

            brd_progress.visibility = View.GONE
            tv_brd_error_msg.text=resp.message
            brd_error_card.visibility = View.VISIBLE

        }
    }

    private fun handleError(error: Throwable) {

        brd_progress!!.visibility = View.GONE
        tv_brd_error_msg!!.text=error.localizedMessage
        brd_error_card!!.visibility = View.VISIBLE
        debug(error.localizedMessage)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    companion object {

        val TITLE = "School Announcement"

        fun newInstance(): SchoolAnnouncementFragment {

            return SchoolAnnouncementFragment()
        }
    }
}