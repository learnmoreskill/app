package com.hacksterkrishna.a1principal.models


class Principal{

    private var id:String?=null
    private var name:String?=null
    private var email:String?=null
    private var password:String?=null
    private var school:String?=null

    fun getId():String{

        return id!!
    }

    fun getName():String{

        return name!!
    }

    fun getEmail():String{

        return email!!
    }

    fun getSchool():String{

        return school!!
    }

    fun setName(name:String){

        this.name=name
    }

    fun setId(id:String){

        this.id=id
    }

    fun setEmail(email:String){

        this.email=email
    }

    fun setPassword(password:String){

        this.password=password
    }

    fun setSchool(school:String){

        this.school=school
    }

}