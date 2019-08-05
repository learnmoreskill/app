package com.hacksterkrishna.a1teachers.models

/**
 * Created by krishna on 31/12/17.
 */
class AttendanceData{

    var asid:String? = null
    var asroll:String? = null
    var asname:String? = null
    var astatus:String? = null
    var aschoolcode:String? = null
    var aclass:String? = null
    var asec:String? = null
    var spnumber:String? = null

    constructor(asid:String,asroll:String,asname:String,astatus:String,aschoolcode:String,aclass:String,asec:String,spnumber:String){

        this.asid=asid
        this.asroll=asroll
        this.asname=asname
        this.astatus=astatus
        this.aschoolcode=aschoolcode
        this.aclass=aclass
        this.asec=asec
        this.spnumber=spnumber

    }
}