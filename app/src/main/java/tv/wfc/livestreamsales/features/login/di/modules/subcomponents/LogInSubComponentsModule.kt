package tv.wfc.livestreamsales.features.login.di.modules.subcomponents

import dagger.Module
import tv.wfc.livestreamsales.features.phonenumberconfirmation.di.PhoneNumberConfirmationComponent
import tv.wfc.livestreamsales.features.phonenumberinput.di.PhoneNumberInputComponent

@Module(subcomponents = [
    PhoneNumberInputComponent::class,
    PhoneNumberConfirmationComponent::class
])
class LogInSubComponentsModule