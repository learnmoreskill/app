package com.hacksterkrishna.a1teachers.models

/**
 * Created by krishna on 31/12/17.
 */
class FeeNotPaid{
    var feenpsid:String? = null
    var feenpstatus:String? = null
    var spnumber:String? = null

    constructor(feenpsid:String?,feenpstatus:String?,spnumber:String?){
        this.feenpsid=feenpsid
        this.feenpstatus=feenpstatus
        this.spnumber=spnumber
    }
}