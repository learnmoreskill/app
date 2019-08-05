package com.hacksterkrishna.a1teachers.fragment

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.crashlytics.android.Crashlytics
import com.hacksterkrishna.a1teachers.Constants
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.RequestInterface
import com.hacksterkrishna.a1teachers.Utils
import com.hacksterkrishna.a1teachers.adapter.FeeNotPaidAdapter
import com.hacksterkrishna.a1teachers.models.*
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
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

/**
 * Created by krishna on 31/12/17.
 */
class FeeNotPaidFragment : Fragment(){

    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    private var feenp_class:TextView? = null
    private var feenp_progress:ProgressBar? = null
    private var feenp_error_card:CardView? = null
    private var iv_feenp_error:ImageView? = null
    private var tv_feenp_error_msg:TextView? = null
    private var card_feenp_list:CardView? = null
    private var feenp_recycler_view:RecyclerView? = null
    private var btn_feenp_submit:AppCompatButton? = null
    private var utils= Utils()
    private var feenp_student_list: ArrayList<Student>? = null
    private var feenp_data_list = ArrayList<FeeNotPaid>()
    private var feenp_adapter: FeeNotPaidAdapter? = null
    private var mCompositeDisposable: CompositeDisposable? = null



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Fabric.with(activity, Crashlytics())
        val view = inflater!!.inflate(R.layout.fragment_feenp, container, false)
        activity.title = "Fee due notification"
        mCompositeDisposable = CompositeDisposable()
        pref=activity.getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        initViews(view)
        return view
    }

    private fun initViews(view: View){
        feenp_class=view.findViewById(R.id.feenp_class)
        feenp_class!!.text="{cmd-school} "+utils.getStandardName(pref!!.getString(Constants.STANDARD,"Class"))+" - "+pref!!.getString(Constants.SEC,"Sec")
        feenp_progress=view.findViewById(R.id.feenp_progress)
        feenp_error_card=view.findViewById(R.id.feenp_error_card)
        iv_feenp_error=view.findViewById(R.id.iv_feenp_error)
        tv_feenp_error_msg=view.findViewById(R.id.tv_feenp_error_msg)
        card_feenp_list=view.findViewById(R.id.card_feenp_list)
        feenp_recycler_view=view.findViewById(R.id.feenp_recycler_view)
        btn_feenp_submit=view.findViewById(R.id.btn_feenp_submit)
        feenp_recycler_view!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        feenp_recycler_view!!.layoutManager = layoutManager
        CreateList()
    }

    private fun CreateList(){
        //attendance_shimmer_recycler_view.addItemDecoration(HorizontalDividerItemDecoration.Builder(this@AttendanceActivity).color(R.color.colorAccent).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build())
        feenp_progress!!.visibility=View.VISIBLE
        card_feenp_list!!.visibility=View.GONE
        //attendance_shimmer_recycler_view.showShimmerAdapter()

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val teacher = Teacher()
        teacher.setStandard(pref!!.getString(Constants.STANDARD,"Class"))
        teacher.setSec(pref!!.getString(Constants.SEC,"Sec"))
        val request = ServerRequest()
        request.setOperation(Constants.FETCH_ALIST)
        request.setTeacher(teacher)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            feenp_student_list=resp.getStudents()
            for(data in feenp_student_list!!){
                feenp_data_list.add(FeeNotPaid(data.getSid(),"P", data.getSpnumber()))
            }
            feenp_adapter = FeeNotPaidAdapter(feenp_student_list!!,feenp_data_list)
            feenp_progress!!.visibility = View.GONE
            //attendance_shimmer_recycler_view.hideShimmerAdapter()
            card_feenp_list!!.visibility = View.VISIBLE
            feenp_recycler_view!!.adapter = feenp_adapter
            feenp_recycler_view!!.addItemDecoration(HorizontalDividerItemDecoration.Builder(activity).color(R.color.colorAccent).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build())
            btn_feenp_submit!!.visibility = View.VISIBLE
            btn_feenp_submit!!.setOnClickListener{
                val feenpData=feenp_adapter!!.FeenpDataList
                submitData(feenpData)
            }

        } else{

            feenp_progress!!.visibility = View.GONE
            //attendance_shimmer_recycler_view.hideShimmerAdapter()
            card_feenp_list!!.visibility = View.GONE
            longSnackbar(view, resp.getMessage())

        }


    }

    private fun handleError(error: Throwable) {

        feenp_progress!!.visibility = View.GONE
        //attendance_shimmer_recycler_view.hideShimmerAdapter()
        card_feenp_list!!.visibility = View.GONE
        longSnackbar(view, error.localizedMessage)
        Log.d(Constants.TAG, "failed")

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    fun submitData(feenpData: ArrayList<FeeNotPaid>){

        feenp_progress!!.visibility=View.VISIBLE
        card_feenp_list!!.visibility=View.GONE
        btn_feenp_submit!!.visibility=View.GONE

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
        val request = ServerRequest()
        request.setOperation(Constants.SUBMIT_FEENP_DATA)
        request.setFeenp(feenpData)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleSubmitResponse, this::handleSubmitError))

    }

    private fun handleSubmitResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {
            feenp_progress!!.visibility = View.GONE
            card_feenp_list!!.visibility = View.GONE
            btn_feenp_submit!!.visibility = View.GONE
            iv_feenp_error!!.setImageResource(R.drawable.ic_correct)
            tv_feenp_error_msg!!.text=resp.getMessage()
            feenp_error_card!!.visibility=View.VISIBLE
        } else{
            feenp_progress!!.visibility = View.GONE
            card_feenp_list!!.visibility = View.GONE
            btn_feenp_submit!!.visibility=View.GONE
            iv_feenp_error!!.setImageResource(R.drawable.ic_error3)
            tv_feenp_error_msg!!.text=resp.getMessage()
            feenp_error_card!!.visibility=View.VISIBLE

        }


    }

    private fun handleSubmitError(error: Throwable) {

        feenp_progress!!.visibility = View.GONE
        card_feenp_list!!.visibility = View.GONE
        btn_feenp_submit!!.visibility=View.GONE
        iv_feenp_error!!.setImageResource(R.drawable.ic_error3)
        tv_feenp_error_msg!!.text=error.localizedMessage
        feenp_error_card!!.visibility=View.VISIBLE
        Log.d(Constants.TAG, "failed")

    }

}