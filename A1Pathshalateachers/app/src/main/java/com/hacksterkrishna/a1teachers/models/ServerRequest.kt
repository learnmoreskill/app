package com.hacksterkrishna.a1teachers.models


/**
 * Created by krishna on 31/12/17.
 */

class ServerRequest{

    private var operation:String?=null
    private var teacher: Teacher?=null
    private var broadcastMessage:BroadcastMessage?=null
    private var student:Student?=null
    private var complaint:Complaint?=null
    private var attendanceCheck:AttendanceCheck?=null
    private var attendance:Attendance?=null
    private var attendanceview:AttendanceView?=null
    private var homework:Homework?=null
    private var fetchType:Int?= null
    private var hwnd:ArrayList<HomeworkNotDone>? = null
    private var feenp:ArrayList<FeeNotPaid>? = null
    private var grpc:ArrayList<GroupComplaint>? = null

    fun setOperation(operation:String){
        this.operation=operation
    }

    fun setTeacher(teacher: Teacher){
        this.teacher = teacher
    }

    fun setBroadcastMessage(broadcastMessage: BroadcastMessage){
        this.broadcastMessage=broadcastMessage
    }

    fun setStudent(student: Student){
        this.student=student
    }

    fun setComplaint(complaint: Complaint){
        this.complaint=complaint
    }

    fun setAttendanceCheck(attendanceCheck: AttendanceCheck){
        this.attendanceCheck=attendanceCheck
    }

    fun setAttendance(attendance:Attendance){
        this.attendance=attendance
    }

    fun setAttendanceView(attendanceview: AttendanceView){
        this.attendanceview=attendanceview
    }

    fun setHomework(homework: Homework){
        this.homework=homework
    }

    fun setFetchType(fetchType:Int){
        this.fetchType=fetchType
    }

    fun setHwnd(hwnd:ArrayList<HomeworkNotDone>){
        this.hwnd=hwnd
    }

    fun setFeenp(feenp:ArrayList<FeeNotPaid>){
        this.feenp=feenp
    }

    fun setGrpc(grpc:ArrayList<GroupComplaint>){
        this.grpc=grpc
    }

}