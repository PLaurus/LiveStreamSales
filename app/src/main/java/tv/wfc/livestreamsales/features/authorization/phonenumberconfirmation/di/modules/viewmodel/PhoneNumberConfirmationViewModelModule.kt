package tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.di.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.di.qualifiers.PhoneNumberConfirmationFragment
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.di.scope.PhoneNumberConfirmationFeatureScope
import tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.viewmodel.IPhoneNumberConfirmationViewModel
import tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.viewmodel.PhoneNumberConfirmationViewModel

@Module
abstract class PhoneNumberConfirmationViewModelModule {
    companion object{
        @PhoneNumberConfirmationFeatureScope
        @Provides
        internal fun provideIPhoneNumberConfirmationViewModel(
            @PhoneNumberConfirmationFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IPhoneNumberConfirmationViewModel {
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[PhoneNumberConfirmationViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(PhoneNumberConfirmationViewModel::class)
    internal abstract fun bindPhoneNumberConfirmationViewModelIntoMap(
        phoneNumberConfirmationViewModel: PhoneNumberConfirmationViewModel
    ): ViewModel
}