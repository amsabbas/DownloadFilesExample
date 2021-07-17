package com.ams.downloadfiles.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.ams.downloadfiles.base.utils.DownloadMediaWorkManager
import com.ams.downloadfiles.data.source.model.File
import com.ams.downloadfiles.data.source.model.FileState
import com.ams.downloadfiles.data.source.remote.FileRemoteDataSource
import com.ams.downloadfiles.domain.repository.FileRepository
import io.reactivex.Observable
import javax.inject.Inject

class FileRepositoryImpl constructor(
    private val fileRemoteDataSource: FileRemoteDataSource,
    private val context: Context
) : FileRepository {

    lateinit var workManager: WorkManager
    lateinit var downloadMediaWorkManager: OneTimeWorkRequest

    override fun getFiles(): Observable<List<File>> {
        return fileRemoteDataSource.getFiles();
    }

    override fun downloadFile(url: String): Observable<Pair<FileState, Int>> {
        return Observable.create {

            val data = workDataOf(DownloadMediaWorkManager.WORK_DATA_MEDIA_URL to url)
            downloadMediaWorkManager = OneTimeWorkRequestBuilder<DownloadMediaWorkManager>()
                .setInputData(data)
                .addTag(url)
                .build()

            workManager = WorkManager.getInstance(context)
            workManager.enqueue(downloadMediaWorkManager)
            workManager.getWorkInfoByIdLiveData(downloadMediaWorkManager.id)
                .observeForever { workInfo ->
                    if (workInfo != null) {
                        when {
                            workInfo.state == WorkInfo.State.SUCCEEDED -> {

                                it.onNext(Pair(FileState.DOWNLOADED, 100))
                            }
                            workInfo.state == WorkInfo.State.FAILED || workInfo.state == WorkInfo.State.CANCELLED ||
                                    workInfo.state == WorkInfo.State.BLOCKED -> {
                                it.onNext(Pair(FileState.FAILED, 0))
                            }
                            else -> {
                                if (workInfo.progress.keyValueMap.containsKey(
                                        DownloadMediaWorkManager.WORK_IN_PROGRESS
                                    )
                                ) {
                                    val progress = workInfo.progress.keyValueMap.get(
                                        DownloadMediaWorkManager.WORK_IN_PROGRESS
                                    )
                                    it.onNext(
                                        Pair(
                                            FileState.IN_PROGRESS,
                                            progress.toString().toInt()
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }
}