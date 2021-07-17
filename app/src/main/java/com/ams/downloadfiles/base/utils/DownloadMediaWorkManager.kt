package com.ams.downloadfiles.base.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.ams.downloadfiles.R
import kotlinx.coroutines.delay
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class DownloadMediaWorkManager(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORK_IN_PROGRESS = "WORK_IN_PROGRESS"
        const val WORK_DATA_MEDIA_URL = "MEDIA_URL"

    }

    override suspend fun doWork(): Result {

        val firstUpdate = workDataOf(WORK_IN_PROGRESS to 0)
        val url = inputData.getString(WORK_DATA_MEDIA_URL)

        try {
            var count = 0

            val file = File(
                getRootFile().path,
                "VIDEO_${System.currentTimeMillis()}.mp4"
            )

            val url = URL(url)
            val connnection = url.openConnection()
            connnection.connect()

            var fileLength = connnection.contentLength

            var input = BufferedInputStream(url.openStream())
            var output = FileOutputStream(file)
            val data = ByteArray(1024)
            var total = 0
            setProgress(firstUpdate)
            while (input.read(data).also { count = it } != -1) {

                total += count
                var p = (total.toDouble() / fileLength) * 100
                setProgress(workDataOf(WORK_IN_PROGRESS to p.toInt()))

                output.write(data, 0, count)
            }


            output.flush()
            output.close()
            input.close()

            setProgress(workDataOf(WORK_IN_PROGRESS to 100))

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return Result.success()

    }

    private fun getRootFile(): File {
        val folderName = applicationContext.getString(R.string.app_name)
        val rootDir = File(applicationContext.getExternalFilesDir(null), folderName)


        if (!rootDir.exists()) {
            rootDir.mkdir()
        }

        val dir = File("$rootDir")

        if (!dir.exists()) {
            dir.mkdir()
        }
        return File(dir.absolutePath)
    }
}