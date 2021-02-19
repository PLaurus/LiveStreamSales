package tv.wfc.livestreamsales.features.settings.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.settings.di.scope.SettingsFeature
import tv.wfc.livestreamsales.features.settings.viewmodel.ISettingsViewModel
import tv.wfc.livestreamsales.features.settings.viewmodel.SettingsViewModel

@Module
abstract class SettingsViewModelModule {
    companion object{
        @SettingsFeature
        @Provides
        @JvmStatic
        fun provideISettingsViewModel(
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): ISettingsViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[SettingsViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModelIntoMap(
        settingsViewModel: SettingsViewModel
    ): ViewModel
}