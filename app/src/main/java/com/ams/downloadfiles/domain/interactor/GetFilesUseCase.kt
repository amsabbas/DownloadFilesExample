package com.ams.downloadfiles.domain.interactor

import com.ams.downloadfiles.data.source.model.File
import com.ams.downloadfiles.data.source.model.FileState
import com.ams.downloadfiles.domain.repository.FileRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetFilesUseCase @Inject constructor(private val repository: FileRepository) {
    fun getFiles(): Observable<List<File>> {
        return repository.getFiles()
    }

    fun downloadFile(url: String): Observable<Pair<FileState, Int>> {
        return repository.downloadFile(url)
    }

}