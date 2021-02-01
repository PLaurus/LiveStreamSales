package com.example.livestreamsales.di.components.authorization.modules.subcomponents

import com.example.livestreamsales.di.components.phonenumberconfirmation.PhoneNumberConfirmationComponent
import com.example.livestreamsales.di.components.phonenumberinput.PhoneNumberInputComponent
import dagger.Module

@Module(subcomponents = [
    PhoneNumberInputComponent::class,
    PhoneNumberConfirmationComponent::class
])
class AuthorizationSubComponentsModule