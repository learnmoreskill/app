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
import com.rengwuxian.materialedittext.MaterialEditText
import com.hacksterkrishna.a1teachers.Constants
import com.hacksterkrishna.a1teachers.R
import com.hacksterkrishna.a1teachers.RequestInterface
import com.hacksterkrishna.a1teachers.adapter.GroupComplaintAdapter
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

class GroupComplaintFragment : Fragment(){

    private var pref: SharedPreferences? = null
    private var Base_url: String?=null

    private var et_grpc_msg: MaterialEditText? = null
    private var grpc_progress: ProgressBar? = null
    private var grpc_error_card: CardView? = null
    private var grpc_details_card: CardView? = null
    private var iv_grpc_error: ImageView? = null
    private var tv_grpc_error_msg: TextView? = null
    private var card_grpc_list: CardView? = null
    private var grpc_recycler_view: RecyclerView? = null
    private var btn_grpc_submit: AppCompatButton? = null
    private var grpc_student_list: ArrayList<Student>? = null
    private var grpc_data_list = ArrayList<GroupComplaint>()
    private var grpc_adapter: GroupComplaintAdapter? = null
    private var mCompositeDisposable: CompositeDisposable? = null



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Fabric.with(activity, Crashlytics())
        val view = inflater!!.inflate(R.layout.fragment_group_complaint, container, false)
        activity.title = "Group Message"
        mCompositeDisposable = CompositeDisposable()
        pref=activity.getSharedPreferences("teacherPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        initViews(view)
        return view
    }

    private fun initViews(view: View){
        et_grpc_msg=view.findViewById(R.id.et_grpc_msg)
        grpc_progress=view.findViewById(R.id.grpc_progress)
        grpc_error_card=view.findViewById(R.id.grpc_error_card)
        iv_grpc_error=view.findViewById(R.id.iv_grpc_error)
        tv_grpc_error_msg=view.findViewById(R.id.tv_grpc_error_msg)
        grpc_details_card= view.findViewById(R.id.grpc_details_card)
        card_grpc_list=view.findViewById(R.id.card_grpc_list)
        grpc_recycler_view=view.findViewById(R.id.grpc_recycler_view)
        btn_grpc_submit=view.findViewById(R.id.btn_grpc_submit)
        grpc_recycler_view!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        grpc_recycler_view!!.layoutManager = layoutManager
        CreateList()
    }

    private fun CreateList(){
        //attendance_shimmer_recycler_view.addItemDecoration(HorizontalDividerItemDecoration.Builder(this@AttendanceActivity).color(R.color.colorAccent).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build())
        grpc_progress!!.visibility= View.VISIBLE
        card_grpc_list!!.visibility= View.GONE
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

            grpc_student_list=resp.getStudents()
            for(data in grpc_student_list!!){
                grpc_data_list.add(GroupComplaint(pref!!.getInt(Constants.ID,0),pref!!.getString(Constants.NAME,"Name"),pref!!.getString(Constants.SCHOOL_CODE,"SchoolCode"),data.getSid(),data.getSname(),data.getSclass(),data.getSsec(),"",null,null,data.getSpnumber(),"N"))
            }
            grpc_adapter = GroupComplaintAdapter(grpc_student_list!!,grpc_data_list)
            grpc_progress!!.visibility = View.GONE
            //attendance_shimmer_recycler_view.hideShimmerAdapter()
            card_grpc_list!!.visibility = View.VISIBLE
            grpc_recycler_view!!.adapter = grpc_adapter
            grpc_recycler_view!!.addItemDecoration(HorizontalDividerItemDecoration.Builder(activity).color(R.color.colorAccent).sizeResId(R.dimen.divider).marginResId(R.dimen.leftmargin, R.dimen.rightmargin).build())
            btn_grpc_submit!!.visibility = View.VISIBLE
            btn_grpc_submit!!.setOnClickListener{
                val message = et_grpc_msg!!.text
                if(message==null || message.isEmpty()){
                    longSnackbar(view, "Field Empty")
                }
                else if(message.length>140) {
                    longSnackbar(view, "Can't exceed 140 chars")
                } else {
                    val grpcData = grpc_adapter!!.GrpcDataList
                    for(i in grpcData.indices){
                        grpcData[i].cmsg=message.toString().trim()
                    }
                    submitData(grpcData)
                }
            }

        } else{

            grpc_progress!!.visibility = View.GONE
            //attendance_shimmer_recycler_view.hideShimmerAdapter()
            card_grpc_list!!.visibility = View.GONE
            longSnackbar(view, resp.getMessage())

        }


    }

    private fun handleError(error: Throwable) {

        grpc_progress!!.visibility = View.GONE
        //attendance_shimmer_recycler_view.hideShimmerAdapter()
        card_grpc_list!!.visibility = View.GONE
        longSnackbar(view, error.localizedMessage)
        Log.d(Constants.TAG, "failed")

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    fun submitData(grpcData: ArrayList<GroupComplaint>){

        grpc_progress!!.visibility= View.VISIBLE
        grpc_details_card!!.visibility=View.GONE
        card_grpc_list!!.visibility= View.GONE
        btn_grpc_submit!!.visibility= View.GONE

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
        request.setOperation(Constants.SUBMIT_GRPC_DATA)
        request.setGrpc(grpcData)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleSubmitResponse, this::handleSubmitError))

    }

    private fun handleSubmitResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {
            grpc_progress!!.visibility = View.GONE
            grpc_details_card!!.visibility=View.GONE
            card_grpc_list!!.visibility = View.GONE
            btn_grpc_submit!!.visibility = View.GONE
            iv_grpc_error!!.setImageResource(R.drawable.ic_correct)
            tv_grpc_error_msg!!.text=resp.getMessage()
            grpc_error_card!!.visibility= View.VISIBLE
        } else{
            grpc_progress!!.visibility = View.GONE
            grpc_details_card!!.visibility=View.GONE
            card_grpc_list!!.visibility = View.GONE
            btn_grpc_submit!!.visibility= View.GONE
            iv_grpc_error!!.setImageResource(R.drawable.ic_error3)
            tv_grpc_error_msg!!.text=resp.getMessage()
            grpc_error_card!!.visibility= View.VISIBLE

        }


    }

    private fun handleSubmitError(error: Throwable) {

        grpc_progress!!.visibility = View.GONE
        grpc_details_card!!.visibility=View.GONE
        card_grpc_list!!.visibility = View.GONE
        btn_grpc_submit!!.visibility= View.GONE
        iv_grpc_error!!.setImageResource(R.drawable.ic_error3)
        tv_grpc_error_msg!!.text=error.localizedMessage
        grpc_error_card!!.visibility= View.VISIBLE
        Log.d(Constants.TAG, "failed")

    }

}