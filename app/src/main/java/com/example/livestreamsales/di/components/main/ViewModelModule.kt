package com.example.livestreamsales.di.components.main

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.viewmodels.IMainViewModel
import com.example.livestreamsales.viewmodels.MainViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @ActivityScope
    @Provides
    fun provideMainViewModel(
        viewModelProviderFactory: ViewModelProvider.Factory,
        activity: AppCompatActivity
    ): IMainViewModel{
        return ViewModelProvider(activity, viewModelProviderFactory)[MainViewModel::class.java]
    }
}