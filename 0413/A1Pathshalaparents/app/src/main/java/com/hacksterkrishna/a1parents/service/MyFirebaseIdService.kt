package com.hacksterkrishna.a1parents.service

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.hacksterkrishna.a1parents.Constants
import com.hacksterkrishna.a1parents.RequestInterface
import com.hacksterkrishna.a1parents.model.Parent
import com.hacksterkrishna.a1parents.model.ServerRequest
import com.hacksterkrishna.a1parents.model.ServerResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by haacksterkrishna on 18/12/17.
 */
class MyFirebaseIdService : FirebaseInstanceIdService(),AnkoLogger{

    private var pref: SharedPreferences? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private var Base_url: String?=null

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        pref=getSharedPreferences("parentPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        if(pref!!.getBoolean(Constants.IS_LOGGED_IN, false)) {
            submitFcmid()
            info(FirebaseInstanceId.getInstance().token)
        }
    }

    private fun submitFcmid() {

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val parent = Parent(null,null,null,pref!!.getString(Constants.SID,"Sid"),null,null,null,null,null,null,null,null,null,FirebaseInstanceId.getInstance().token)
        val request = ServerRequest(Constants.SUBMIT_FCMID_OPERATION,parent,null,null,null)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleSubmitResponse, this::handleSubmitError))

    }

    private fun handleSubmitResponse(resp: ServerResponse) {
        if (resp.result == Constants.SUCCESS) {
            info("FCM id updated")
        }

    }

    private fun handleSubmitError(error: Throwable) {
        debug(error.localizedMessage)
    }



    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

}