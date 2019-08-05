package com.hacksterkrishna.a1teachers.models

/**
 * Created by krishna on 31/12/17.
 */
class Teacher {

    private var id:Int?=null
    private var name:String?=null
    private var email:String?=null
    private var address:String?=null
    private var password:String?=null
    private var school:String?=null
    private var schoolCode:String?=null
    private var standard:String?=null
    private var sec:String?=null

    fun getId():Int{

        return id!!
    }

    fun getName():String{

        return name!!
    }

    fun getEmail():String{

        return email!!
    }

    fun getAddress():String{

        return address!!
    }

    fun getSchool():String{

        return school!!
    }

    fun getSchoolCode():String{

        return schoolCode!!
    }

    fun getStandard():String{

        return standard!!
    }

    fun getSec():String{

        return sec!!
    }

    fun setStandard(standard:String){
        this.standard=standard
    }

    fun setSec(sec:String){
        this.sec=sec
    }

    fun setEmail(email:String){

        this.email=email
    }

    fun setPassword(password:String){

        this.password=password
    }



}