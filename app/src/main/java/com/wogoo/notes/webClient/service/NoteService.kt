package com.wogoo.notes.webClient.service

import com.wogoo.notes.webClient.dto.NoteRequest
import com.wogoo.notes.webClient.dto.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NoteService {

    @GET("notes")
    suspend fun findAll() : List<NoteResponse>

    @PUT("notas/{id}")
    suspend fun save(@Path("id") id: String, @Body note: NoteRequest) : Response<NoteResponse>

    @DELETE("notas/{id}")
    suspend fun remove(@Path("id") id: String) : Response<Void>

}