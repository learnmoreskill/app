package com.hacksterkrishna.a1principal.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SimpleAdapter
import com.crashlytics.android.Crashlytics
import com.mikepenz.iconics.context.IconicsContextWrapper
import com.hacksterkrishna.a1principal.Constants
import com.hacksterkrishna.a1principal.R
import com.hacksterkrishna.a1principal.RequestInterface
import com.hacksterkrishna.a1principal.Utils
import com.hacksterkrishna.a1principal.models.ServerRequest
import com.hacksterkrishna.a1principal.models.ServerResponse
import com.hacksterkrishna.a1principal.models.Student
import io.fabric.sdk.android.Fabric
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_student_detail_homework.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
/**
 * Created by krishna on 31/12/17.
 */
class StudentDetailHomework : AppCompatActivity() {

    private var sid:String?=null
    private var sname:String?=null
    private val hwndList = ArrayList<HashMap<String,String>>()
    private val KEY_SUB = "subject"
    private val KEY_DATE = "date"
    private val utils = Utils()
    private var mCompositeDisposable: CompositeDisposable? = null

    private var pref: SharedPreferences?= null
    private var Base_url: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_student_detail_homework)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        sid = intent.getStringExtra("sid")
        sname = intent.getStringExtra("sname")
        this.title=sname+"'s undone Homeworks"

        pref=getSharedPreferences("princiPrefs", Context.MODE_PRIVATE)
        Base_url=pref!!.getString(Constants.SCHOOL_URL,"url")

        mCompositeDisposable = CompositeDisposable()
        setHwNotDoneList()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase))
    }

    fun setHwNotDoneList(){

        val requestInterface = Retrofit.Builder()
                .baseUrl(Base_url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RequestInterface::class.java)

        val student = Student()
        student.setSid(sid!!)
        val request = ServerRequest()
        request.setOperation(Constants.FETCH_SD_HOMEWORK_OPERATION)
        request.setStudent(student)
        mCompositeDisposable?.add(requestInterface.operation(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
        sd_view_hwnd_progress.visibility= View.VISIBLE
    }

    private fun handleResponse(resp: ServerResponse) {

        if (resp.getResult() == Constants.SUCCESS) {

            var hwndResp=resp.getHomeworkNotDone()
            for(hwnd in hwndResp){
                var map = HashMap<String,String>()
                map.put(KEY_SUB,hwnd.getSub())
                map.put(KEY_DATE,"{cmd-calendar} "+utils.prettifyDate(hwnd.getDate()))
                hwndList.add(map)
            }

            loadListView()

        } else{
            sd_view_hwnd_progress.visibility= View.GONE
            hwnotdone_list_view.visibility= View.GONE
            tv_sd_hwnd_error_msg.text=resp.getMessage()
            sd_hwnd_error_card.visibility= View.VISIBLE
            Log.d(Constants.TAG, resp.getMessage())
            //Snackbar.make(view!!, resp.getMessage(), Snackbar.LENGTH_LONG).show()


        }


    }

    private fun handleError(error: Throwable) {

        sd_view_hwnd_progress.visibility= View.GONE
        hwnotdone_list_view.visibility= View.GONE
        tv_sd_hwnd_error_msg.text=error.localizedMessage
        sd_hwnd_error_card.visibility= View.VISIBLE
        Log.d(Constants.TAG, error.localizedMessage)
        //Snackbar.make(view!!, t.localizedMessage, Snackbar.LENGTH_LONG).show()

    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    fun loadListView(){
        sd_view_hwnd_progress.visibility=View.GONE
        val adapter = SimpleAdapter(this, hwndList, R.layout.homework_list_item,
                arrayOf(KEY_SUB,KEY_DATE),
                intArrayOf(R.id.tv_sd_hwnd_subject, R.id.tv_sd_hwnd_date))
        hwnotdone_list_view.adapter=adapter

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                //NavUtils.navigateUpFromSameTask(this)
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
