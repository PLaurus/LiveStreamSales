package tv.wfc.livestreamsales.di.components.app.subscomponents.login.modules.subcomponents

import tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberconfirmation.PhoneNumberConfirmationComponent
import tv.wfc.livestreamsales.di.components.app.subscomponents.login.subcomponents.phonenumberinput.PhoneNumberInputComponent
import dagger.Module

@Module(subcomponents = [
    PhoneNumberInputComponent::class,
    PhoneNumberConfirmationComponent::class
])
class LogInSubComponentsModule