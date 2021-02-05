package tv.wfc.livestreamsales.features.splash.di.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.splash.di.scope.SplashFeatureScope
import tv.wfc.livestreamsales.features.splash.viewmodel.ISplashViewModel
import tv.wfc.livestreamsales.features.splash.viewmodel.SplashViewModel

@Module
abstract class SplashViewModelModule {
    companion object{
        @SplashFeatureScope
        @Provides
        @JvmStatic
        internal fun provideISplashViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): ISplashViewModel {
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