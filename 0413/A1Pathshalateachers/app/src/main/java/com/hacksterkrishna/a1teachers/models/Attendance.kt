package com.hacksterkrishna.a1teachers.models

/**
 * Created by krishna on 31/12/17.
 */
class Attendance{
    private var abschoolcode:String? = null
    private var abclass:String? = null
    private var absec:String? = null
    private var attendanceData:ArrayList<AttendanceData>?=null
    private var attendanceEditData:ArrayList<AttendanceEditData>?=null

    fun setSchoolcode(abschoolcode:String){
        this.abschoolcode=abschoolcode
    }

    fun setStandard(abclass:String){
        this.abclass=abclass
    }

    fun setSec(absec:String){
        this.absec=absec
    }

    fun setAttendanceData(attendanceData:ArrayList<AttendanceData>){
        this.attendanceData=attendanceData
    }

    fun setAttendanceEditData(attendanceEditData:ArrayList<AttendanceEditData>){
        this.attendanceEditData=attendanceEditData
    }

}