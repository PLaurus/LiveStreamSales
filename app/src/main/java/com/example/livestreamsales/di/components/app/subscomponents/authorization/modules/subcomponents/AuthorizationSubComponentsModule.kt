package com.example.livestreamsales.di.components.app.subscomponents.authorization.modules.subcomponents

import com.example.livestreamsales.di.components.app.subscomponents.authorization.subcomponents.phonenumberconfirmation.PhoneNumberConfirmationComponent
import com.example.livestreamsales.di.components.app.subscomponents.authorization.subcomponents.phonenumberinput.PhoneNumberInputComponent
import dagger.Module

@Module(subcomponents = [
    PhoneNumberInputComponent::class,
    PhoneNumberConfirmationComponent::class
])
class AuthorizationSubComponentsModule