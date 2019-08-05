package com.hacksterkrishna.a1teachers.models

/**
 * Created by krishna on 31/12/17.
 */
class BroadcastMessage{

    private var bmtid:Int?=null
    private var bmtname:String?=null
    private var bmtext:String?=null
    private var bmclass:String?=null
    private var bmsec:String?=null
    private var bmschoolname:String?=null
    private var bmschoolcode:String?=null
    private var bmdate:String?=null
    private var bmclock:String?=null


    fun getTname():String{
        return bmtname!!
    }

    fun getText():String{
        return bmtext!!
    }

    fun getStandard():String{
        return bmclass!!
    }

    fun getSec():String{
        return bmsec!!
    }

    fun getDateTime():String{
        return bmclock!!
    }


    fun setTid(bmtid:Int){
        this.bmtid=bmtid
    }

    fun setTname(bmtname:String){
        this.bmtname=bmtname
    }

    fun setStandard(bmclass:String){
        this.bmclass=bmclass
    }

    fun setSection(bmsec:String){
        this.bmsec=bmsec
    }

    fun setSchoolName(bmschoolname:String){
        this.bmschoolname=bmschoolname
    }

    fun setSchoolCode(bmschoolcode:String){
        this.bmschoolcode=bmschoolcode
    }

    fun setText(bmtext:String){
        this.bmtext=bmtext
    }

    fun setDate(bmdate:String){
        this.bmdate=bmdate
    }
}