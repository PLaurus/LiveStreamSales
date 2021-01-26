package com.example.livestreamsales.di.components.authorization.modules.subcomponents

import com.example.livestreamsales.di.components.phoneconfirmation.PhoneConfirmationComponent
import com.example.livestreamsales.di.components.phoneinput.PhoneInputComponent
import dagger.Module

@Module(subcomponents = [
    PhoneInputComponent::class,
    PhoneConfirmationComponent::class
])
class AuthorizationSubComponentsModule