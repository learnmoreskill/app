package com.hacksterkrishna.a1teachers.models

import com.hacksterkrishna.a1teachers.Utils

/**
 * Created by krishna on 31/12/17.
 */
class AttendanceView {
    private var asroll:String?=null
    private var asname:String?=null
    private var astatus:String?=null
    private var aclass:String?=null
    private var asec:String?=null
    private var aclock:String?=null
    private var utils= Utils()

    fun getDate():String{
        return aclock!!
    }

    fun getRoll():String{
        return asroll!!
    }

    fun getName():String{
        return asname!!
    }

    fun getStatus():String{
        return astatus!!
    }

    fun getStandard():String{
        return utils.getStandardName(aclass!!)
    }

    fun getSection():String{
        return asec!!
    }

    fun setDate(aclock:String){
        this.aclock=aclock
    }

    fun setStandard(aclass:String){
        this.aclass=utils.getStandardValue(aclass)
    }

    fun setSection(asec:String){
        this.asec=asec
    }
}