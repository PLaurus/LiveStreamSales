package tv.wfc.livestreamsales.features.usersettings.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.usersettings.di.scope.ProfileFeature
import tv.wfc.livestreamsales.features.usersettings.viewmodel.IProfileViewModel
import tv.wfc.livestreamsales.features.usersettings.viewmodel.ProfileViewModel

@Module
abstract class ProfileViewModelModule {
    companion object{
        @ProfileFeature
        @Provides
        fun provideIProfileViewModel(
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IProfileViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[ProfileViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModelIntoMap(
        userSettingsViewModel: ProfileViewModel
    ): ViewModel
}