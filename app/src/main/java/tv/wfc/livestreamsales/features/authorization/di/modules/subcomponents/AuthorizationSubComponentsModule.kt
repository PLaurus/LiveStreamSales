package tv.wfc.livestreamsales.features.authorization.di.modules.subcomponents

import dagger.Module
import tv.wfc.livestreamsales.features.authorization.phonenumberconfirmation.di.PhoneNumberConfirmationComponent
import tv.wfc.livestreamsales.features.authorization.phonenumberinput.di.PhoneNumberInputComponent

@Module(subcomponents = [
    PhoneNumberInputComponent::class,
    PhoneNumberConfirmationComponent::class
])
class AuthorizationSubComponentsModule