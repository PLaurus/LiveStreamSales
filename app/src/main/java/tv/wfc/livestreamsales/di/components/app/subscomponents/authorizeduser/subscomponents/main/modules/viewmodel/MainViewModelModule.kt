package tv.wfc.livestreamsales.di.components.app.subscomponents.authorizeduser.subscomponents.main.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tv.wfc.livestreamsales.di.scopes.ActivityScope
import tv.wfc.livestreamsales.viewmodels.main.IMainViewModel
import tv.wfc.livestreamsales.viewmodels.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.di.mapkeys.ViewModelKey

@Module
abstract class MainViewModelModule {
    companion object{
        @ActivityScope
        @Provides
        @JvmStatic
        internal fun provideIMainViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IMainViewModel {
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