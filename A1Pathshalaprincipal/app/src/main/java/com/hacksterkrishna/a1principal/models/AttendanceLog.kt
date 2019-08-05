package com.hacksterkrishna.a1principal.models

import com.hacksterkrishna.a1principal.Utils


class AttendanceLog{
    private var abclass:String?=null
    private var absec:String?=null
    private var abdate:String?=null
    private var abpcount:String?=null
    private var abacount:String?=null
    private var utils= Utils()

    fun getDate():String{
        return abdate!!
    }

    fun getStandard():String{
        return utils.getStandardName(abclass!!)
    }

    fun getSec():String{
        return absec!!
    }

    fun getAcount():String{
        return abacount!!
    }

    fun getPcount():String{
        return abpcount!!
    }

    fun setDate(abdate:String){
        this.abdate=abdate
    }

    fun setStandard(abclass:String){
        this.abclass=utils.getStandardValue(abclass)
    }

    fun setSec(absec:String){
        this.absec=absec
    }
}