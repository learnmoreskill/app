package com.hacksterkrishna.a1principal.models



class ServerResponse{

    private val result: String? = null
    private val message: String? = null
    private val acount: String? = null
    private val pcount: String? = null
    private val principal: Principal? = null
    private val homework: ArrayList<Homework>? = null
    private val broadcast: ArrayList<Broadcast>? = null
    private val msg: ArrayList<Message>? = null
    private val students: ArrayList<Student>? = null
    private val attendance: ArrayList<Attendance>? = null
    private val attendancelog: ArrayList<AttendanceLog>? = null
    private val homeworknd: ArrayList<HomeworkNotDone>? = null
    private val broadcastMessage: ArrayList<BroadcastMessage>? = null

    fun getResult(): String {
        return result!!
    }

    fun getMessage(): String {
        return message!!
    }

    fun getAcount(): String {
        return acount!!
    }

    fun getPcount(): String {
        return pcount!!
    }

    fun getPrincipal(): Principal {
        return principal!!
    }

    fun getHomework(): ArrayList<Homework> {
        return homework!!
    }

    fun getBroadcast(): ArrayList<Broadcast> {
        return broadcast!!
    }

    fun getMsg(): ArrayList<Message> {
        return msg!!
    }

    fun getStudents():ArrayList<Student> {
        return students!!
    }

    fun getAttendance():ArrayList<Attendance> {
        return attendance!!
    }

    fun getAttendanceLog():ArrayList<AttendanceLog> {
        return attendancelog!!
    }

    fun getHomeworkNotDone():ArrayList<HomeworkNotDone> {
        return homeworknd!!
    }

    fun getBroadcastMessage(): ArrayList<BroadcastMessage> {
        return broadcastMessage!!
    }
}