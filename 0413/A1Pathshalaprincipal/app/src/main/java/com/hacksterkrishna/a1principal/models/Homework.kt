package com.hacksterkrishna.a1principal.models

import com.hacksterkrishna.a1principal.Utils



class Homework{

    private var htname:String?=null
    private var hsubject:String?=null
    private var htopic:String?=null
    private var hclass:String?=null
    private var hsec:String?=null
    private var hdate:String?=null
    private var hclock:String?=null
    private var utils= Utils()


    fun getTname():String{
        return htname!!
    }

    fun getSubject():String{
        return hsubject!!
    }

    fun getTopic():String{
        return htopic!!
    }

    fun getStandard():String{
        return utils.getStandardName(hclass!!)
    }

    fun getSection():String{
        return hsec!!
    }

    fun getdate():String{
        return hdate!!
    }

    fun getTime():String{
        return hclock!!
    }

    fun setDate(hdate:String){
        this.hdate=hdate
    }

    fun setClass(hclass:String){
        this.hclass=utils.getStandardValue(hclass)
    }

    fun setSec(hsec:String){
        this.hsec=hsec
    }

}