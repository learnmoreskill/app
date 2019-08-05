package com.hacksterkrishna.a1parents

import com.hacksterkrishna.a1parents.model.ServerRequest
import com.hacksterkrishna.a1parents.model.ServerResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by krishna on 31/12/17.
 */

interface RequestInterface{

    @POST("parentserver_api/")
    fun operation(@Body request: ServerRequest): Observable<ServerResponse>
}