package com.hacksterkrishna.a1teachers.models

/**
 * Created by krishna on 31/12/17.
 */
class Student{

    private var sid:String?=null
    private var sroll:String?=null
    private var sname:String?=null
    private var saddress:String?=null
    private var semail:String?=null
    private var sschoolcode:String?=null
    private var spname:String?=null
    private var spnumber:String?=null
    private var spemail:String?=null
    private var sclass:String?=null
    private var ssec:String?=null

    fun setSid(sid:String){
        this.sid=sid
    }

    fun setSname(sname:String){
        this.sname=sname

    }

    fun getSid():String{
        return sid!!
    }

    fun getSroll():String{
        return sroll!!
    }

    fun getSname():String{
        return sname!!
    }

    fun getSaddress():String{
        return saddress!!
    }

    fun getSemail():String{
        return semail!!
    }

    fun getSchoolCode():String{
        return sschoolcode!!
    }

    fun getSpname():String{
        return spname!!
    }

    fun getSpnumber():String{
        return spnumber!!
    }

    fun getSpemail():String{
        return spemail!!
    }

    fun getSclass():String{
        return sclass!!
    }

    fun getSsec():String{
        return ssec!!
    }
}