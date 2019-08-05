package com.hacksterkrishna.a1parents

import android.os.AsyncTask
import com.google.firebase.iid.FirebaseInstanceId
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.IOException

/**
 * Created by krishna on 31/12/17.
 */

class DropToken : AsyncTask<Void, Void, Void>(),AnkoLogger {
    override fun doInBackground(vararg params: Void): Void? {
        run {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    override fun onPostExecute(result: Void?) {
        info("Token Dropped")
        info("New Token:"+FirebaseInstanceId.getInstance().token)
    }
}