package com.ams.downloadfiles.presentation.activity

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ams.downloadfiles.Application
import com.ams.downloadfiles.R
import com.ams.downloadfiles.base.model.ResourceState
import com.ams.downloadfiles.base.utils.DownloadMediaWorkManager
import com.ams.downloadfiles.data.source.model.FileState
import com.ams.downloadfiles.presentation.adapter.FileAdapter
import com.ams.downloadfiles.presentation.viewmodel.FileViewModel
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fileAdapter: FileAdapter

    @Inject
    lateinit var fileViewModel: FileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        setContentView(R.layout.activity_main)
        requestPermissions()
        init()
    }

    @SuppressLint("CheckResult")
    private fun requestPermissions() {

        val rxPermissions = RxPermissions(this)

        rxPermissions.request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).subscribe { granted ->
            if (!granted) {
                // finish()
            }
        }
    }


    private fun init() {
        initRecyclerView()
        loadFiles()
        observeOnPostClickListener()
    }

    private fun initRecyclerView() {
        rcFiles.adapter = fileAdapter
        rcFiles.layoutManager = LinearLayoutManager(this)
    }

    private fun observeOnPostClickListener() {
        fileAdapter.onPostClickListener.observe(this, androidx.lifecycle.Observer {
            Toast.makeText(this, "Item Clicked", Toast.LENGTH_SHORT).show()
            downloadVideo(it.url)
        })
    }


    private fun downloadVideo(url: String) {
        fileViewModel.downloadFile(url)
        fileViewModel.downloadFileLiveData.observe(this, androidx.lifecycle.Observer {
            if (it.state == ResourceState.SUCCESS) {
                it.data?.first.let { fileState ->
                    when (fileState) {
                        FileState.DOWNLOADED -> {
                            Toast.makeText(this, "Downloaded", Toast.LENGTH_SHORT).show()
                        }
                        FileState.FAILED -> {
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // show progress
                            Log.i("State", it.data?.second.toString())
                        }
                    }

                }
            }
        })
    }

    private fun loadFiles() {

        fileViewModel.fileLiveData.observe(this,
            androidx.lifecycle.Observer {
                if (it.state == ResourceState.SUCCESS) {
                    fileAdapter.files = ArrayList(it.data)
                    fileAdapter.notifyDataSetChanged()
                    pbFiles.visibility = View.GONE
                } else if (it.state == ResourceState.LOADING) {
                    pbFiles.visibility = View.VISIBLE
                } else
                    pbFiles.visibility = View.GONE
            })

        fileViewModel.getFiles()
    }
}