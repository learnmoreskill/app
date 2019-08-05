package com.hacksterkrishna.a1principal.models

import com.hacksterkrishna.a1principal.Utils



class Message{

    private var ctname:String?=null
    private var csname:String?=null
    private var csclass:String?=null
    private var cssec:String?=null
    private var cmsg:String?=null
    private var cdate:String?=null
    private var cclock:String?=null
    private var utils= Utils()

    fun getTname():String{
        return ctname!!
    }

    fun getSname():String{
        return csname!!
    }

    fun getMessage():String{
        return cmsg!!
    }

    fun getStandard():String{
        return utils.getStandardName(csclass!!)
    }

    fun getSection():String{
        return cssec!!
    }

    fun getdate():String{
        return cdate!!
    }

    fun getTime():String{
        return cclock!!
    }

    fun setDate(cdate:String){
        this.cdate=cdate
    }

    fun setClass(csclass:String){
        this.csclass=utils.getStandardValue(csclass)
    }

    fun setSec(cssec:String){
        this.cssec=cssec
    }
}