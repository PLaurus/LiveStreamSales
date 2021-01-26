package com.example.livestreamsales.di.components.main.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.viewmodels.IMainViewModel
import com.example.livestreamsales.viewmodels.MainViewModel
import dagger.Module
import dagger.Provides

@Module
class MainViewModelModule {

    @ActivityScope
    @Provides
    fun provideMainViewModel(
        activity: AppCompatActivity,
        viewModelProviderFactory: ViewModelProvider.Factory
    ): IMainViewModel{
        return ViewModelProvider(activity, viewModelProviderFactory)[MainViewModel::class.java]
    }
}