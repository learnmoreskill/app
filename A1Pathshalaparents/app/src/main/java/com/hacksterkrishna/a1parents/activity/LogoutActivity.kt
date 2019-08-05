package com.hacksterkrishna.a1parents.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.hacksterkrishna.a1parents.Constants
import com.hacksterkrishna.a1parents.DropToken
import com.hacksterkrishna.a1parents.R
import com.hacksterkrishna.a1parents.RequestInterface
import com.hacksterkrishna.a1parents.model.Parent
import com.hacksterkrishna.a1parents.model.ServerRequest
import com.hacksterkrishna.a1parents.model.ServerResponse
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by krishna on 31/12/17.
 */

class LogoutActivity : AppCompatActivity() , AnkoLogger {

    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)
        pref = getSharedPreferences("parentPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        mCompositeDisposable = CompositeDisposable()
        Fabric.with(this, Crashlytics())
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)

        logoutProcess()

    }

    private fun logoutProcess() {
        dropTokenFromServer()
    }

    private fun dropTokenFromServer() {
        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val parent = Parent(null,null,null,pref!!.getString(Constants.SID,"Sid"),null,null,null,null,null,null,null,null,null,null)
        val request = ServerRequest(Constants.DROP_FCMID_OPERATION,parent,null,null,null)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleSubmitResponse, this::handleSubmitError))

    }

    private fun handleSubmitResponse(resp: ServerResponse) {
        if (resp.result == Constants.SUCCESS) {
            info("Notifications Disabled")
        } else {
            info("Failed: "+resp.message)
        }

        val editor = pref!!.edit()
        editor.putBoolean(Constants.IS_LOGGED_IN, false)
        editor.putString(Constants.NAME, "")
        editor.putString(Constants.NUMBER, "")
        editor.putString(Constants.EMAIL, "")
        editor.putString(Constants.SID, "")
        editor.putString(Constants.SROLL, "")
        editor.putString(Constants.SADMSNNO, "")
        editor.putString(Constants.SNAME, "")
        editor.putString(Constants.SADDRESS, "")
        editor.putString(Constants.SEMAIL, "")
        editor.putString(Constants.SSCHOOL, "")
        editor.putString(Constants.SSCHOOL_CODE, "")
        editor.putString(Constants.SSTANDARD, "")
        editor.putString(Constants.SSEC, "")
        editor.apply()
        DropToken().execute()
        goToLogin()

    }

    private fun handleSubmitError(error: Throwable) {
        val editor = pref!!.edit()
        editor.putBoolean(Constants.IS_LOGGED_IN, false)
        editor.putString(Constants.NAME, "")
        editor.putString(Constants.NUMBER, "")
        editor.putString(Constants.EMAIL, "")
        editor.putString(Constants.SID, "")
        editor.putString(Constants.SROLL, "")
        editor.putString(Constants.SADMSNNO, "")
        editor.putString(Constants.SNAME, "")
        editor.putString(Constants.SADDRESS, "")
        editor.putString(Constants.SEMAIL, "")
        editor.putString(Constants.SSCHOOL, "")
        editor.putString(Constants.SSCHOOL_CODE, "")
        editor.putString(Constants.SSTANDARD, "")
        editor.putString(Constants.SSEC, "")

        //clear the SchoolUrl from shared preferences
        editor.putString(Constants.SCHOOL_URL,"")

        editor.apply()
        debug(error.localizedMessage)
        DropToken().execute()
        goToLogin()
    }



    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    private fun goToLogin() {
        startActivity<LoginActivity>()
        finish()
    }

}