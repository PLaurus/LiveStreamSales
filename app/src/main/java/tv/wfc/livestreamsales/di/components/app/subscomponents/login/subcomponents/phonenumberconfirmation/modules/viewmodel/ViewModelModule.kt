package tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberconfirmation.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberconfirmation.qualifiers.PhoneNumberConfirmationFragment
import tv.wfc.livestreamsales.di.scopes.FragmentScope
import tv.wfc.livestreamsales.viewmodels.ViewModelProviderFactory
import tv.wfc.livestreamsales.viewmodels.phonenumberconfirmation.IPhoneNumberConfirmationViewModel
import tv.wfc.livestreamsales.viewmodels.phonenumberconfirmation.PhoneNumberConfirmationViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.di.mapkeys.ViewModelKey

@Module
abstract class ViewModelModule {
    companion object{
        @FragmentScope
        @Provides
        @JvmStatic
        fun provideIPhoneNumberConfirmationViewModel(
            @PhoneNumberConfirmationFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProviderFactory
        ): IPhoneNumberConfirmationViewModel{
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[PhoneNumberConfirmationViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(PhoneNumberConfirmationViewModel::class)
    abstract fun bindPhoneNumberConfirmationViewModelIntoMap(
        phoneNumberConfirmationViewModel: PhoneNumberConfirmationViewModel
    ): ViewModel
}