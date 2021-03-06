package com.ams.downloadfiles.base.extension

import androidx.lifecycle.MutableLiveData
import com.ams.downloadfiles.base.model.Resource
import com.ams.downloadfiles.base.model.ResourceState


fun <T> MutableLiveData<Resource<T>>.setSuccess(data: T) =
    postValue(
        Resource(
            ResourceState.SUCCESS,
            data
        )
    )


fun <T> MutableLiveData<Resource<T>>.setLoading() =
    postValue(
        Resource(
            ResourceState.LOADING,
            value?.data
        )
    )

fun <T> MutableLiveData<Resource<T>>.setError(message: String? = null) =
    postValue(
        Resource(
            ResourceState.ERROR,
            value?.data,
            message
        )
    )
