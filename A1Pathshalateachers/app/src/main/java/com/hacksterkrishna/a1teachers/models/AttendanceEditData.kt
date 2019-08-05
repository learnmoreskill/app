package com.hacksterkrishna.a1teachers.models

/**
 * Created by krishna on 31/12/17.
 */
class AttendanceEditData{

    var aid:String? = null
    var asid:String? = null
    var asroll:String? = null
    var asname:String? = null
    var astatus:String? = null
    var aschoolcode:String? = null
    var aclass:String? = null
    var asec:String? = null
    var aclock:String? = null

    constructor(aid:String,asid:String,asroll:String,asname:String,astatus:String,aschoolcode:String,aclass:String,asec:String,aclock:String){

        this.aid=aid
        this.asid=asid
        this.asroll=asroll
        this.asname=asname
        this.astatus=astatus
        this.aschoolcode=aschoolcode
        this.aclass=aclass
        this.asec=asec
        this.aclock=aclock

    }

}