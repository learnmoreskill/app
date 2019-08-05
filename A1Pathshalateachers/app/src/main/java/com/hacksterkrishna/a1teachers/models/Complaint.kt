package com.hacksterkrishna.a1teachers.models

/**
 * Created by krishna on 31/12/17.
 */
class Complaint{

    private var ctid:Int?=null
    private var ctname:String?=null
    private var ctschoolcode:String?=null
    private var csid:String?=null
    private var csname:String?=null
    private var csclass:String?=null
    private var cssec:String?=null
    private var cmsg:String?=null
    private var cdate:String?=null
    private var cclock:String?=null
    private var spnumber:String?=null

    fun getTname():String{
        return ctname!!
    }

    fun getSname():String{
        return csname!!
    }

    fun getStandard():String{
        return csclass!!
    }

    fun getSec():String{
        return cssec!!
    }

    fun getText():String{
        return cmsg!!
    }

    fun getDateTime():String{
        return cclock!!
    }


    fun setTid(ctid:Int){
        this.ctid=ctid
    }

    fun setTname(ctname:String){
        this.ctname=ctname
    }

    fun setSchoolcode(ctschoolcode:String){
        this.ctschoolcode=ctschoolcode
    }

    fun setSid(csid:String){
        this.csid=csid
    }

    fun setSname(csname:String){
        this.csname=csname
    }

    fun setStandard(csclass:String){
        this.csclass=csclass
    }

    fun setSection(cssec:String){
        this.cssec=cssec
    }

    fun setText(cmsg:String){
        this.cmsg=cmsg
    }

    fun setPnumber(spnumber:String){
        this.spnumber=spnumber
    }

    fun setDate(cdate:String){
        this.cdate=cdate
    }

}