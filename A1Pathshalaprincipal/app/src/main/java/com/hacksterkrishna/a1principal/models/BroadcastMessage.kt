package com.hacksterkrishna.a1principal.models


class BroadcastMessage{

    private var brdpname:String?=null
    private var brdtext:String?=null
    private var pushed_at:String?=null


    fun getPname():String{
        return brdpname!!
    }

    fun getText():String{
        return brdtext!!
    }


    fun getDate():String{
        return pushed_at!!
    }


    fun setPname(brdpname:String){
        this.brdpname=brdpname
    }

    fun setText(brdtext:String){
        this.brdtext=brdtext
    }

    fun setDate(pushed_at:String){
        this.pushed_at=pushed_at
    }
}