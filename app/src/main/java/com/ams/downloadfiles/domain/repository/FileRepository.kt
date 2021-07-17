package com.ams.downloadfiles.domain.repository

import com.ams.downloadfiles.data.source.model.File
import com.ams.downloadfiles.data.source.model.FileState
import io.reactivex.Observable

interface FileRepository {
    fun getFiles(): Observable<List<File>>
    fun downloadFile(url: String): Observable<Pair<FileState, Int>>
}
