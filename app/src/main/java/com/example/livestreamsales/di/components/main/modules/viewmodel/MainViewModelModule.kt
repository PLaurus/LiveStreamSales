package com.example.livestreamsales.di.components.main.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.livestreamsales.di.mapkeys.ViewModelKey
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.viewmodels.IMainViewModel
import com.example.livestreamsales.viewmodels.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {
    companion object{
        @ActivityScope
        @Provides
        @JvmStatic
        internal fun provideIMainViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IMainViewModel{
            return ViewModelProvider(
                activity,
                viewModelProviderFactory
            )[MainViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModelIntoMap(
        mainViewModel: MainViewModel
    ): ViewModel
}