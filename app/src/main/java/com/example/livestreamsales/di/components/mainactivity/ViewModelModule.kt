package com.example.livestreamsales.di.components.mainactivity

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.livestreamsales.di.scopes.ActivityScope
import com.example.livestreamsales.viewmodels.IMainViewModel
import com.example.livestreamsales.viewmodels.MainViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ViewModelModule {

    @ActivityScope
    @Provides
    fun provideMainViewModel(
        viewModelProviderFactory: ViewModelProvider.Factory,
        @Named(MainActivityComponent.DEPENDENCY_NAME_MAIN_VIEW_MODEL_STORE_OWNER)
        viewModelStoreOwner: ViewModelStoreOwner
    ): IMainViewModel{
        return ViewModelProvider(viewModelStoreOwner, viewModelProviderFactory)[MainViewModel::class.java]
    }
}