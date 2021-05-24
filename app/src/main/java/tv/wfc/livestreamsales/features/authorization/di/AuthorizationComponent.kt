package tv.wfc.livestreamsales.features.authorization.di

import dagger.Subcomponent
import tv.wfc.livestreamsales.features.authorization.di.modules.repository.LogInRepositoryModule
import tv.wfc.livestreamsales.features.authorization.di.modules.storage.LogInStorageModule
import tv.wfc.livestreamsales.features.authorization.di.modules.subcomponents.AuthorizationSubComponentsModule
import tv.wfc.livestreamsales.features.authorization.di.scope.AuthorizationFeatureScope
import tv.wfc.livestreamsales.features.authorization.paymentcardinformation.di.RegistrationPaymentCardInformationComponent
import tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.di.PhoneNumberConfirmationComponent
import tv.wfc.livestreamsales.features.authorization.phonenumberinput.di.PhoneNumberInputComponent
import tv.wfc.livestreamsales.features.authorization.userpersonalinformation.di.RegistrationUserPersonalInformationComponent

@AuthorizationFeatureScope
@Subcomponent(modules = [
    AuthorizationSubComponentsModule::class,
    LogInRepositoryModule::class,
    LogInStorageModule::class
])
interface AuthorizationComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthorizationComponent
    }

    fun phoneNumberInputComponent(): PhoneNumberInputComponent.Factory
    fun phoneNumberConfirmationComponent(): PhoneNumberConfirmationComponent.Factory
    fun registrationUserPersonalInformationComponent(): RegistrationUserPersonalInformationComponent.Factory
    fun registrationPaymentCardInformationComponent(): RegistrationPaymentCardInformationComponent.Factory
}