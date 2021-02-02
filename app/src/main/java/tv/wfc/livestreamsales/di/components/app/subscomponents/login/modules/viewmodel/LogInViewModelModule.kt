package tv.wfc.livestreamsales.di.components.app.subscomponents.login.modules.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tv.wfc.livestreamsales.di.scopes.ActivityScope
import tv.wfc.livestreamsales.viewmodels.ViewModelProviderFactory
import tv.wfc.livestreamsales.viewmodels.login.ILogInViewModel
import tv.wfc.livestreamsales.viewmodels.login.LogInViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.di.mapkeys.ViewModelKey

@Module
abstract class LogInViewModelModule {
    companion object{
        @ActivityScope
        @Provides
        @JvmStatic
        internal fun provideILogInViewModel(
            activity: AppCompatActivity,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): ILogInViewModel{
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