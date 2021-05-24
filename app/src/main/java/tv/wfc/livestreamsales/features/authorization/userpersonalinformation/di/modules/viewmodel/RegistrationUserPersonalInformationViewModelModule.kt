package tv.wfc.livestreamsales.features.authorization.userpersonalinformation.di.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.authorization.userpersonalinformation.di.qualifiers.RegistrationUserPersonalInformationFragment
import tv.wfc.livestreamsales.features.authorization.userpersonalinformation.di.scope.RegistrationUserPersonalInformationFeatureScope
import tv.wfc.livestreamsales.features.authorization.userpersonalinformation.viewmodel.IRegistrationUserPersonalInformationViewModel
import tv.wfc.livestreamsales.features.authorization.userpersonalinformation.viewmodel.RegistrationUserPersonalInformationViewModel

@Module
abstract class RegistrationUserPersonalInformationViewModelModule {
    companion object{
        @RegistrationUserPersonalInformationFeatureScope
        @Provides
        fun provideIRegistrationUserPersonalInformationViewModel(
            @RegistrationUserPersonalInformationFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IRegistrationUserPersonalInformationViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[RegistrationUserPersonalInformationViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationUserPersonalInformationViewModel::class)
    abstract fun bindRegistrationUserPersonalInformationViewModelIntoMap(
        registrationUserPersonalInformationViewModel: RegistrationUserPersonalInformationViewModel
    ): ViewModel
}