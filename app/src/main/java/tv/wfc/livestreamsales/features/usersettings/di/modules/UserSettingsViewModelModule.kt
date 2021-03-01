package tv.wfc.livestreamsales.features.usersettings.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.usersettings.di.scope.UserSettingsFeature
import tv.wfc.livestreamsales.features.usersettings.viewmodel.IUserSettingsViewModel
import tv.wfc.livestreamsales.features.usersettings.viewmodel.UserSettingsViewModel

@Module
abstract class UserSettingsViewModelModule {
    companion object{
        @UserSettingsFeature
        @Provides
        @JvmStatic
        fun provideISettingsViewModel(
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IUserSettingsViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[UserSettingsViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(UserSettingsViewModel::class)
    abstract fun bindSettingsViewModelIntoMap(
        userSettingsViewModel: UserSettingsViewModel
    ): ViewModel
}