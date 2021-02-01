package tv.wfc.livestreamsales.di.components.app.subscomponents.splash.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tv.wfc.livestreamsales.di.scopes.ActivityScope
import tv.wfc.livestreamsales.viewmodels.ViewModelProviderFactory
import tv.wfc.livestreamsales.viewmodels.splash.ISplashViewModel
import tv.wfc.livestreamsales.viewmodels.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.di.mapkeys.ViewModelKey

@Module
abstract class SplashViewModelModule {
    companion object{
        @ActivityScope
        @Provides
        @JvmStatic
        internal fun provideISplashViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProviderFactory
        ): ISplashViewModel{
            return ViewModelProvider(activity, viewModelProviderFactory)[SplashViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun bindSplashViewModelIntoMap(
        splashViewModel: SplashViewModel
    ): ViewModel
}