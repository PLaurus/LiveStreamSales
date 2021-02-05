package tv.wfc.livestreamsales.features.login.di.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tv.wfc.livestreamsales.features.login.viewmodel.ILogInViewModel
import tv.wfc.livestreamsales.features.login.viewmodel.LogInViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.login.di.scope.LogInFeatureScope

@Module
abstract class LogInViewModelModule {
    companion object{
        @LogInFeatureScope
        @Provides
        @JvmStatic
        internal fun provideILogInViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): ILogInViewModel {
            return ViewModelProvider(
                activity,
                viewModelProviderFactory
            )[LogInViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(LogInViewModel::class)
    internal abstract fun bindLogInViewModelIntoMap(
        logInViewModel: LogInViewModel
    ): ViewModel
}