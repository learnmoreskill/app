package com.hacksterkrishna.a1principal.fragment

/**
 * Created by krishna on 31/12/17.
 */
import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.rengwuxian.materialedittext.MaterialEditText
import com.hacksterkrishna.a1principal.Constants
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.RequestInterface
import com.hacksterkrishna.a1principal.models.BroadcastMessage
import com.hacksterkrishna.a1principal.models.ServerRequest
import com.hacksterkrishna.a1principal.models.ServerResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class BroadCastSendFragment : Fragment(), View.OnClickListener {

    private var et_brd_msg: MaterialEditText ?= null
    private var btn_brd_send: AppCompatButton?= null
    private var brd_progress: ProgressBar?= null

    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_broadcast_send, container, false)
        activity.title = "Broadcast"

        pref=activity.getSharedPreferences("princiPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        initViews(view)
        mCompositeDisposable = CompositeDisposable()
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

                var message= et_brd_msg!!.text

                if(message==null || message.isEmpty()){
                    Snackbar.make(view!!, "Field Empty", Snackbar.LENGTH_LONG).show()
                }
                else if(message.length>300) {
                    Snackbar.make(view!!, "Can't exceed 300 chars", Snackbar.LENGTH_LONG).show()
                } else {
                    SendBroadcast(message.toString())
                }
            }

        }
    }

    fun SendBroadcast(message:String){

        brd_progress!!.visibility=View.VISIBLE
        btn_brd_send!!.visibility=View.GONE

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)


        val broadcastMessage = BroadcastMessage()
        broadcastMessage.setPname(pref!!.getString(Constants.NAME,"Name"))
        broadcastMessage.setText(message)
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
            Snackbar.make(view!!, resp.getMessage(), Snackbar.LENGTH_LONG).show()

        } else{

            brd_progress!!.visibility = View.GONE
            btn_brd_send!!.visibility = View.VISIBLE
            Snackbar.make(view!!, resp.getMessage(), Snackbar.LENGTH_LONG).show()


        }

    }

    private fun handleError(error: Throwable) {

        brd_progress!!.visibility = View.GONE
        btn_brd_send!!.visibility = View.VISIBLE
        Log.d(Constants.TAG, "failed")
        Snackbar.make(view!!, error.localizedMessage, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

}