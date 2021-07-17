package com.ams.downloadfiles.presentation.viewmodel


import androidx.lifecycle.MutableLiveData
import com.ams.downloadfiles.base.extension.setError
import com.ams.downloadfiles.base.extension.setLoading
import com.ams.downloadfiles.base.extension.setSuccess
import com.ams.downloadfiles.base.model.Resource
import com.ams.downloadfiles.base.viewmodel.BaseViewModel
import com.ams.downloadfiles.data.source.model.File
import com.ams.downloadfiles.data.source.model.FileState
import com.ams.downloadfiles.domain.interactor.GetFilesUseCase
import dagger.internal.DoubleCheck.lazy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FileViewModel @Inject constructor(
    private val getFilesUseCase: GetFilesUseCase
) : BaseViewModel() {

    val fileLiveData: MutableLiveData<Resource<List<File>>> = MutableLiveData()

    val downloadFileLiveData: MutableLiveData<Resource<Pair<FileState,Int>>> = MutableLiveData()

    fun getFiles() {
        addDisposable(
            getFilesUseCase.getFiles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { fileLiveData.setLoading() }
                .subscribe({
                    fileLiveData.setSuccess(it)
                }, {
                    fileLiveData.setError(it.message)
                })
        )

    }

    fun downloadFile(url: String) {
        addDisposable(
            getFilesUseCase.downloadFile(url)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { downloadFileLiveData.setLoading() }
                .subscribe({
                    downloadFileLiveData.setSuccess(it)
                }, {
                    downloadFileLiveData.setError(it.message)
                })
        )
    }
}