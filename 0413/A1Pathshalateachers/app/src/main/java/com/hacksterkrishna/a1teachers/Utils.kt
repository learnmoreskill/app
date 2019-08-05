package com.hacksterkrishna.a1teachers


import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by krishna on 31/12/17.
 */
class Utils{

    fun prettifyDate(uglydate:String):String{

        val parser = SimpleDateFormat("yyyy-MM-dd")
        val date = parser.parse(uglydate) as Date

        return DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH).format(date)
    }

    fun prettifyDateTime(uglydatetime:String):String{

        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = parser.parse(uglydatetime) as Date

        return DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.SHORT, Locale.ENGLISH).format(date)
    }

    fun getStandardValue(standard:String):String{
        return when(standard){
            "Nursery"-> "Nursery"
            "LKG"-> "LKG"
            "UKG"-> "UKG"
            else-> standard
        }
    }

    fun getStandardName(standard:String):String{
        return when(standard){
            "Nursery"-> "Nursery"
            "LKG"-> "LKG"
            "UKG"-> "UKG"
            else-> standard
        }
    }

}