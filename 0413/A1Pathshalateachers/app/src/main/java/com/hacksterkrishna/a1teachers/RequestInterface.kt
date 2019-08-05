package com.hacksterkrishna.a1teachers

import com.hacksterkrishna.a1teachers.models.ServerRequest
import com.hacksterkrishna.a1teachers.models.ServerResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by krishna on 31/12/17.
 */

interface RequestInterface{

    @POST("teacherserver_api/")
    fun operation(@Body request: ServerRequest): Observable<ServerResponse>
}