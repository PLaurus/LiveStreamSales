package tv.wfc.livestreamsales.features.phonenumberinput.di.modules.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tv.wfc.livestreamsales.features.phonenumberinput.di.qualifiers.PhoneNumberInputFragment
import tv.wfc.livestreamsales.features.phonenumberinput.viewmodel.IPhoneNumberInputViewModel
import tv.wfc.livestreamsales.features.phonenumberinput.viewmodel.PhoneNumberInputViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.wfc.livestreamsales.application.di.modules.viewmodelprovider.mapkeys.ViewModelKey
import tv.wfc.livestreamsales.features.phonenumberinput.di.scope.PhoneNumberInputFeatureScope

@Module
abstract class PhoneNumberInputViewModelModule {
    companion object{
        @PhoneNumberInputFeatureScope
        @Provides
        @JvmStatic
        fun provideIPhoneNumberInputViewModel(
            @PhoneNumberInputFragment
            fragment: Fragment,
            viewModelProviderFactory: ViewModelProvider.Factory
        ): IPhoneNumberInputViewModel {
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