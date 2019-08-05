package com.hacksterkrishna.a1principal.models


class HomeworkNotDone{

    private var hwndsub:String? = null
    private var hwnddate:String? = null

    fun getSub():String{
        return hwndsub!!
    }

    fun getDate():String{
        return hwnddate!!
    }

}