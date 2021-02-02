package tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberinput.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberinput.qualifiers.PhoneNumberInputFragment
import tv.wfc.livestreamsales.di.scopes.FragmentScope
import tv.wfc.livestreamsales.viewmodels.ViewModelProviderFactory
import tv.wfc.livestreamsales.viewmodels.phonenumberinput.IPhoneNumberInputViewModel
import tv.wfc.livestreamsales.viewmodels.phonenumberinput.PhoneNumberInputViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.di.mapkeys.ViewModelKey

@Module
abstract class PhoneNumberInputViewModelModule {
    companion object{
        @FragmentScope
        @Provides
        @JvmStatic
        fun provideIPhoneNumberInputViewModel(
            @PhoneNumberInputFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IPhoneNumberInputViewModel{
            return ViewModelProvider(
                fragment,
                viewModelProviderFactory
            )[PhoneNumberInputViewModel::class.java]
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(PhoneNumberInputViewModel::class)
    abstract fun bindPhoneNumberInputViewModelIntoMap(
        phoneNumberInputViewModel: PhoneNumberInputViewModel
    ): ViewModel
}