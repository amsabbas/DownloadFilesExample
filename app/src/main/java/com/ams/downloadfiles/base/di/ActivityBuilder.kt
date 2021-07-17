package com.ams.downloadfiles.base.di

import com.ams.downloadfiles.presentation.activity.MainActivity
import com.ams.downloadfiles.presentation.di.FileModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [FileModule::class])
    abstract fun bindMainActivity(): MainActivity
}