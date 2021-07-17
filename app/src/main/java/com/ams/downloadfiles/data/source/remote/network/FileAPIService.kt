package com.ams.downloadfiles.data.source.remote.network

import com.ams.downloadfiles.data.source.model.File
import io.reactivex.Observable
import retrofit2.http.GET

interface FileAPIService {

    @GET("v3/0ef1cf29-dd3c-4f4c-bcdd-c793856d9a6e")
    fun getFiles(): Observable<List<File>>

}