package com.hacksterkrishna.a1teachers.models



/**
 * Created by krishna on 31/12/17.
 */

class ServerResponse{

    private val result: String? = null
    private val message: String? = null
    private val teacher: Teacher? = null
    private val acount: String? = null
    private val pcount: String? = null
    private val broadcastMessage: ArrayList<BroadcastMessage>? = null
    private val students: ArrayList<Student>? = null
    private val complaint: ArrayList<Complaint>? = null
    private val attendanceedit:ArrayList<AttendanceEditData>? = null
    private val attendanceview: ArrayList<AttendanceView>? = null
    private val homework: ArrayList<Homework>? = null

    fun getResult(): String {
        return result!!
    }

    fun getMessage(): String {
        return message!!
    }

    fun getTeacher(): Teacher {
        return teacher!!
    }

    fun getBroadcastMessage(): ArrayList<BroadcastMessage> {
        return broadcastMessage!!
    }

    fun getStudents():ArrayList<Student> {
        return students!!
    }

    fun getComplaints():ArrayList<Complaint> {
        return complaint!!
    }

    fun getAttendanceEdit():ArrayList<AttendanceEditData> {
        return attendanceedit!!
    }

    fun getAcount(): String {
        return acount!!
    }

    fun getPcount(): String {
        return pcount!!
    }

    fun getAttendanceView():ArrayList<AttendanceView> {
        return attendanceview!!
    }

    fun getHomework(): ArrayList<Homework> {
        return homework!!
    }

}