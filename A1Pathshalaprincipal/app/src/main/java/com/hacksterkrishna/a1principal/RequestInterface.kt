package com.hacksterkrishna.a1principal

import com.hacksterkrishna.a1principal.models.ServerRequest
import com.hacksterkrishna.a1principal.models.ServerResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
/**
 * Created by krishna on 31/12/17.
 */

interface RequestInterface{

    @POST("principalserver_api/")
    fun operation(@Body request: ServerRequest): Observable<ServerResponse>
}