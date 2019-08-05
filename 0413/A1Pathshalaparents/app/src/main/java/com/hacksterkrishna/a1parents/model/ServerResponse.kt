package com.hacksterkrishna.a1parents.model


/**
 * Created by krishna on 31/12/17.
 */

data class ServerResponse(var result: String?,var message: String?,var parent: Parent?,var attendance: ArrayList<Attendance>?,val msg: ArrayList<Message>?,val acount:Int?,val pcount:Int?,val classBroadcast: ArrayList<ClassBroadcast>?,val schoolBroadcast: ArrayList<SchoolBroadcast>,val homework: ArrayList<Homework>)