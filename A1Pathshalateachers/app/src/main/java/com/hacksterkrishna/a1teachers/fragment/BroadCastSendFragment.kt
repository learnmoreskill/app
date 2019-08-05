package com.hacksterkrishna.a1teachers.fragment

/**
 * Created by krishna on 31/12/17.
 */
import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.crashlytics.android.Crashlytics
import com.rengwuxian.materialedittext.MaterialEditText
import com.hacksterkrishna.a1teachers.Constants
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.RequestInterface
import com.hacksterkrishna.a1teachers.models.BroadcastMessage
import com.hacksterkrishna.a1teachers.models.ServerRequest
import com.hacksterkrishna.a1teachers.models.ServerResponse
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.jetbrains.anko.design.longSnackbar
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class BroadCastSendFragment : Fragment(), View.OnClickListener {

    private var et_brd_msg: MaterialEditText ?= null
    private var btn_brd_send: AppCompatButton?= null
    private var brd_progress: ProgressBar?= null
    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Fabric.with(activity, Crashlytics())
        val view = inflater!!.inflate(R.layout.fragment_broadcast_send, container, false)

        activity.title = "Class Broadcast"
        mCompositeDisposable = CompositeDisposable()
        pref=activity.getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        initViews(view)
        return view
    }

    private fun initViews (view: View) {
        et_brd_msg=view.findViewById(R.id.et_brd_msg)
        brd_progress=view.findViewById(R.id.brd_progress)
        btn_brd_send=view.findViewById(R.id.btn_brd_send)
        btn_brd_send!!.setOnClickListener(this)
    }

    companion object {

        val TITLE = "SEND BROADCAST"

        fun newInstance(): BroadCastSendFragment {

            return BroadCastSendFragment()
        }
    }

    override fun onClick(v: View?) {

        when(v!!.id){
            R.id.btn_brd_send -> {

                val message= et_brd_msg!!.text

                if(message==null || message.isEmpty()){
                    longSnackbar(view!!, "Field Empty")
                }
                else if(message.length>140) {
                    longSnackbar(view!!, "Can't exceed 140 chars")
                } else {
                    SendBroadcast(message.toString())
                }
            }

        }
    }

    fun SendBroadcast(message:String){

        brd_progress!!.visibility=View.VISIBLE
        btn_brd_send!!.visibility=View.GONE

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val broadcastMessage = BroadcastMessage()
        broadcastMessage.setTid(pref!!.getInt(Constants.ID,0))
        broadcastMessage.setTname(pref!!.getString(Constants.NAME,"Name"))
        broadcastMessage.setStandard(pref!!.getString(Constants.STANDARD,"Class"))
        broadcastMessage.setSection(pref!!.getString(Constants.SEC,"Sec"))
        broadcastMessage.setSchoolName(pref!!.getString(Constants.SCHOOL,"School"))
        broadcastMessage.setSchoolCode(pref!!.getString(Constants.SCHOOL_CODE,"SchoolCode"))
        broadcastMessage.setText(message.trim())
        val request = ServerRequest()
        request.setOperation(Constants.SEND_BRD_OPERATION)
        request.setBroadcastMessage(broadcastMessage)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))

    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            brd_progress!!.visibility = View.GONE
            btn_brd_send!!.visibility = View.VISIBLE
            et_brd_msg!!.setText("")
            longSnackbar(view!!, resp.getMessage())

        } else{

            brd_progress!!.visibility = View.GONE
            btn_brd_send!!.visibility = View.VISIBLE
            longSnackbar(view!!, resp.getMessage())


        }


    }

    private fun handleError(error: Throwable) {

        brd_progress!!.visibility = View.GONE
        btn_brd_send!!.visibility = View.VISIBLE
        Log.d(Constants.TAG, "failed")
        longSnackbar(view!!, error.localizedMessage)


    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

}