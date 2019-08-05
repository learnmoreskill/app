package com.hacksterkrishna.a1principal.models

import com.hacksterkrishna.a1principal.Utils


class Broadcast{

    private var bmtname:String?=null
    private var bmtext:String?=null
    private var bmclass:String?=null
    private var bmsec:String?=null
    private var bmdate:String?=null
    private var bmclock:String?=null
    private var utils= Utils()

    fun getTname():String{
        return bmtname!!
    }

    fun getText():String{
        return bmtext!!
    }

    fun getStandard():String{
        return utils.getStandardName(bmclass!!)
    }

    fun getSection():String{
        return bmsec!!
    }

    fun getdate():String{
        return bmdate!!
    }

    fun getTime():String{
        return bmclock!!
    }

    fun setDate(bmdate:String){
        this.bmdate=bmdate
    }

    fun setClass(bmclass:String){
        this.bmclass=utils.getStandardValue(bmclass)
    }

    fun setSec(bmsec:String){
        this.bmsec=bmsec
    }
}