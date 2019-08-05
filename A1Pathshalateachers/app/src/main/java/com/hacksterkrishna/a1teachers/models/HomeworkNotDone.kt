package com.hacksterkrishna.a1teachers.models

/**
 * Created by krishna on 31/12/17.
 */
class HomeworkNotDone{
    var hwndsid:String? = null
    var hwndsub:String? = null
    var hwndstatus:String? = null
    var hwnddate:String? = null
    var spnumber:String? = null

    constructor(hwndsid:String?,hwndsub:String?,hwndstatus:String?,hwnddate:String?,spnumber:String?){
        this.hwndsid=hwndsid
        this.hwndsub=hwndsub
        this.hwndstatus=hwndstatus
        this.hwnddate=hwnddate
        this.spnumber=spnumber
    }
}