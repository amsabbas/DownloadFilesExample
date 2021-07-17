package com.ams.downloadfiles.presentation.di

import android.content.Context
import com.ams.downloadfiles.base.utils.ServiceGenerator
import com.ams.downloadfiles.data.repository.FileRepositoryImpl
import com.ams.downloadfiles.data.source.remote.FileRemoteDataSource
import com.ams.downloadfiles.data.source.remote.network.FileAPIService
import com.ams.downloadfiles.domain.repository.FileRepository
import dagger.Module
import dagger.Provides

@Module
class FileModule {

    @Provides
    fun provideFileServiceAPIs(): FileAPIService =
        ServiceGenerator().createService(FileAPIService::class.java)

    @Provides
    fun provideFilesRepository(
        remoteDataSource: FileRemoteDataSource,
        context: Context
    ): FileRepository {
        return FileRepositoryImpl(
            remoteDataSource, context
        )
    }


}
